package models;

import java.util.List;

/**
 * Encapsula el resultado completo de una ejecución de un algoritmo de resolución.
 * Contiene tanto la lista de todas las celdas exploradas como el camino de la solución final.
 */
public class SolveResult {
    private final List<Cell> exploredCells;
    private final List<Cell> solutionPath;

    /**
     * Constructor para el resultado de la resolución.
     * @param exploredCells La lista de celdas visitadas en orden de exploración.
     * @param solutionPath La lista de celdas que forman el camino final.
     */
    public SolveResult(List<Cell> exploredCells, List<Cell> solutionPath) {
        this.exploredCells = exploredCells;
        this.solutionPath = solutionPath;
    }

    /**
     * Obtiene la lista de todas las celdas que fueron exploradas durante la búsqueda.
     * @return Una lista de celdas exploradas.
     */
    public List<Cell> getExploredCells() {
        return exploredCells;
    }

    /**
     * Obtiene la lista de celdas que componen el camino de la solución final.
     * @return Una lista de celdas de la solución.
     */
    public List<Cell> getSolutionPath() {
        return solutionPath;
    }

    /**
     * Verifica si se encontró una solución.
     * @return true si el camino de la solución no es nulo y no está vacío, false en caso contrario.
     */
    public boolean isSolutionFound() {
        return solutionPath != null && !solutionPath.isEmpty();
    }
}
