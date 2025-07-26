package views;

import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import models.AlgorithmResult;

/**
 * Una ventana (JFrame) que muestra un gráfico de barras comparando los resultados
 * de los diferentes algoritmos de resolución de laberintos.
 */
public class ResultsChart extends JFrame {

    /**
     * Construye la ventana del gráfico.
     * @param title El título de la ventana.
     * @param results La lista de resultados de algoritmos a graficar.
     */
    public ResultsChart(String title, List<AlgorithmResult> results) {
        super(title);

        DefaultCategoryDataset dataset = createDataset(results);

        JFreeChart barChart = ChartFactory.createBarChart(
            "Comparación de Algoritmos",
            "Algoritmo",
            "Valor",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Crea y rellena el conjunto de datos para el gráfico a partir de la lista de resultados.
     * @param results La lista de objetos AlgorithmResult.
     * @return Un objeto DefaultCategoryDataset listo para ser usado por JFreeChart.
     */
    private DefaultCategoryDataset createDataset(List<AlgorithmResult> results) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series1 = "Tiempo (ns)";
        String series2 = "Celdas Camino";

        for (AlgorithmResult result : results) {
            dataset.addValue(result.getExecutionTime(), series1, result.getAlgorithmName());
            dataset.addValue(result.getPathLength(), series2, result.getAlgorithmName());
        }

        return dataset;
    }
}
