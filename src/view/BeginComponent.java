package view;

import controller.GameController;
import model.Chessboard;
import model.Timer;


import javax.swing.*;
import java.awt.*;

public class BeginComponent extends JFrame {
    ChessGameFrame gameFrame;
    ChessGameFrame information;
    AIComponent aiFrame;


    private final int WIDTH;
    private final int HEIGHT;

    public BeginComponent() {
        setTitle("斗兽棋pro");
        this.WIDTH = 500;
        this.HEIGHT = 900;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        ChessGameFrame gameFrame = new ChessGameFrame(1100, 810);
        GameController controller = new GameController(gameFrame.getChessboardComponent(), new Chessboard());
        this.gameFrame = gameFrame;
        gameFrame.beginFrame= this;


        addBeginButton();
        addAIButton();

        Image image = new ImageIcon("src\\resource/background/bg.png").getImage();
        image = image.getScaledInstance(500, 900, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        JLabel bg = new JLabel(icon);
        bg.setSize(500, 900);
        bg.setLocation(0, 0);
        add(bg);
    }

    private void addBeginButton() {
        JButton button = new JButton("开始游戏");
        button.addActionListener((e) -> {
            this.setVisible(false);
            this.setVisible(false);
            model.Timer.time = 45;
            if (GameController.timer == null){
                GameController.timer = new Timer(gameFrame.getChessboardComponent().gameController);
                GameController.timer.start();
            }

            gameFrame.statusLabel.setLocation(770, 81);
            gameFrame.repaint();
            gameFrame.getChessboardComponent().gameController.AIPlaying =false;
            gameFrame.getChessboardComponent().gameController.reset();
            gameFrame.setVisible(true);
        });
        button.setLocation(150, 250);
        button.setSize(200, 100);
        button.setFont(new Font("Simsun", Font.BOLD, 20));
        add(button);
    }


    private void addAIButton() {
        JButton button = new JButton("人机");
        button.addActionListener((e) -> {
            this.setVisible(false);
            gameFrame.getChessboardComponent().gameController.reset();
            gameFrame.getChessboardComponent().gameController.AIPlaying = true;
            gameFrame.setVisible(true);
        });
        button.setLocation(150, 520);
        button.setSize(200, 100);
        button.setFont(new Font("Simsun", Font.BOLD, 20));
        add(button);
    }

}