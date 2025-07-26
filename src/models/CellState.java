package models;

/**
 * Enum que define los posibles estados de una celda en el laberinto.
 * Esto nos ayuda a evitar usar números o strings "mágicos" en el código,
 * haciendo que sea más legible y seguro.
 */
public enum CellState {
    /**
     * Representa un camino libre por donde se puede transitar.
     */
    PATH,

    /**
     * Representa un muro o un obstáculo infranqueable.
     */
    WALL,

    /**
     * Representa la celda de inicio del laberinto (Punto A).
     */
    START,

    /**
     * Representa la celda de destino del laberinto (Punto B).
     */
    END,

    /**
     * Representa una celda que forma parte del camino de la solución encontrada.
     * Se usará para visualizar el resultado.
     */
    SOLUTION,
    
    /**
     * Representa una celda que fue visitada durante la búsqueda pero no es parte
     * de la solución final (útil para visualizar el backtracking).
     */
    VISITED
}
