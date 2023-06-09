package view;

import model.CellType;
import model.PlayerColor;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on ChessboardComponent
 */

public class CellComponent extends JPanel {
    public PlayerColor owner;
    private Color background;
    int size;
    public boolean canStep;
    public CellType type;

    public CellComponent(Color background, Point location, int size) {
        setLayout(new GridLayout(1,1));
        setLocation(location);
        setSize(size, size);
        this.background = background;
        this.size = size;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.setColor(background);
        g.fillRect(1, 1, this.getWidth()-1, this.getHeight()-1);
        if (canStep) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(103, 83, 83, 234));

            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1,
                    this.getWidth() - 1, this.getHeight() - 1, size / 4, size / 4);
            g2d.fill(roundedRectangle);
        }
    }
}
