package controllers;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.HyperlinkEvent;

import dao.AlgorithmResultDAO;
import dao.impl.AlgorithmResultDAOFile;
import models.AlgorithmResult;
import models.Cell;
import models.CellState;
import models.SolveResult;
import solver.MazeSolver;
import solver.impl.MazeSolverBFS;
import solver.impl.MazeSolverDFS;
import solver.impl.MazeSolverRecursivo;
import solver.impl.MazeSolverRecursivoCompleto;
import solver.impl.MazeSolverRecursivoCompletoBT;
import views.MazeFrame;
import views.ResultsChart;
import views.ResultsDialog;

/**
 * El Controlador en el patrón MVC, orquesta las animaciones y la lógica de la aplicación.
 */
public class MazeController extends MouseAdapter implements ActionListener {

    private MazeFrame view;
    private Cell[][] mazeModel;
    private Cell startCell, endCell;
    private AlgorithmResultDAO resultDAO;
    private Timer animationTimer;
    private ResultsDialog resultsDialog;

    private enum EditMode { SET_START, SET_END, TOGGLE_WALL }
    private EditMode currentEditMode = EditMode.TOGGLE_WALL;

    private SolveResult stepByStepResult;
    private int currentExplorationStep = 0;
    private int currentSolutionStep = 0;
    private boolean isExploringStepByStep = true;

    /**
     * Constructor del controlador.
     * @param rows Filas iniciales del laberinto.
     * @param cols Columnas iniciales del laberinto.
     */
    public MazeController(int rows, int cols) {
        this.mazeModel = createEmptyMaze(rows, cols);
        this.view = new MazeFrame(rows, cols);
        this.resultDAO = new AlgorithmResultDAOFile();
        this.view.addController(this);
        this.view.getMazePanel().addMouseClickListener(this);
        this.view.getMazePanel().setMazeModel(this.mazeModel);
    }

