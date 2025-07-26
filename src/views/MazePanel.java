package views;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.List;

import models.Cell;
import models.CellState;

public class MazePanel extends JPanel {

    private Cell[][] maze;
    public void setMaze(Cell[][] maze) {
        this.maze = maze;
        repaint();
    }

    public void drawSolution(List<Cell> path) {
        if (maze == null || path == null || path.isEmpty()) {
            return;
        }

        for (Cell cell : path) {
            if (cell.getState() == CellState.PATH) {
                cell.setState(CellState.SOLUTION);
            }
        }
        repaint();
    }

    public void clearSolution() {
        if (maze == null) {
            return;
        }
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                if (maze[row][col].getState() == CellState.SOLUTION) {
                    maze[row][col].setState(CellState.PATH);
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (maze == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        int rows = maze.length;
        int cols = maze[0].length;

        int cellSize = Math.min(getWidth() / cols, getHeight() / rows);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                switch (maze[row][col].getState()) {
                    case PATH:     g.setColor(Color.WHITE); break;
                    case WALL:     g.setColor(Color.DARK_GRAY); break;
                    case START:    g.setColor(new Color(0, 204, 102)); break; 
                    case END:      g.setColor(Color.RED); break;
                    case SOLUTION: g.setColor(Color.CYAN); break;
                    case VISITED:  g.setColor(new Color(200, 200, 255)); break;
                    default:       g.setColor(Color.GRAY);
                }
                
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);

                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
    }
}