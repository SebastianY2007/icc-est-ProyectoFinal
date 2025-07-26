package dao.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;

/**
 * Implementación del DAO que utiliza un archivo CSV para la persistencia de datos.
 *
 * Esta clase maneja toda la lógica de bajo nivel para leer y escribir en el archivo
 * "results.csv". Utiliza un enfoque de "leer todo, modificar en memoria, escribir todo"
 * para garantizar la consistencia de los datos.
 */
public class AlgorithmResultDAOFile implements AlgorithmResultDAO {

    private final String filePath = "results.csv";

    public AlgorithmResultDAOFile() {
        // Asegurarse de que el archivo exista al crear el DAO.
        try {
            if (!Files.exists(Paths.get(filePath))) {
                Files.createFile(Paths.get(filePath));
            }
        } catch (IOException e) {
            System.err.println("Error al crear el archivo de resultados: " + e.getMessage());
        }
    }

    @Override
    public void saveOrUpdate(AlgorithmResult result) {
        // Usamos LinkedHashMap para mantener el orden de inserción.
        Map<String, AlgorithmResult> resultsMap = new LinkedHashMap<>();
        
        // 1. Leer todos los resultados existentes y cargarlos en el mapa.
        for (AlgorithmResult existingResult : findAll()) {
            resultsMap.put(existingResult.getAlgorithmName(), existingResult);
        }

        // 2. Añadir o actualizar el nuevo resultado en el mapa.
        resultsMap.put(result.getAlgorithmName(), result);

        // 3. Escribir el contenido completo del mapa de vuelta al archivo.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) { 
            for (AlgorithmResult res : resultsMap.values()) {
                writer.write(res.toCsvRow());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el resultado en el archivo: " + e.getMessage());
        }
    }

    @Override
    public List<AlgorithmResult> findAll() {
        List<AlgorithmResult> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        results.add(AlgorithmResult.fromCsvRow(line));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Omitiendo línea mal formada en CSV: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de resultados: " + e.getMessage());
        }
        return results;
    }

    @Override
    public void clearAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
        } catch (IOException e) {
            System.err.println("Error al limpiar el archivo de resultados: " + e.getMessage());
        }
    }
}
