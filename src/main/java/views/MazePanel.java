package views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import javax.swing.*;
import models.Cell;
import models.CellState;

/**
 * Panel personalizado (JPanel) que renderiza visualmente el laberinto.
 *
 * Esta clase es responsable de todo el dibujo. Toma un modelo de datos 2D (Cell[][])
 * y lo traduce en una representación gráfica de celdas de colores. También captura
 * los eventos del ratón para permitir la edición interactiva del laberinto.
 *
 * @author [Tu Nombre]
 * @version 1.0
 */
public class MazePanel extends JPanel {

    private Cell[][] mazeModel;
    private int cellSize;
    private final int rows;
    private final int cols;

    /**
     * Construye el panel del laberinto con dimensiones específicas.
     *
     * @param rows El número de filas de la cuadrícula.
     * @param cols El número de columnas de la cuadrícula.
     */
    public MazePanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        // Inicializa un modelo vacío para evitar errores al inicio.
        this.mazeModel = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mazeModel[i][j] = new Cell(i, j); // Por defecto, todas son PATH
            }
        }

        // Calcula un tamaño preferido para el panel.
        // Esto ayuda al método pack() de JFrame a dimensionar la ventana correctamente.
        int preferredWidth = 500; // Ancho base
        this.cellSize = preferredWidth / cols;
        int preferredHeight = cellSize * rows;
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
    }

    /**
     * El corazón del dibujado. Este método es llamado por Swing cada vez que el
     * panel necesita ser redibujado.
     *
     * @param g El contexto gráfico proporcionado por Swing para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Limpia el fondo del panel

        if (mazeModel == null) {
            return; // No hay nada que dibujar
        }

        // Recalcula el tamaño de la celda basado en el tamaño actual del panel
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int cellWidth = panelWidth / cols;
        int cellHeight = panelHeight / rows;
        this.cellSize = Math.min(cellWidth, cellHeight); // Usa el menor para mantener las celdas cuadradas

        // Itera sobre el modelo de datos y dibuja cada celda
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                drawCell(g, mazeModel[row][col]);
            }
        }
    }

    /**
     * Dibuja una única celda en el panel.
     *
     * @param g El contexto gráfico.
     * @param cell La celda a dibujar.
     */
    private void drawCell(Graphics g, Cell cell) {
        int x = cell.getCol() * cellSize;
        int y = cell.getRow() * cellSize;

        // 1. Elige el color de fondo basado en el estado de la celda
        g.setColor(getColorForState(cell.getState()));
        g.fillRect(x, y, cellSize, cellSize);

        // 2. Dibuja un borde negro para la cuadrícula
        g.setColor(Color.BLACK);
        g.drawRect(x, y, cellSize, cellSize);
    }

    /**
     * Devuelve el color correspondiente a un estado de celda.
     *
     * @param state El estado de la celda.
     * @return El color para dibujar esa celda.
     */
    private Color getColorForState(CellState state) {
        switch (state) {
            case START:
                return Color.GREEN;
            case END:
                return Color.RED;
            case WALL:
                return Color.BLACK;
            case SOLUTION:
                return Color.BLUE;
            case VISITED:
                return Color.GRAY;
            case PATH:
            default:
                return Color.WHITE;
        }
    }

    /**
     * Actualiza el modelo de datos del laberinto y solicita un redibujado.
     * El controlador llamará a este método cuando el modelo cambie.
     *
     * @param mazeModel La nueva matriz de celdas a dibujar.
     */
    public void setMazeModel(Cell[][] mazeModel) {
        this.mazeModel = mazeModel;
        repaint(); // Le dice a Swing que este componente necesita ser redibujado
    }

    /**
     * Asigna un listener para los eventos del ratón.
     * El controlador usará este método para saber cuándo y dónde el usuario hace clic.
     *
     * @param listener El adaptador del ratón que manejará los eventos.
     */
    public void addMouseClickListener(MouseAdapter listener) {
        this.addMouseListener(listener);
    }

    /**
     * Calcula la fila de la celda a partir de una coordenada Y del píxel.
     * @param y La coordenada Y del clic del ratón.
     * @return El índice de la fila.
     */
    public int getRowFromY(int y) {
        return y / cellSize;
    }

    /**
     * Calcula la columna de la celda a partir de una coordenada X del píxel.
     * @param x La coordenada X del clic del ratón.
     * @return El índice de la columna.
     */
    public int getColFromX(int x) {
        return x / cellSize;
    }
}
