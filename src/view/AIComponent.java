package view;


import javax.swing.*;
import java.awt.*;

public class AIComponent extends JFrame {

    private final int WIDTH;
    private final int HEIGHT;

    public AIComponent() {
        setTitle("Jungle");
        this.WIDTH = 400;
        this.HEIGHT = 500;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);


        Image image = new ImageIcon("resource/background/bg.png").getImage();
        image = image.getScaledInstance(400, 500, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        JLabel bg = new JLabel(icon);
        bg.setSize(400, 500);
        bg.setLocation(0, 0);
        add(bg);
    }






}
