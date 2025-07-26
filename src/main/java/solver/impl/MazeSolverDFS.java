package solver.impl;

import java.util.*;
import models.Cell;
import models.CellState;
import models.SolveResult;
import solver.MazeSolver;

/**
 * Implementación del algoritmo de Búsqueda en Profundidad (DFS) que devuelve un resultado completo.
 */
public class MazeSolverDFS implements MazeSolver {

    /**
     * Resuelve el laberinto utilizando el algoritmo DFS.
     * @param maze La matriz 2D que representa el laberinto.
     * @param start La celda de inicio.
     * @param end La celda de destino.
     * @return Un objeto SolveResult que contiene la lista de celdas exploradas y la ruta de la solución.
     */
    @Override
    public SolveResult solve(Cell[][] maze, Cell start, Cell end) {
        Stack<Cell> stack = new Stack<>();
        Map<Cell, Cell> parentMap = new HashMap<>();
        List<Cell> exploredOrder = new ArrayList<>();

        stack.push(start);
        parentMap.put(start, null);

        boolean found = false;

        while (!stack.isEmpty()) {
            Cell current = stack.pop();
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
                    stack.push(neighbor);
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
