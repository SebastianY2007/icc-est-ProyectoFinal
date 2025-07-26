package solver.impl;

import models.Cell;
import models.CellState;
import solver.MazeSolver;

import java.util.*;

/**
 * Implementación del algoritmo de Búsqueda en Anchura (Breadth-First Search) para resolver el laberinto.
 *
 * BFS explora el laberinto "capa por capa" desde el punto de inicio. Utiliza una cola (Queue)
 * para llevar un registro de las celdas a visitar. Su principal ventaja es que siempre
 * garantiza encontrar el camino más corto en términos de número de celdas.
 */
public class MazeSolverBFS implements MazeSolver {

    @Override
    public List<Cell> solve(Cell[][] maze, Cell start, Cell end) {
        // 1. Estructuras de Datos
        // Cola para las celdas pendientes de visitar (comportamiento FIFO)
        Queue<Cell> queue = new LinkedList<>();
        // Mapa para reconstruir el camino. Guarda: <Hijo, Padre>
        Map<Cell, Cell> parentMap = new HashMap<>();
        // Matriz para marcar celdas visitadas y no volver a procesarlas
        boolean[][] visited = new boolean[maze.length][maze[0].length];

        // 2. Inicialización
        // Empezamos la búsqueda desde la celda de inicio
        queue.add(start);
        visited[start.getRow()][start.getCol()] = true;

        Cell current = null;
        boolean found = false;

        // 3. Bucle Principal de Búsqueda
        while (!queue.isEmpty()) {
            current = queue.poll(); // Sacamos el siguiente elemento de la cola

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
                    queue.add(neighbor); // La añadimos a la cola para visitarla después
                }
            }
        }

        // 5. Reconstrucción del Camino
        if (found) {
            return reconstructPath(parentMap, start, end);
        }

        // Si no se encontró, devolvemos una lista vacía
        return Collections.emptyList();
    }

    /**
     * Verifica si una celda en una posición dada es un movimiento válido.
     */
    private boolean isValid(Cell[][] maze, boolean[][] visited, int row, int col) {
        // ¿Está dentro de los límites del laberinto?
        if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length) {
            return false;
        }
        // ¿Es un muro?
        if (maze[row][col].getState() == CellState.WALL) {
            return false;
        }
        // ¿Ya la hemos visitado?
        if (visited[row][col]) {
            return false;
        }
        return true;
    }

    /**
     * Reconstruye el camino desde la celda final hasta la inicial usando el mapa de padres.
     */
    private List<Cell> reconstructPath(Map<Cell, Cell> parentMap, Cell start, Cell end) {
        LinkedList<Cell> path = new LinkedList<>();
        Cell current = end;

        // Trazamos el camino hacia atrás desde el final
        while (current != null) {
            path.addFirst(current); // Añadimos al principio para que el camino quede en orden
            current = parentMap.get(current);
        }

        // Verificamos si el camino encontrado realmente comienza en 'start'
        if (path.getFirst().equals(start)) {
            return path;
        }

        return Collections.emptyList(); // No debería ocurrir si 'found' es true, pero es una salvaguarda
    }
}
