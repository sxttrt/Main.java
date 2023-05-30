package view;


import javax.swing.*;
import java.awt.*;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    public BeginComponent beginFrame;
    private final int WIDTH;
    private final int HEIGTH;

    private final int ONE_CHESS_SIZE;

    private ChessboardComponent chessboardComponent;
    JLabel statusLabel;
    JLabel timeLabel;
    public boolean isSpring;
    JLabel background;
    public final JLabel springBG;
    public final JLabel autumnBG;
    public ChessGameFrame(int width, int height) {
        setTitle("斗兽棋"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addLabel();
        addTimeLabel();
        addChessboard();
        addResetButton();
        addRegretButton();
        addThemeButton();
        addPlaybackButton();
        addSaveButton();
        addLoadButton();
        addBackButton();

        Image image = new ImageIcon("resource/background/spring.png").getImage();
        image = image.getScaledInstance(1100, 810,Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        springBG = new JLabel(icon);
        springBG.setSize(1100, 810);
        springBG.setLocation(0, 0);

        image = new ImageIcon("resource/background/autumn.png").getImage();
        image = image.getScaledInstance(1100, 810,Image.SCALE_DEFAULT);
        icon = new ImageIcon(image);
        autumnBG = new JLabel(icon);
        autumnBG.setSize(1100, 810);
        autumnBG.setLocation(0, 0);

        background = springBG;
        add(background);
    }

    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE,statusLabel,timeLabel);
        chessboardComponent.setLocation(HEIGTH / 5, HEIGTH / 10);
        add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        statusLabel = new JLabel("回合 1: 蓝方");
        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Simsun", Font.BOLD, 20));
        add(statusLabel);
    }
    private void addTimeLabel() {
        timeLabel = new JLabel("Time: 45");
        timeLabel.setLocation(HEIGHT +900, HEIGHT/ 10+80);
        timeLabel.setSize(200, 60);
        timeLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(timeLabel);
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */


    private void addResetButton() {
        JButton button = new JButton("重开");
        button.addActionListener((e) -> {
            chessboardComponent.gameController.reset();
        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 100);
        button.setSize(200, 60);
        button.setFont(new Font("Simsun", Font.BOLD, 20));
        add(button);
    }

    private void addRegretButton() {
        JButton button = new JButton("悔棋");
        button.setLocation(HEIGTH, HEIGTH / 10 + 200);
        button.setSize(200, 60);
        button.setFont(new Font("Simsun", Font.BOLD, 20));
        add(button);
        button.addActionListener((e) -> {
            System.out.println("Click regret");
            chessboardComponent.gameController.regret();
        });
    }

    private void addThemeButton() {
        JButton button = new JButton("改主题");
        button.setLocation(HEIGTH, HEIGTH / 10 + 300);
        button.setSize(200, 60);
        button.setFont(new Font("Simsun", Font.BOLD, 20));
        add(button);
        button.addActionListener((e) -> {
            System.out.println("Click change theme");
            chessboardComponent.changeTheme(isSpring);
            if (isSpring){
                remove(background);
                isSpring = false;
                background = autumnBG;
                add(background);
            } else {
                remove(background);
                isSpring = true;
                background = springBG;
                add(background);
            }
            repaint();
            revalidate();
        });
    }

    private void addPlaybackButton() {
        JButton button = new JButton("回放");
        button.setLocation(HEIGTH, HEIGTH / 10 + 400);
        button.setSize(200, 60);
        button.setFont(new Font("Simsun", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click playback");
            chessboardComponent.gameController.playBack();
        });
    }

    private void addSaveButton() {
        JButton button = new JButton("保存");
        button.setLocation(HEIGTH, HEIGTH / 10 + 500);
        button.setSize(200, 60);
        button.setFont(new Font("Simsun", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click save");
            String path = JOptionPane.showInputDialog("存档名");
            while (path.equals("")){
                JOptionPane.showMessageDialog(null, "存档名不能为空");
                path = JOptionPane.showInputDialog("存档名");
            }
            chessboardComponent.gameController.saveGame(path);
            new LoadComponent();

        });
    }

    private void addLoadButton() {
        JButton button = new JButton("加载");
        button.setLocation(HEIGTH, HEIGTH / 10 + 600);
        button.setSize(200, 60);
        button.setFont(new Font("Simsun", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            boolean b = chessboardComponent.gameController.loadGame();
            if (b) new LoadComponent();
        });
    }

    private void addBackButton() {
        JButton button = new JButton("返回");
        button.setLocation(HEIGTH - 800, HEIGTH/ 10 - 60);
        button.setSize(180, 54);
        button.setFont(new Font("Simsun", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click back");
            getChessboardComponent().gameController.AIPlaying = true;
            this.setVisible(false);
            beginFrame.setVisible(true);
        });
    }







//    private void addLoadButton() {
//        JButton button = new JButton("Load");
//        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
//        button.setSize(200, 60);
//        button.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(button);
//
//        button.addActionListener(e -> {
//            System.out.println("Click load");
//            String path = JOptionPane.showInputDialog(this,"Input Path here");
//            gameController.loadGameFromFile(path);
//        });
//    }

}
