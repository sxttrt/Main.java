package view;


import model.User;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class AIComponent extends JFrame {
    public BeginComponent beginFrame;
    public boolean isLogin;
    public User user;
    public ArrayList<User> users;

    private final int WIDTH;
    private final int HEIGHT;

    public AIComponent(){
        setTitle("Jungle");
        this.WIDTH = 400;
        this.HEIGHT = 500;
        users = new ArrayList<>();

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        Image image = new ImageIcon("resource/background/bg.png").getImage();
        image = image.getScaledInstance(400, 500,Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        JLabel bg = new JLabel(icon);
        bg.setSize(400, 500);
        bg.setLocation(0, 0);
        add(bg);

        try {
            File file = new File("user\\users");

            String temp;
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"GBK");
            ArrayList<String> readList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(read);

            while(true){
                if (!((temp = reader.readLine()) != null && !"".equals(temp))) break;
                readList.add(temp);
                System.out.println(temp);
            }

            for (int i = 0; i < readList.size(); i++){
                String[] strArr = readList.get(i).split(" ");
                users.add(new User(strArr[0], strArr[1], Integer.parseInt(strArr[2])));
            }

        } catch (Exception e){}
    }






}
