package solver.impl;

import models.Cell;
import models.CellState;
import solver.MazeSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementación de un algoritmo recursivo con backtracking explícito.
 *
 * Esta es la versión más robusta de los algoritmos recursivos. Si un camino
 * resulta ser un callejón sin salida, la función no solo retorna 'false',
 * sino que también "limpia su rastro", desmarcando la celda actual del arreglo
 * 'visited'. Esto permite que la celda pueda ser utilizada por otra ruta
 * potencial en el futuro de la exploración.
 */
public class MazeSolverRecursivoCompletoBT implements MazeSolver {

    private Cell[][] maze;
    private boolean[][] visited;
    private List<Cell> path;

    @Override
    public List<Cell> solve(Cell[][] maze, Cell start, Cell end) {
        this.maze = maze;
        this.visited = new boolean[maze.length][maze[0].length];
        this.path = new ArrayList<>();

        if (findPath(start.getRow(), start.getCol(), end)) {
            return path;
        }

        return Collections.emptyList();
    }

    private boolean findPath(int row, int col, Cell end) {
        // --- CASOS BASE ---
        if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length) {
            return false;
        }
        if (maze[row][col].getState() == CellState.WALL || visited[row][col]) {
            return false;
        }

        // Marcamos la celda como parte del camino potencial ACTUAL
        visited[row][col] = true;
        Cell current = maze[row][col];

        if (current.equals(end)) {
            path.add(current);
            return true;
        }

        // --- PASO RECURSIVO (4 DIRECCIONES) ---
        int[] dr = {1, -1, 0, 0}; // Abajo, Arriba
        int[] dc = {0, 0, 1, -1}; // Derecha, Izquierda

        for (int i = 0; i < 4; i++) {
            if (findPath(row + dr[i], col + dc[i], end)) {
                path.add(0, current); // Si se encontró un camino, nos añadimos y retornamos éxito
                return true;
            }
        }

        // Si después de probar las 4 direcciones ninguna llevó a la solución,
        // significa que esta celda es un callejón sin salida.
        // La desmarcamos para que pueda ser usada por otra ruta.
        visited[row][col] = false;
        return false;
    }
}
