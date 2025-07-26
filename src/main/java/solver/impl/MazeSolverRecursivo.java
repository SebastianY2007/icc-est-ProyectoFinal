package solver.impl;

import java.util.ArrayList;
import java.util.List;
import models.Cell;
import models.CellState;
import models.SolveResult;
import solver.MazeSolver;

/**
 * Implementación de un algoritmo recursivo simple (2 direcciones) que devuelve un resultado completo.
 */
public class MazeSolverRecursivo implements MazeSolver {

    private Cell[][] maze;
    private boolean[][] visited;
    private List<Cell> solutionPath;
    private List<Cell> exploredOrder;

    /**
     * Resuelve el laberinto utilizando un algoritmo recursivo simple.
     * @param maze La matriz 2D que representa el laberinto.
     * @param start La celda de inicio.
     * @param end La celda de destino.
     * @return Un objeto SolveResult que contiene la lista de celdas exploradas y la ruta de la solución.
     */
    @Override
    public SolveResult solve(Cell[][] maze, Cell start, Cell end) {
        this.maze = maze;
        this.visited = new boolean[maze.length][maze[0].length];
        this.solutionPath = new ArrayList<>();
        this.exploredOrder = new ArrayList<>();

        findPath(start.getRow(), start.getCol(), end);
        
        return new SolveResult(exploredOrder, solutionPath);
    }

    private boolean findPath(int row, int col, Cell end) {
        if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length) {
            return false;
        }
        if (maze[row][col].getState() == CellState.WALL || visited[row][col]) {
            return false;
        }

        visited[row][col] = true;
        Cell current = maze[row][col];
        exploredOrder.add(current);

        if (current.equals(end)) {
            solutionPath.add(current);
            return true;
        }

        if (findPath(row + 1, col, end)) {
            solutionPath.add(0, current);
            return true;
        }

        if (findPath(row, col + 1, end)) {
            solutionPath.add(0, current);
            return true;
        }
        
        return false;
    }
}
