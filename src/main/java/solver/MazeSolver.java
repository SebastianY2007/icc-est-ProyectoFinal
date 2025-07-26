package solver;

import models.Cell;
import models.SolveResult; // Importar la nueva clase de resultado

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
     * @return Un objeto SolveResult que contiene la lista de celdas exploradas
     * y la lista de celdas de la solución.
     */
    SolveResult solve(Cell[][] maze, Cell start, Cell end);

}
