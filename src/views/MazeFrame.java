package views;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

public class MazeFrame extends JFrame {

    private MazePanel mazePanel;
    private JButton btnGenerar, btnResolverBfs, btnResolverDfs, btnVerResultados, btnLimpiar;
    private JComboBox<String> comboTamaño;

    public MazeFrame() {
        setTitle("Solucionador de Laberintos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de controles en la parte superior
        JPanel panelControles = new JPanel(new FlowLayout());
        comboTamaño = new JComboBox<>(new String[]{"10x10", "20x20", "30x30"});
        btnGenerar = new JButton("Generar Laberinto");
        btnResolverBfs = new JButton("Resolver con BFS");
        btnResolverDfs = new JButton("Resolver con DFS");
        btnVerResultados = new JButton("Ver Resultados");
        btnLimpiar = new JButton("Limpiar Camino");
        
        panelControles.add(new JLabel("Tamaño:"));
        panelControles.add(comboTamaño);
        panelControles.add(btnGenerar);
        panelControles.add(btnResolverBfs);
        panelControles.add(btnResolverDfs);
        panelControles.add(btnVerResultados);
        panelControles.add(btnLimpiar);

        mazePanel = new MazePanel();

        add(panelControles, BorderLayout.NORTH);
        add(mazePanel, BorderLayout.CENTER);
    }

    public void addController(ActionListener controller) {
        btnGenerar.addActionListener(controller);
        btnResolverBfs.addActionListener(controller);
        btnResolverDfs.addActionListener(controller);
        btnVerResultados.addActionListener(controller);
        btnLimpiar.addActionListener(controller);
    }
    
    public MazePanel getMazePanel() {
        return mazePanel;
    }
    
    public JComboBox<String> getComboTamaño() {
        return comboTamaño;
    }
}