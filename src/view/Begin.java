package view;
import javax.swing.*;
import  java.awt.*;

public class Begin {
    public void init(){
        JFrame frame = new JFrame("斗兽棋");
        frame.setVisible(true);
        frame.setBounds(200,200,400,600);
        frame.setBackground(Color.RED);

        //close
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


}
