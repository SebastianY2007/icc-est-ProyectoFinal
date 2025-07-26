package solver.impl;

import models.Cell;
import models.CellState;
import solver.MazeSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementación de un algoritmo recursivo simple para resolver el laberinto.
 *
 * Esta versión solo intenta moverse en dos direcciones: hacia abajo y hacia la derecha.
 * Es un ejemplo básico para ilustrar el concepto de recursividad y backtracking implícito.
 * No garantiza encontrar un camino aunque exista si este requiere moverse hacia arriba o izquierda.
 */
public class MazeSolverRecursivo implements MazeSolver {

    private Cell[][] maze;
    private boolean[][] visited;
    private List<Cell> path;

    @Override
    public List<Cell> solve(Cell[][] maze, Cell start, Cell end) {
        // Inicializamos las variables de instancia para esta ejecución
        this.maze = maze;
        this.visited = new boolean[maze.length][maze[0].length];
        this.path = new ArrayList<>();

        // Llamamos al método auxiliar recursivo para iniciar la búsqueda
        if (findPath(start.getRow(), start.getCol(), end)) {
            // Si se encontró un camino, lo devolvemos
            return path;
        }

        // Si no, devolvemos una lista vacía
        return Collections.emptyList();
    }

    /**
     * El método recursivo principal que busca el camino.
     * @return true si se encuentra un camino desde (row, col) hasta el final, false en caso contrario.
     */
    private boolean findPath(int row, int col, Cell end) {
        // --- CASOS BASE (Condiciones de parada) ---

        // 1. Fallo: Fuera de los límites del laberinto
        if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length) {
            return false;
        }

        // 2. Fallo: Es un muro o ya ha sido visitado
        if (maze[row][col].getState() == CellState.WALL || visited[row][col]) {
            return false;
        }

        // Marcamos la celda actual como visitada para no entrar en bucles
        visited[row][col] = true;
        Cell current = maze[row][col];

        // 3. Éxito: Hemos llegado a la celda final
        if (current.equals(end)) {
            path.add(current); // Añadimos la celda final al camino
            return true;
        }

        // --- PASO RECURSIVO ---

        // Intentamos movernos hacia ABAJO
        if (findPath(row + 1, col, end)) {
            path.add(0, current); // Si el camino desde abajo tuvo éxito, añadimos la celda actual al inicio
            return true;
        }

        // Intentamos movernos hacia la DERECHA
        if (findPath(row, col + 1, end)) {
            path.add(0, current); // Si el camino desde la derecha tuvo éxito, añadimos la celda actual
            return true;
        }
        
        // Si ninguna dirección funcionó desde aquí, este no es el camino correcto.
        // No hacemos nada, simplemente devolvemos false (backtracking implícito).
        return false;
    }
}
