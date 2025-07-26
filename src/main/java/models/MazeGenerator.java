package models;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.Random;

/**
 * Clase de utilidad para generar laberintos.
 * Utiliza un algoritmo de Búsqueda en Profundidad (DFS) con retroceso (backtracking)
 * para crear un laberinto perfecto garantizando que haya un camino desde cualquier
 * celda a cualquier otra.
 */
public class MazeGenerator {

    /**
     * Genera una nueva cuadrícula de laberinto del tamaño especificado.
     * @param size El ancho y alto del laberinto a generar.
     * @return Una matriz 2D de Celdas (Cell[][]) que representa el laberinto.
     */
    public static Cell[][] generate(int size) {
        // 1. Inicialización: Crear una cuadrícula donde todo es un muro.
        Cell[][] maze = new Cell[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                maze[row][col] = new Cell(row, col);
                maze[row][col].setState(CellState.WALL);
            }
        }

        Random random = new Random();
        Stack<Cell> stack = new Stack<>();

        int startRow = random.nextInt(size);
        int startCol = random.nextInt(size);
        Cell startCell = maze[startRow][startCol];
        startCell.setState(CellState.PATH);
        stack.push(startCell);

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            List<Cell> neighbors = getUnvisitedNeighbors(current, maze, size);

            if (!neighbors.isEmpty()) {
                Cell neighbor = neighbors.get(random.nextInt(neighbors.size()));

                int wallRow = current.getRow() + (neighbor.getRow() - current.getRow()) / 2;
                int wallCol = current.getCol() + (neighbor.getCol() - current.getCol()) / 2;
                maze[wallRow][wallCol].setState(CellState.PATH);
                neighbor.setState(CellState.PATH);
                stack.push(neighbor);
            } else {
                stack.pop();
            }
        }

        maze[0][0].setState(CellState.START);
        maze[size - 1][size - 1].setState(CellState.END);

        return maze;
    }

    /**
     * Obtiene los vecinos de una celda que están a 2 pasos de distancia y no han sido
     * convertidos en camino todavía (siguen siendo muros).
     */
    private static List<Cell> getUnvisitedNeighbors(Cell cell, Cell[][] maze, int size) {
        int r = cell.getRow();
        int c = cell.getCol();
        
       
        int[][] moves = {{r - 2, c}, {r + 2, c}, {r, c - 2}, {r, c + 2}};
        Stack<Cell> result = new Stack<>();

        for (int[] move : moves) {
            int newRow = move[0];
            int newCol = move[1];
          
            if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size && maze[newRow][newCol].getState() == CellState.WALL) {
                result.push(maze[newRow][newCol]);
            }
        }
        
        Collections.shuffle(result);
        return result;
    }
}