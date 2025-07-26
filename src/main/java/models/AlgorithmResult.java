package models;

/**
 * Clase que encapsula el resultado de la ejecución de un algoritmo de resolución.
 * Almacena el nombre del algoritmo, el tiempo que tardó en ejecutarse y la
 * longitud del camino encontrado.
 * Esta clase es un "POJO" (Plain Old Java Object) o una clase de datos simple.
 */
public class AlgorithmResult {

    private String algorithmName;
    private long executionTime; // Se recomienda almacenar en nanosegundos para mayor precisión.
    private int pathLength;

    /**
     * Constructor para crear un nuevo resultado.
     * @param algorithmName El nombre del algoritmo (ej. "BFS", "DFS").
     * @param executionTime El tiempo de ejecución.
     * @param pathLength El número de celdas en la ruta de la solución.
     */
    public AlgorithmResult(String algorithmName, long executionTime, int pathLength) {
        this.algorithmName = algorithmName;
        this.executionTime = executionTime;
        this.pathLength = pathLength;
    }

    // --- Getters y Setters ---

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public int getPathLength() {
        return pathLength;
    }

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }

    /**
     * Convierte los datos del resultado en una fila de texto para un archivo CSV.
     * @return Un String con los valores separados por comas. Ej: "BFS,52341,32"
     */
    public String toCsvRow() {
        return algorithmName + "," + executionTime + "," + pathLength;
    }

    /**
     * Crea un objeto AlgorithmResult a partir de una línea de un archivo CSV.
     * Este método estático es útil para la capa DAO al leer el archivo.
     * @param csvRow La línea de texto del archivo CSV.
     * @return Un nuevo objeto AlgorithmResult.
     * @throws IllegalArgumentException si la fila no tiene el formato correcto.
     */
    public static AlgorithmResult fromCsvRow(String csvRow) {
        String[] parts = csvRow.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("La fila del CSV no es válida: " + csvRow);
        }
        String name = parts[0];
        long time = Long.parseLong(parts[1]);
        int length = Integer.parseInt(parts[2]);
        return new AlgorithmResult(name, time, length);
    }
}
