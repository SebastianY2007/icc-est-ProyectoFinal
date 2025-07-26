package solver.impl;

import models.Cell;
import models.CellState;
import solver.MazeSolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Implementación del algoritmo de Búsqueda en Profundidad (DFS) utilizando un enfoque recursivo.
 * La pila de llamadas de la función reemplaza la necesidad de una estructura de datos Stack explícita.
 */
public class MazeSolverDFS implements MazeSolver {

    @Override
    public List<Cell> solve(Cell[][] maze, Cell start, Cell end) {
        // Estructuras de datos para el seguimiento
        boolean[][] visited = new boolean[maze.length][maze[0].length];
        Map<Cell, Cell> parentMap = new HashMap<>();

        // Inicia la búsqueda recursiva desde el punto de partida
        boolean found = findPathRecursive(maze, start, end, visited, parentMap);

        // Si se encontró un camino, reconstrúyelo. De lo contrario, devuelve una lista vacía.
        if (found) {
            return reconstructPath(parentMap, start, end);
        }
        return Collections.emptyList();
    }

    /**
     * El método recursivo principal que busca un camino.
     * @return true si se encuentra un camino desde 'current' hasta 'end', false en caso contrario.
     */
    private boolean findPathRecursive(Cell[][] maze, Cell current, Cell end, boolean[][] visited, Map<Cell, Cell> parentMap) {
        // Caso base de éxito: hemos llegado al final.
        if (current.equals(end)) {
            return true;
        }

        // Marca la celda actual como visitada.
        visited[current.getRow()][current.getCol()] = true;

        // Explora los vecinos.
        int[] dr = {-1, 1, 0, 0}; // Cambios en las filas
        int[] dc = {0, 0, -1, 1}; // Cambios en las columnas

        for (int i = 0; i < 4; i++) {
            int newRow = current.getRow() + dr[i];
            int newCol = current.getCol() + dc[i];

            if (isValid(maze, visited, newRow, newCol)) {
                Cell neighbor = maze[newRow][newCol];
                parentMap.put(neighbor, current); // Guarda el camino

                // Llama recursivamente para el vecino.
                // Si la llamada encuentra el final, propaga el éxito hacia arriba.
                if (findPathRecursive(maze, neighbor, end, visited, parentMap)) {
                    return true;
                }
            }
        }

        // Caso base de fallo: ningún vecino desde 'current' conduce a la solución.
        return false;
    }

    /**
     * Verifica si una celda en una posición dada es un movimiento válido.
     */
    private boolean isValid(Cell[][] maze, boolean[][] visited, int row, int col) {
        // ¿Está dentro de los límites?
        if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length) {
            return false;
        }
        // ¿Es un muro?
        if (maze[row][col].getState() == CellState.WALL) {
            return false;
        }
        // ¿Ya fue visitada?
        return !visited[row][col];
    }

    /**
     * Reconstruye el camino desde la celda final hasta la inicial usando el mapa de padres.
     */
    private List<Cell> reconstructPath(Map<Cell, Cell> parentMap, Cell start, Cell end) {
        LinkedList<Cell> path = new LinkedList<>();
        Cell current = end;

        while (current != null) {
            path.addFirst(current);
            current = parentMap.get(current);
        }

        if (!path.isEmpty() && path.getFirst().equals(start)) {
            return path;
        }

        return Collections.emptyList();
    }
}