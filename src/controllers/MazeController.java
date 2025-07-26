package controllers;

import models.Cell;
import models.CellState;
import models.MazeGenerator; // ¡Importante! Necesitas crear esta clase.
import solver.MazeSolver;
import solver.impl.MazeSolverBFS;
import solver.impl.MazeSolverDFS; // Asumo que tienes una clase MazeSolverDFS similar a la de BFS.
import views.MazeFrame;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * El controlador que conecta la vista (MazeFrame) con el modelo (Cell, MazeGenerator)
 * y la lógica de resolución (MazeSolver). Escucha los eventos de los botones y
 * orquesta las operaciones de generación y resolución del laberinto.
 */
public class MazeController implements ActionListener {

    private final MazeFrame view;
    private final MazeSolver bfsSolver;
    private final MazeSolver dfsSolver;
    private Cell[][] currentMaze;

    public MazeController(MazeFrame view) {
        this.view = view;
        this.bfsSolver = new MazeSolverBFS();
        this.dfsSolver = new MazeSolverDFS(); 
        this.view.addController(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Generar Laberinto":
                if (currentMaze != null) {
                    view.getMazePanel().clearSolution();
                }
                
                // --- ¡ACCIÓN REQUERIDA! ---
                // Debes crear la clase 'MazeGenerator' para que esta línea funcione.
                currentMaze = MazeGenerator.generate(getSelectedSize());
                
                view.getMazePanel().setMaze(currentMaze);
                view.getMazePanel().repaint();
                break;

            case "Resolver con BFS":
                solveMaze(bfsSolver);
                break;

            case "Resolver con DFS":
                solveMaze(dfsSolver);
                break;

            case "Limpiar Camino":
                if (currentMaze == null) return;
                view.getMazePanel().clearSolution();
                break;
                
            case "Ver Resultados":
                JOptionPane.showMessageDialog(view, "Funcionalidad 'Ver Resultados' no implementada.", "Información", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    /**
     * Lógica genérica para resolver el laberinto usando un solver específico.
     * @param solver El algoritmo a utilizar (BFS, DFS, etc.).
     */
    private void solveMaze(MazeSolver solver) {
        if (currentMaze == null) {
            JOptionPane.showMessageDialog(view, "Primero debes generar un laberinto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        view.getMazePanel().clearSolution(); // Limpia el camino anterior si existe

        Cell start = findCell(currentMaze, CellState.START);
        Cell end = findCell(currentMaze, CellState.END);

        if (start == null || end == null) {
            JOptionPane.showMessageDialog(view, "No se encontró el punto de inicio o fin en el laberinto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Cell> path = solver.solve(currentMaze, start, end);

        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No se encontró una solución con " + solver.getClass().getSimpleName(), "Sin Solución", JOptionPane.WARNING_MESSAGE);
        } else {
            view.getMazePanel().drawSolution(path);
        }
    }

    /**
     * Obtiene el tamaño seleccionado en el JComboBox de la vista.
     * @return El tamaño (ancho o alto) del laberinto a generar.
     */
    private int getSelectedSize() {
        String selected = (String) view.getComboTamaño().getSelectedItem();
        return Integer.parseInt(selected.split("x")[0]);
    }

    /**
     * Busca y devuelve la primera celda que coincida con el estado especificado.
     * @param maze El laberinto en el que buscar.
     * @param state El estado de la celda a encontrar (START o END).
     * @return La celda encontrada o null si no existe.
     */
    private Cell findCell(Cell[][] maze, CellState state) {
        for (Cell[] row : maze) {
            for (Cell cell : row) {
                if (cell.getState() == state) {
                    return cell;
                }
            }
        }
        return null;
    }
}