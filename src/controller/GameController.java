package controller;


import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessboardComponent;
import view.animal.*;

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

    public GameController(ChessboardComponent view, Chessboard chessboard) {
        this.view = view;
        this.chessboard = chessboard;
        this.currentPlayer = PlayerColor.BLUE;
        this.winner=null;
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
    private void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
    }

    private boolean checkWin() {
        if (chessboard.grid[0][3].getPiece() != null || chessboard.deadRedChess.size() == 8){
            this.winner = PlayerColor.BLUE;
        }
        if (chessboard.grid[8][3].getPiece() != null || chessboard.deadBlueChess.size() == 8){
            this.winner = PlayerColor.RED;
        }
        return false;
    }


    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && chessboard.isValidMove(selectedPoint, point)) {
            chessboard.moveChessPiece(selectedPoint, point);
            clearCanStep();
            canStepPoints=null;
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            swapColor();
            component.revalidate();
            view.repaint();
            checkWin();
            if(winner!=null){
                //TODO:重返初始界面之类的
            }
        }
    }

    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, Animal component) {
        if (selectedPoint == null) {
            if (chessboard.getChessPieceOwner(point).equals(currentPlayer)) {
                canStepPoints= getCanStepPoints(point);
                selectedPoint = point;
                component.setSelected(true);
                component.revalidate();
                component.repaint();
                view.repaint();
                view.revalidate();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            canStepPoints = null;
            clearCanStep();
            component.setSelected(false);
            component.revalidate();
            component.repaint();
        } else if (chessboard.isValidCapture(selectedPoint, point)) {
                chessboard.capture(selectedPoint, point);
                view.removeChessComponentAtGrid(point);
                view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
                selectedPoint = null;
                clearCanStep();
                swapColor();
                view.repaint();
                view.revalidate();
                component.revalidate();

                checkWin();
            if (winner != null){
               //TODO:重开
            }
        }

    }
    public void clearCanStep(){
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
                if (chessboard.isValidMove(src, dest)){
                    view.gridComponents[i][j].canStep = true;
                    list.add(dest);
                }
                if (chessboard.isValidCapture(src, dest)){
                    view.gridComponents[i][j].canStep = true;
                    list.add(dest);
                }
            }
        }
        return list;
    }
}
