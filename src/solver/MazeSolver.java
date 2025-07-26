package solver;

import models.Cell;
import java.util.List;

/**
 * Interfaz que define el contrato para cualquier algoritmo que resuelva un laberinto.
 *
 * El uso de una interfaz nos permite aplicar el Patrón de Diseño Estrategia (Strategy Pattern).
 * El controlador puede tener una referencia a un 'MazeSolver' sin saber qué algoritmo
 * concreto se está utilizando (BFS, DFS, etc.). Esto hace que el sistema sea flexible
 * y fácil de extender con nuevos algoritmos en el futuro.
 */
public interface MazeSolver {

    /**
     * Método principal para resolver el laberinto.
     *
     * @param maze La matriz 2D de celdas que representa el laberinto.
     * @param start La celda de inicio.
     * @param end La celda de destino.
     * @return Una lista de celdas (List<Cell>) que representa el camino desde el
     * inicio hasta el fin. Si no se encuentra una solución, debe devolver
     * una lista vacía (nunca null).
     */
    List<Cell> solve(Cell[][] maze, Cell start, Cell end);

}