    /**
     * Maneja todos los eventos de clic en botones y menús.
     * @param e El evento de acción que ocurrió.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (animationTimer != null && animationTimer.isRunning()) return;

        switch (command) {
            case "Set Start": currentEditMode = EditMode.SET_START; break;
            case "Set End": currentEditMode = EditMode.SET_END; break;
            case "Toggle Wall": currentEditMode = EditMode.TOGGLE_WALL; break;
            case "Resolver": solveMazeAnimated(); break;
            case "Paso a paso": executeStep(); break;
            case "Limpiar": clearBoard(); break;
            case "Nuevo laberinto": createNewMaze(); break;
            case "Ver resultados": showResults(); break;
            case "Limpiar Resultados": clearResults(); break;
            case "Graficar Resultados": plotResults(); break;
            case "Acerca de...": showAboutDialog(); break;
        }
    }

    /**
     * Maneja los eventos de clic del ratón en el MazePanel para diseñar el laberinto.
     * @param e El evento del ratón.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (animationTimer != null && animationTimer.isRunning()) return;
        int col = view.getMazePanel().getColFromX(e.getX());
        int row = view.getMazePanel().getRowFromY(e.getY());

        if (row < 0 || row >= mazeModel.length || col < 0 || col >= mazeModel[0].length) return;

        Cell clickedCell = mazeModel[row][col];
        switch (currentEditMode) {
            case SET_START:
                if (startCell != null) startCell.setState(CellState.PATH);
                startCell = clickedCell;
                startCell.setState(CellState.START);
                break;
            case SET_END:
                if (endCell != null) endCell.setState(CellState.PATH);
                endCell = clickedCell;
                endCell.setState(CellState.END);
                break;
            case TOGGLE_WALL:
                if (clickedCell.getState() == CellState.PATH) clickedCell.setState(CellState.WALL);
                else if (clickedCell.getState() == CellState.WALL) clickedCell.setState(CellState.PATH);
                break;
        }
        view.getMazePanel().repaint();
    }

    private void solveMazeAnimated() {
        if (startCell == null || endCell == null) {
            JOptionPane.showMessageDialog(view, "Por favor, establece un punto de inicio y un punto final.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        clearSolution();
        
        String selectedAlgorithm = view.getSelectedAlgorithm();
        MazeSolver solver = getSolverForName(selectedAlgorithm);

        long startTime = System.nanoTime();
        SolveResult result = solver.solve(mazeModel, startCell, endCell);
        long endTime = System.nanoTime();

        animateExploration(result.getExploredCells(), () -> {
            if (result.isSolutionFound()) {
                animateSolution(result.getSolutionPath());
                resultDAO.saveOrUpdate(new AlgorithmResult(selectedAlgorithm, (endTime - startTime), result.getSolutionPath().size()));
            } else {
                JOptionPane.showMessageDialog(view, "No se encontró una solución.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void animateExploration(List<Cell> explored, Runnable onFinish) {
        final int[] index = {0};
        animationTimer = new Timer(20, e -> {
            if (index[0] < explored.size()) {
                Cell cell = explored.get(index[0]++);
                if (cell.getState() == CellState.PATH) cell.setState(CellState.VISITED);
                view.getMazePanel().repaint();
            } else {
                ((Timer) e.getSource()).stop();
                onFinish.run();
            }
        });
        animationTimer.start();
    }

    private void animateSolution(List<Cell> solution) {
        final int[] index = {0};
        animationTimer = new Timer(50, e -> {
            if (index[0] < solution.size()) {
                Cell cell = solution.get(index[0]++);
                if (cell.getState() != CellState.START && cell.getState() != CellState.END) {
                    cell.setState(CellState.SOLUTION);
                }
                view.getMazePanel().repaint();
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        animationTimer.start();
    }

    private void executeStep() {
        if (stepByStepResult == null) {
            if (startCell == null || endCell == null) {
                JOptionPane.showMessageDialog(view, "Establece inicio y fin primero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            clearSolution();
            MazeSolver solver = getSolverForName(view.getSelectedAlgorithm());
            stepByStepResult = solver.solve(mazeModel, startCell, endCell);
        }

        if (isExploringStepByStep) {
            if (currentExplorationStep < stepByStepResult.getExploredCells().size()) {
                Cell cell = stepByStepResult.getExploredCells().get(currentExplorationStep++);
                if (cell.getState() == CellState.PATH) cell.setState(CellState.VISITED);
                view.getMazePanel().repaint();
            } else {
                isExploringStepByStep = false;
                if (!stepByStepResult.isSolutionFound()) {
                    JOptionPane.showMessageDialog(view, "Exploración completada. No se encontró solución.", "Paso a paso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else if (stepByStepResult.isSolutionFound()) {
            if (currentSolutionStep < stepByStepResult.getSolutionPath().size()) {
                Cell cell = stepByStepResult.getSolutionPath().get(currentSolutionStep++);
                if (cell.getState() != CellState.START && cell.getState() != CellState.END) {
                    cell.setState(CellState.SOLUTION);
                }
                view.getMazePanel().repaint();
            } else {
                JOptionPane.showMessageDialog(view, "Paso a paso completado.", "Paso a paso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void clearBoard() {
        if (animationTimer != null && animationTimer.isRunning()) animationTimer.stop();
        startCell = null;
        endCell = null;
        for (Cell[] row : mazeModel) {
            for (Cell cell : row) {
                cell.setState(CellState.PATH);
            }
        }
        clearSolution();
    }

    private void clearSolution() {
        currentExplorationStep = 0;
        currentSolutionStep = 0;
        stepByStepResult = null;
        isExploringStepByStep = true;
        for (Cell[] row : mazeModel) {
            for (Cell cell : row) {
                if (cell.getState() == CellState.SOLUTION || cell.getState() == CellState.VISITED) {
                    cell.setState(CellState.PATH);
                }
            }
        }
        view.getMazePanel().repaint();
    }

    private void showResults() {
        if (resultsDialog == null) {
            resultsDialog = new ResultsDialog(view);
            resultsDialog.addController(this);
        }
        
        List<AlgorithmResult> results = resultDAO.findAll();
        resultsDialog.populateTable(results);
        resultsDialog.setVisible(true);
    }
    
    private void clearResults() {
        int response = JOptionPane.showConfirmDialog(resultsDialog, "¿Estás seguro de que quieres borrar todos los resultados?", "Confirmar Limpieza", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            resultDAO.clearAll();
            resultsDialog.populateTable(resultDAO.findAll());
        }
    }

    private void plotResults() {
        List<AlgorithmResult> results = resultDAO.findAll();
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(resultsDialog, "No hay resultados para graficar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            ResultsChart chart = new ResultsChart("Comparación de Rendimiento de Algoritmos", results);
            chart.setVisible(true);
        });
    }
    
    private MazeSolver getSolverForName(String name) {
        switch (name) {
            case "BFS": return new MazeSolverBFS();
            case "DFS": return new MazeSolverDFS();
            case "Recursivo": return new MazeSolverRecursivo();
            case "Recursivo Completo": return new MazeSolverRecursivoCompleto();
            case "Recursivo Completo BT": default: return new MazeSolverRecursivoCompletoBT();
        }
    }
    private void createNewMaze() {
        view.dispose();
        int rows = askForDimension("filas");
        int cols = askForDimension("columnas");
        SwingUtilities.invokeLater(() -> new MazeController(rows, cols));
    }
    /**
     * Muestra la ventana "Acerca de" con información de los desarrolladores y enlaces clicables.
     */
    private void showAboutDialog() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setOpaque(false);

        String htmlContent = "<html><body style='font-family: sans-serif; text-align: center;'>"
                           + "<h2>Maze Creator</h2>"
                           + "<p>Desarrollado por:</p>"
                           + "<p><b>Sebastian Yupangui</b><br>"
                           + "<a href='https://github.com/SebastianY2007'>SebastianY2007</a></p>"
                           + "<p><b>Javier Barrezueta</b><br>"
                           + "<a href='https://github.com/JavierBzt17'>JavierBzt17</a></p>"
                           + "</body></html>";

        editorPane.setText(htmlContent);

        editorPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JOptionPane.showMessageDialog(view, editorPane, "Acerca de Maze Creator", JOptionPane.INFORMATION_MESSAGE);
    }
    private Cell[][] createEmptyMaze(int rows, int cols) {
        Cell[][] maze = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = new Cell(i, j);
            }
        }
        return maze;
    }
    private int askForDimension(String dimensionName) {
        String input;
        int value = 0;
        boolean isValid = false;
        while (!isValid) {
            input = JOptionPane.showInputDialog(null, "Ingrese número de " + dimensionName + " (mayor a 4):", "Entrada", JOptionPane.QUESTION_MESSAGE);
            if (input == null) System.exit(0);
            try {
                value = Integer.parseInt(input);
                if (value > 4) isValid = true;
                else JOptionPane.showMessageDialog(null, "Por favor, ingrese un número mayor que 4.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada no válida. Por favor, ingrese un número entero.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
        return value;
    }
}
