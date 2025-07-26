package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.AlgorithmResult;

/**
 * Un diálogo (JDialog) que muestra los resultados de las ejecuciones de los algoritmos
 * en una tabla. También proporciona opciones para limpiar los resultados y graficarlos.
 *
 * @author [Tu Nombre]
 * @version 1.0
 */
public class ResultsDialog extends JDialog {

    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JButton limpiarButton;
    private JButton graficarButton;

    /**
     * Construye el diálogo de resultados.
     * @param owner El JFrame padre sobre el cual se mostrará este diálogo.
     */
    public ResultsDialog(JFrame owner) {
        super(owner, "Resultados Guardados", true);
        setSize(500, 300);
        setLayout(new BorderLayout(5, 5));

        String[] columnNames = {"Algoritmo", "Celdas Camino", "Tiempo (ns)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        limpiarButton = new JButton("Limpiar Resultados");
        graficarButton = new JButton("Graficar Resultados");
        buttonPanel.add(limpiarButton);
        buttonPanel.add(graficarButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(owner);
    }

    /**
     * Rellena la tabla con una lista de resultados de algoritmos.
     * @param results La lista de objetos AlgorithmResult a mostrar.
     */
    public void populateTable(List<AlgorithmResult> results) {
        tableModel.setRowCount(0);

        for (AlgorithmResult result : results) {
            Object[] row = {
                result.getAlgorithmName(),
                result.getPathLength(),
                result.getExecutionTime()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Asigna un ActionListener a los botones del diálogo.
     * @param listener El controlador que manejará los eventos.
     */
    public void addController(ActionListener listener) {
        limpiarButton.addActionListener(listener);
        graficarButton.addActionListener(listener);
    }
}
