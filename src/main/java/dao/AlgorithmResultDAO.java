package dao;

import models.AlgorithmResult;
import java.util.List;

/**
 * Interfaz para el Objeto de Acceso a Datos (DAO) de los resultados de los algoritmos.
 *
 * Define las operaciones estándar de persistencia (CRUD - Create, Read, Update, Delete)
 * que se pueden realizar sobre los resultados, independientemente de dónde o cómo
 * se almacenen los datos (archivo de texto, base de datos, etc.).
 */
public interface AlgorithmResultDAO {

    /**
     * Guarda un nuevo resultado o actualiza uno existente.
     * Si ya existe un resultado para el mismo algoritmo, se deben actualizar
     * sus datos (tiempo, longitud). Si no existe, se debe agregar.
     *
     * @param result El objeto AlgorithmResult a guardar o actualizar.
     */
    void saveOrUpdate(AlgorithmResult result);

    /**
     * Recupera todos los resultados almacenados.
     *
     * @return Una lista de todos los objetos AlgorithmResult. Si no hay
     * resultados, devuelve una lista vacía.
     */
    List<AlgorithmResult> findAll();

    /**
     * Elimina todos los resultados almacenados.
     * Limpia el historial por completo.
     */
    void clearAll();

}
