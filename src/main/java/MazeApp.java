import controllers.MazeController;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada principal para la aplicación Maze Creator.
 *
 * Su única responsabilidad es obtener las dimensiones iniciales del laberinto
 * por parte del usuario y luego iniciar el MazeController, que se encargará
 * del resto de la lógica de la aplicación.
 */
public class MazeApp {

    public static void main(String[] args) {
        // Ejecuta la lógica de la UI en el hilo de despacho de eventos de Swing (Event Dispatch Thread)
        // Esta es la forma correcta y segura de iniciar una aplicación Swing.
        SwingUtilities.invokeLater(() -> {
            int rows = askForDimension("filas");
            int cols = askForDimension("columnas");

            // Inicia el controlador con las dimensiones obtenidas.
            // El controlador se encargará de crear la vista (MazeFrame).
            new MazeController(rows, cols);
        });
    }

    /**
     * Muestra un diálogo para pedir al usuario una dimensión (filas o columnas).
     * Valida que la entrada sea un número entero mayor que 4.
     *
     * @param dimensionName El nombre de la dimensión a pedir (ej. "filas").
     * @return El número de la dimensión ingresada por el usuario.
     */
    private static int askForDimension(String dimensionName) {
        String input;
        int value = 0;
        boolean isValid = false;

        while (!isValid) {
            input = JOptionPane.showInputDialog(
                null,
                "Ingrese número de " + dimensionName + " (mayor a 4):",
                "Entrada",
                JOptionPane.QUESTION_MESSAGE
            );

            // Si el usuario presiona "Cancelar" o cierra el diálogo, salimos de la aplicación.
            if (input == null) {
                System.exit(0);
            }

            try {
                value = Integer.parseInt(input);
                if (value > 4) {
                    isValid = true;
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Por favor, ingrese un número mayor que 4.",
                        "Error de Entrada",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Entrada no válida. Por favor, ingrese un número entero.",
                    "Error de Entrada",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return value;
    }
}
