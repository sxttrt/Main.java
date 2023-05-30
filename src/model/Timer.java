package model;


import controller.GameController;

public class Timer extends Thread {
    public static int time = 45;
    public GameController gamecontroller;

    @Override
    public void run(){
        synchronized (this){
            while (true){
                PlayerColor player = gamecontroller.currentPlayer;
                boolean temp = true;
                while(time > 0) {
                    time--;
                    try {
                        Thread.sleep(300);
                        gamecontroller.timeLabel.setText("Time: " + time);
                        if (gamecontroller.currentPlayer != player){
                            gamecontroller.swapColor();
                            temp = false;
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                time = 45;

                if (temp){
                    ChessboardPoint[] points = gamecontroller.AIGetPoint();
                    ChessboardPoint src = points[0];
                    ChessboardPoint dest = points[1];

                    if (gamecontroller.chessboard.getChessPieceAt(dest) == null){
                        gamecontroller.chessboard.moveChessPiece(src, dest);
                        gamecontroller.view.setChessComponentAtGrid(dest, gamecontroller.view.removeChessComponentAtGrid(src));
                    } else {
                        gamecontroller.chessboard.capture(src, dest);
                        gamecontroller.view.removeChessComponentAtGrid(dest);
                        gamecontroller.view.setChessComponentAtGrid(dest, gamecontroller.view.removeChessComponentAtGrid(src));
                    }
                    gamecontroller.canStepPoints = null;
                    gamecontroller.clearCanStep();
                    gamecontroller.swapColor();
                    gamecontroller.view.repaint();
                    gamecontroller.view.gridComponents[dest.getRow()][dest.getCol()].revalidate();
                    gamecontroller.checkWin();
                    if (gamecontroller.winner != null){
                        gamecontroller.doWin();
                        gamecontroller.reset();
                    }
                } else {
                    gamecontroller.swapColor();
                }

            }
        }

    }

    public Timer(GameController gamecontroller){
        this.gamecontroller = gamecontroller;
    }

}