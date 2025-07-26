package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * Representa la ventana principal (JFrame) de la aplicación "Maze Creator".
 *
 * Esta clase es la vista principal en el patrón MVC. Es responsable de construir
 * y organizar todos los componentes visuales con los que el usuario interactúa,
 * como menús, botones y paneles. Delega toda la lógica de manejo de eventos
 * a una clase controladora externa para mantener una separación clara de
 * responsabilidades.
 */
public class MazeFrame extends JFrame {

    // Menú
    private JMenuItem nuevoLaberintoItem;
    private JMenuItem verResultadosItem;
    private JMenuItem acercaDeItem;

    // Panel de Diseño (Superior)
    private JButton setStartButton;
    private JButton setEndButton;
    private JButton toggleWallButton;

    // Panel del Laberinto (Central)
    private MazePanel mazePanel;

    // Panel de Control (Inferior)
    private JComboBox<String> algoritmoComboBox;
    private JButton resolverButton;
    private JButton pasoAPasoButton;
    private JButton limpiarButton;

    /**
     * Construye y configura la ventana principal de la aplicación.
     *
     * @param rows El número de filas con el que se inicializará la cuadrícula del laberinto.
     * @param cols El número de columnas con el que se inicializará la cuadrícula del laberinto.
     */
    public MazeFrame(int rows, int cols) {
        setTitle("Maze Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5)); // Layout principal con espaciado de 5px.

        // 1. Construir y añadir la barra de menú.
        setupMenuBar();

        // 2. Construir y añadir el panel de botones de diseño.
        setupDesignPanel();

        // 3. Construir y añadir el panel central del laberinto.
        this.mazePanel = new MazePanel(rows, cols);
        add(this.mazePanel, BorderLayout.CENTER);

        // 4. Construir y añadir el panel de botones de control.
        setupControlPanel();

        // Finalizar el ensamblaje de la ventana.
        pack(); // Ajusta el tamaño de la ventana para que quepan todos los componentes.
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        setResizable(false); // Opcional: Evita que el usuario cambie el tamaño.
        setVisible(true); // Hace visible la ventana.
    }

    /**
     * Método privado auxiliar que crea y configura la barra de menú (JMenuBar).
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu archivoMenu = new JMenu("Archivo");
        nuevoLaberintoItem = new JMenuItem("Nuevo laberinto");
        verResultadosItem = new JMenuItem("Ver resultados");
        archivoMenu.add(nuevoLaberintoItem);
        archivoMenu.add(verResultadosItem);

        JMenu ayudaMenu = new JMenu("Ayuda");
        acercaDeItem = new JMenuItem("Acerca de...");
        ayudaMenu.add(acercaDeItem);

        menuBar.add(archivoMenu);
        menuBar.add(ayudaMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Método privado auxiliar que crea y configura el panel superior (NORTH)
     * con los botones para diseñar el laberinto.
     */
    private void setupDesignPanel() {
        JPanel designPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        setStartButton = new JButton("Set Start");
        setEndButton = new JButton("Set End");
        toggleWallButton = new JButton("Toggle Wall");

        designPanel.add(setStartButton);
        designPanel.add(setEndButton);
        designPanel.add(toggleWallButton);
        add(designPanel, BorderLayout.NORTH);
    }

    /**
     * Método privado auxiliar que crea y configura el panel inferior (SOUTH)
     * con los controles para resolver el laberinto.
     */
    private void setupControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.add(new JLabel("Algoritmo:"));

        String[] algoritmos = {"Recursivo", "Recursivo Completo", "Recursivo Completo BT", "BFS", "DFS"};
        algoritmoComboBox = new JComboBox<>(algoritmos);
        controlPanel.add(algoritmoComboBox);

        resolverButton = new JButton("Resolver");
        pasoAPasoButton = new JButton("Paso a paso");
        limpiarButton = new JButton("Limpiar");

        controlPanel.add(resolverButton);
        controlPanel.add(pasoAPasoButton);
        controlPanel.add(limpiarButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    /**
     * Asigna un único ActionListener a todos los componentes interactivos de la ventana.
     * Este método es fundamental para conectar la Vista con el Controlador.
     *
     * @param listener El objeto controlador que manejará todos los eventos de acción.
     */
    public void addController(ActionListener listener) {
        // Menú
        nuevoLaberintoItem.addActionListener(listener);
        verResultadosItem.addActionListener(listener);
        acercaDeItem.addActionListener(listener);

        // Botones de diseño
        setStartButton.addActionListener(listener);
        setEndButton.addActionListener(listener);
        toggleWallButton.addActionListener(listener);

        // Botones de control
        resolverButton.addActionListener(listener);
        pasoAPasoButton.addActionListener(listener);
        limpiarButton.addActionListener(listener);
    }

    /**
     * Devuelve la instancia del panel del laberinto.
     * El controlador usará este método para decirle al panel cuándo y qué dibujar.
     *
     * @return El componente MazePanel.
     */
    public MazePanel getMazePanel() {
        return mazePanel;
    }

    /**
     * Devuelve el algoritmo seleccionado actualmente en el JComboBox.
     *
     * @return Un String con el nombre del algoritmo elegido.
     */
    public String getSelectedAlgorithm() {
        return (String) algoritmoComboBox.getSelectedItem();
    }
}
