import controller.GameController;
import model.Chessboard;
import view.Begin;
import view.ChessGameFrame;

import javax.swing.*;
import model.BackgroundMusic;

public class Main {
    public static void main(String[] args) {
//        Begin begin = new Begin();
//        begin.init();
        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
            GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
            mainFrame.setVisible(true);
           new BackgroundMusic().playMusic("resource\\bgm\\bgm3.wav");
        });
    }
}
