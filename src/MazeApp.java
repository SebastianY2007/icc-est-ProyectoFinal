import javax.swing.SwingUtilities;
import views.MazeFrame;
import controllers.MazeController;

public class MazeApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MazeFrame view = new MazeFrame();
            new MazeController(view);
            view.setVisible(true);
        });
    }
}