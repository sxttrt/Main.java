package controller;


import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessboardComponent;
import view.animal.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/
public class GameController implements GameListener {

    public Chessboard chessboard;
    public ChessboardComponent view;
    public ArrayList<ChessboardPoint> canStepPoints;
    public PlayerColor currentPlayer;
    public PlayerColor winner;
    public boolean skip;
    public boolean isPlayback;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;

    private Thread thread;
    public static Timer timer;
    public GameController(){}

    public GameController(ChessboardComponent view, Chessboard chessboard) {
        this.view = view;
        this.chessboard = chessboard;
        this.currentPlayer = PlayerColor.BLUE;
        this.winner = null;
        isPlayback = false;
        skip = false;

        view.registerController(this);
        initialize();
        view.initiateChessComponent(chessboard);
        view.repaint();
    }

    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {

            }
        }
    }

    // after a valid move swap the player
    public void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
    }

    private boolean checkWin() {
        if (chessboard.grid[0][3].getPiece() != null || chessboard.deadRedChess.size() == 8) {
            this.winner = PlayerColor.BLUE;
        }
        if (chessboard.grid[8][3].getPiece() != null || chessboard.deadBlueChess.size() == 8) {
            this.winner = PlayerColor.RED;
        }
        return false;
    }

    public void doWin(){
        JOptionPane.showMessageDialog(view, "Congratulations!" + (winner == PlayerColor.BLUE ? "BLUE" : "RED") + " Win the game");
    }


    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && chessboard.isValidMove(selectedPoint, point)) {
            chessboard.moveChessPiece(selectedPoint, point);
            clearCanStep();
            canStepPoints = null;
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            swapColor();
            view.repaint();
            component.revalidate();
            checkWin();
            if (winner != null) {
                doWin();
                reset();
            }
        }
    }

    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, Animal component) {
        if (selectedPoint == null) {
            if (chessboard.getChessPieceOwner(point).equals(currentPlayer)) {
                canStepPoints = getCanStepPoints(point);
                selectedPoint = point;
                component.setSelected(true);
                component.revalidate();
                component.repaint();
                view.revalidate();
                view.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            canStepPoints = null;
            clearCanStep();
            component.setSelected(false);
            component.revalidate();
            component.repaint();
            view.revalidate();
            view.repaint();
        } else if (chessboard.isValidCapture(selectedPoint, point)) {
            chessboard.capture(selectedPoint, point);
            view.removeChessComponentAtGrid(point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            clearCanStep();
            swapColor();
            view.revalidate();
            view.repaint();
            component.revalidate();
            checkWin();
            if (winner != null) {
                doWin();
                reset();
            }
        }

    }

    public void clearCanStep() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                view.gridComponents[i][j].canStep = false;
            }
        }
    }

    public ArrayList<ChessboardPoint> getCanStepPoints(ChessboardPoint src) {
        ArrayList<ChessboardPoint> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint dest = new ChessboardPoint(i, j);
                if (chessboard.isValidMove(src, dest)) {
                    view.gridComponents[i][j].canStep = true;
                    list.add(dest);
                }
                if (chessboard.isValidCapture(src, dest)) {
                    view.gridComponents[i][j].canStep = true;
                    list.add(dest);
                }
            }
        }
        return list;
    }

    public void reset() {
        canStepPoints = null;
        chessboard.initGrid();
        chessboard.initPieces();
        view.removeChessComponent();
        view.initiateChessComponent(chessboard);
        currentPlayer = PlayerColor.BLUE;
        selectedPoint = null;
        clearCanStep();
        chessboard.steps = new ArrayList<>();
        chessboard.deadBlueChess = new ArrayList<>();
        chessboard.deadRedChess = new ArrayList<>();
        view.revalidate();
        view.repaint();
        winner = null;

        chessboard.deadRedChess = new ArrayList<>();
        chessboard.deadBlueChess = new ArrayList<>();
    }

    private static String animalShortWriting(ChessPiece chess) {
        if (chess == null) return "O";
        else if (chess.getName().equals("Elephant")) return "E";
        else if (chess.getName().equals("Lion")) return "L";
        else if (chess.getName().equals("Tiger")) return "T";
        else if (chess.getName().equals("Leopard")) return "l";
        else if (chess.getName().equals("Wolf")) return "w";
        else if (chess.getName().equals("Dog")) return "d";
        else if (chess.getName().equals("Cat")) return "c";
        else if (chess.getName().equals("Rat")) return "r";
        else return "";
    }

    private static boolean checkName(String[] chessPlace) {
        int numberE=0,numberL=0,numberT=0,numberl=0,numberw=0,numberd=0,numberc=0,numberr=0;
        for (int i = 0; i < chessPlace.length; i++) {
            if(chessPlace[i].equals("E")){numberE++;}
            if(chessPlace[i].equals("L")){numberL++;}
            if(chessPlace[i].equals("T")){numberT++;}
            if(chessPlace[i].equals("l")){numberl++;}
            if(chessPlace[i].equals("w")){numberw++;}
            if(chessPlace[i].equals("d")){numberd++;}
            if(chessPlace[i].equals("c")){numberc++;}
            if(chessPlace[i].equals("r")){numberr++;}
            if (!chessPlace[i].equals("E")
                    && !chessPlace[i].equals("L")
                    && !chessPlace[i].equals("T")
                    && !chessPlace[i].equals("l")
                    && !chessPlace[i].equals("w")
                    && !chessPlace[i].equals("d")
                    && !chessPlace[i].equals("c")
                    && !chessPlace[i].equals("r")
                    && !chessPlace[i].equals("O")) {
                return false;
            }
        }
        if (numberE>2 || numberT>2 || numberL>2 || numberl>2 || numberw>2 || numberd>2 || numberc>2 || numberr>2){
            return false;
        }
        return true;
    }

    public void regret() {
        chessboard.steps.remove(chessboard.steps.size() - 1);
        ArrayList<Step> tempList = chessboard.steps;
        reset();
        for (int i = 0; i < tempList.size(); i++) {
            Step step = tempList.get(i);
            ChessboardPoint src = step.src;
            ChessboardPoint dest = step.dest;
            boolean isCapture = step.captured != null;
            if (! isCapture) {
                chessboard.moveChessPiece(src, dest);
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                selectedPoint = null;
                swapColor();
                view.repaint();
            } else {
                chessboard.capture(src, dest);
                view.removeChessComponentAtGrid(dest);
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                swapColor();
                view.repaint();
                view.revalidate();
            }
        }
    }

    public void playBack() {
        isPlayback = true;
        thread = new Thread() {
            @Override
            public void run() {
                ArrayList<Step> tempList = chessboard.steps;
                reset();
                for (int i = 0; i < tempList.size(); i++) {
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Step step = tempList.get(i);
                    ChessboardPoint src = step.src;
                    ChessboardPoint dest = step.dest;
                    if (step.captured != null) {
                        chessboard.capture(src, dest);
                        view.removeChessComponentAtGrid(dest);
                        view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                        swapColor();
                        view.revalidate();
                        view.repaint();
                    } else {
                        chessboard.moveChessPiece(src, dest);
                        view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                        selectedPoint = null;
                        swapColor();
                        view.revalidate();
                        view.repaint();
                    }
                }
            }
        };
        thread.start();
        isPlayback = false;
    }

    public void saveGame(String filename) {
        String location = "save\\" + filename + ".txt";
        File file = new File(location);

        try {
            if (file.exists()) {
                int n = JOptionPane.showConfirmDialog(view, "同名存档已存在，是否覆盖?", "", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    file.delete();
                }
            }

            FileWriter fileWriter = new FileWriter(location, true);
            fileWriter.write(chessboard.steps.size() + ""+"\n");

            for(int i=0;i<chessboard.steps.size();i++){
                fileWriter.write(chessboard.steps.get(i).toString()+"\n");
            }
            fileWriter.write(currentPlayer == PlayerColor.BLUE ? "b" : "r"+"\n");

            for(int i=0;i<9;i++){
                for(int j=0;j<7;j++){
                    ChessPiece chess = chessboard.grid[i][j].getPiece();
                    fileWriter.write((animalShortWriting(chess))+" ");
                }
                fileWriter.write("\n");
            }
            fileWriter.close();
            System.out.println("Correct Save");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public boolean loadGame(){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory( new File ("save"));
        chooser.showOpenDialog(view);
        File file = chooser.getSelectedFile();

        if(!file.getName().endsWith(".txt")){
            JOptionPane.showMessageDialog(null, "文件后缀错误\n错误编码： 101",
                    "载入存档出现错误", JOptionPane.ERROR_MESSAGE);
            reset();
            return false;
        }

        try {
            String temp;
            ArrayList<String> readList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));

            while (true) {
                temp = reader.readLine();
                if (temp != null && !"".equals(temp)) {
                    readList.add(temp);
                } else {
                    break;
                }
            }

            int num = Integer.parseInt(readList.remove(0));

            for (int i = 0; i <=num; i++) {
                String str = readList.get(i);
                if (i % 2 == 0 && str.charAt(0) != 'b') {
                    JOptionPane.showMessageDialog(null, "行棋方错误\n错误编码：104",
                            "载入存档出现错误", JOptionPane.ERROR_MESSAGE);
                    reset();
                    return false;
                }
                if (i % 2 == 1 && str.charAt(0) != 'r'){
                    JOptionPane.showMessageDialog(null, "行棋方错误\n错误编码：104",
                            "载入存档出现错误", JOptionPane.ERROR_MESSAGE);
                    reset();
                    return false;
                }
            }

            try {
                for (int i = num+1 ; i < num + 10; i++) {
                    String[] chess = readList.get(i).split(" ");
                    if (chess.length != 7) {
                        JOptionPane.showMessageDialog(null, "棋盘错误，并非9*71\n错误编码：102",
                                "载入存档出现错误", JOptionPane.ERROR_MESSAGE);
                        reset();
                        return false;
                    }
                    if (!checkName(chess)) {
                        JOptionPane.showMessageDialog(null, "棋子错误\n错误编码：103",
                                "载入存档出现错误", JOptionPane.ERROR_MESSAGE);
                        reset();
                        return false;
                    }
                }
            }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "棋盘错误，并非9*72\n错误编码：102",
                            "载入存档出现错误", JOptionPane.ERROR_MESSAGE);
                    reset();
                    return false;
                }

                reset();
                for (int i = 0; i < num; i++) {
                    String[] step = readList.get(i).split(" ");
                    ChessboardPoint src = new ChessboardPoint(Integer.parseInt(step[1].charAt(1) + ""), Integer.parseInt(step[1].charAt(3) + ""));
                    ChessboardPoint dest = new ChessboardPoint(Integer.parseInt(step[2].charAt(1) + ""), Integer.parseInt(step[2].charAt(3) + ""));

                    boolean isCapture = !step[3].equals("null");
                    if (!isCapture) {
                        if (!chessboard.isValidMove(src, dest)) {
                            JOptionPane.showMessageDialog(null, "行棋步骤错误\n错误编码：105",
                                    "载入存档出现错误", JOptionPane.ERROR_MESSAGE);
                            reset();
                            return false;
                        }
                        chessboard.moveChessPiece(src, dest);
                        view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                        selectedPoint = null;
                        swapColor();
                        view.revalidate();
                        view.repaint();
                    } else {
                        if (!chessboard.isValidCapture(src, dest)) {
                            JOptionPane.showMessageDialog(null, "行棋步骤错误\n错误编码：105",
                                    "载入存档出现错误", JOptionPane.ERROR_MESSAGE);
                            reset();
                            return false;
                        }
                        chessboard.capture(src, dest);
                        view.removeChessComponentAtGrid(dest);
                        view.setChessComponentAtGrid(src, view.removeChessComponentAtGrid(dest));
                        selectedPoint = null;
                        swapColor();
                        view.revalidate();
                        view.repaint();
                    }
                }
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, "无此存档\n错误编码： 106",
                        "载入存档出现错误", JOptionPane.ERROR_MESSAGE);
                reset();
                return false;
            }
        return true;
        }
    }

