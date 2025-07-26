package solver.impl;

import models.Cell;
import models.CellState;
import solver.MazeSolver;

import java.util.*;

/**
 * Implementación del algoritmo de Búsqueda en Profundidad (Depth-First Search) para resolver el laberinto.
 *
 * DFS explora tan profundo como sea posible a lo largo de cada rama antes de retroceder.
 * Utiliza una Pila (Stack) para llevar un registro de las celdas a visitar (LIFO - Last In, First Out).
 * Generalmente encuentra una solución más rápido que BFS, pero no garantiza que sea la ruta más corta.
 */
public class MazeSolverDFS implements MazeSolver {

    @Override
    public List<Cell> solve(Cell[][] maze, Cell start, Cell end) {
        // 1. Estructuras de Datos
        // Pila para las celdas pendientes de visitar (comportamiento LIFO)
        Stack<Cell> stack = new Stack<>();
        // Mapa para reconstruir el camino. Guarda: <Hijo, Padre>
        Map<Cell, Cell> parentMap = new HashMap<>();
        // Matriz para marcar celdas visitadas
        boolean[][] visited = new boolean[maze.length][maze[0].length];

        // 2. Inicialización
        // Empezamos la búsqueda desde la celda de inicio
        stack.push(start);
        visited[start.getRow()][start.getCol()] = true;

        Cell current = null;
        boolean found = false;

        // 3. Bucle Principal de Búsqueda
        while (!stack.isEmpty()) {
            current = stack.pop(); // Sacamos el último elemento añadido a la pila

            // Si hemos llegado al final, terminamos la búsqueda
            if (current.equals(end)) {
                found = true;
                break;
            }

            // 4. Explorar Vecinos (Arriba, Abajo, Izquierda, Derecha)
            int[] dr = {-1, 1, 0, 0}; // Cambios en las filas
            int[] dc = {0, 0, -1, 1}; // Cambios en las columnas

            for (int i = 0; i < 4; i++) {
                int newRow = current.getRow() + dr[i];
                int newCol = current.getCol() + dc[i];

                // Si el vecino es una celda válida...
                if (isValid(maze, visited, newRow, newCol)) {
                    visited[newRow][newCol] = true; // La marcamos como visitada
                    Cell neighbor = maze[newRow][newCol];
                    parentMap.put(neighbor, current); // Guardamos quién es su padre
                    stack.push(neighbor); // La añadimos a la pila para visitarla después
                }
            }
        }

        // 5. Reconstrucción del Camino
        if (found) {
            return reconstructPath(parentMap, start, end);
        }

        return Collections.emptyList();
    }

    /**
     * Verifica si una celda en una posición dada es un movimiento válido.
     * (Este método es idéntico al de BFS)
     */
    private boolean isValid(Cell[][] maze, boolean[][] visited, int row, int col) {
        if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length) {
            return false;
        }
        if (maze[row][col].getState() == CellState.WALL) {
            return false;
        }
        return !visited[row][col];
    }

    /**
     * Reconstruye el camino desde la celda final hasta la inicial usando el mapa de padres.
     * (Este método es idéntico al de BFS)
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
MazeSolverRecursivo.java