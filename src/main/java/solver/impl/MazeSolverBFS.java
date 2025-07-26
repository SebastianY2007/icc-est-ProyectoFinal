package solver.impl;

import models.Cell;
import models.CellState;
import models.SolveResult;
import solver.MazeSolver;

import java.util.*;

/**
 * Implementación del algoritmo de Búsqueda en Anchura (BFS) que devuelve un resultado completo.
 * Retorna tanto las celdas exploradas para la animación como el camino de la solución.
 */
public class MazeSolverBFS implements MazeSolver {

    /**
     * Resuelve el laberinto utilizando el algoritmo BFS.
     * @param maze La matriz 2D que representa el laberinto.
     * @param start La celda de inicio.
     * @param end La celda de destino.
     * @return Un objeto SolveResult que contiene la lista de celdas exploradas y la ruta de la solución.
     */
    @Override
    public SolveResult solve(Cell[][] maze, Cell start, Cell end) {
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> parentMap = new HashMap<>();
        List<Cell> exploredOrder = new ArrayList<>();

        queue.add(start);
        parentMap.put(start, null);

        boolean found = false;

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            exploredOrder.add(current);

            if (current.equals(end)) {
                found = true;
                break;
            }

            int[] dr = {-1, 1, 0, 0};
            int[] dc = {0, 0, -1, 1};

            for (int i = 0; i < 4; i++) {
                int newRow = current.getRow() + dr[i];
                int newCol = current.getCol() + dc[i];

                if (isValid(maze, parentMap, newRow, newCol)) {
                    Cell neighbor = maze[newRow][newCol];
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        List<Cell> solutionPath = new ArrayList<>();
        if (found) {
            Cell step = end;
            while (step != null) {
                solutionPath.add(0, step);
                step = parentMap.get(step);
            }
        }
        
        return new SolveResult(exploredOrder, solutionPath);
    }

    /**
     * Verifica si una celda es un movimiento válido.
     * @param maze La matriz del laberinto.
     * @param parentMap El mapa de celdas visitadas.
     * @param row La fila a verificar.
     * @param col La columna a verificar.
     * @return true si la celda es válida, false en caso contrario.
     */
    private boolean isValid(Cell[][] maze, Map<Cell, Cell> parentMap, int row, int col) {
        if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length) {
            return false;
        }
        if (maze[row][col].getState() == CellState.WALL) {
            return false;
        }
        return !parentMap.containsKey(maze[row][col]);
    }
}
