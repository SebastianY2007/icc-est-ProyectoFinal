package views;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

import java.util.List; 

import models.Cell;
import models.CellState;

public class MazePanel extends JPanel {

    private Cell[][] mazeData; 

    public void setMazeData(Cell[][] data) {
        this.mazeData = data;
        repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mazeData == null) return; 

        int rows = mazeData.length;
        int cols = mazeData[0].length;
        int cellSize = Math.min(getWidth() / cols, getHeight() / rows);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                switch (mazeData[row][col].getState()) {
                    case PATH: g.setColor(Color.WHITE); break;
                    case WALL: g.setColor(Color.BLACK); break;
                    case START: g.setColor(Color.GREEN); break;
                    case END: g.setColor(Color.RED); break;
                    case SOLUTION: g.setColor(Color.CYAN); break;
                    default: g.setColor(Color.GRAY);
                }
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize); 
            }
        }
    }

    public void drawSolution(List<Cell> path) {
        for (Cell cell : path) {
            if (cell.getState() == CellState.PATH) {
                cell.setState(CellState.SOLUTION);
            }
        }
        repaint();
    }
}