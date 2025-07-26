package models;

import java.util.Objects;

/**
 * Representa una celda individual en la cuadrícula del laberinto.
 * Cada celda tiene una posición (fila y columna) y un estado.
 * Es crucial sobreescribir equals() y hashCode() para que las estructuras de datos
 * (como HashMaps o Sets) puedan identificar celdas por su posición, no por su
 * referencia en memoria.
 */
public class Cell {

    private int row;
    private int col;
    private CellState state;

    /**
     * Constructor para crear una nueva celda.
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.state = CellState.PATH; // Por defecto, una celda nueva es un camino.
    }

    // --- Getters y Setters ---

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    // --- Métodos cruciales sobreescritos ---

    /**
     * Compara si esta celda es igual a otro objeto.
     * Dos celdas se consideran iguales si están en la misma fila y columna.
     * El estado (WALL, PATH, etc.) no se considera para la igualdad de posición.
     * @param o El objeto a comparar.
     * @return true si las celdas tienen la misma posición, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    /**
     * Genera un código hash para la celda basado en su posición.
     * Es fundamental que si dos objetos son iguales según equals(), también
     * tengan el mismo hashCode().
     * @return Un código hash entero.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Representación en String de la celda, útil para depuración.
     * @return Un string como "Cell(3,4)".
     */
    @Override
    public String toString() {
        return "Cell(" + row + "," + col + ")";
    }
}
