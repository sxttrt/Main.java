
import view.BeginComponent;

import javax.swing.*;
import model.BackgroundMusic;

public class Main {
    public static void main(String[] args) {
//        Begin begin = new Begin();
//        begin.init();
        SwingUtilities.invokeLater(() -> {
            BeginComponent beginFrame = new BeginComponent();
            beginFrame.setVisible(true);
           new BackgroundMusic().playMusic("resource\\bgm\\bgm3.wav");
        });
    }
}
