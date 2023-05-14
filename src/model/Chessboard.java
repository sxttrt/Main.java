package model;
import java.util.ArrayList;

/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard {
    public Cell[][] grid;
    public ArrayList<Step> steps;
    public ArrayList<ChessPiece> deadBlueChess;
    public ArrayList<ChessPiece> deadRedChess;

    public Chessboard() {
        this.grid =
                new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//19X19
        initGrid();
        initPieces();
        steps = new ArrayList<>();
        deadBlueChess = new ArrayList<>();
        deadRedChess = new ArrayList<>();
    }

    private void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    private void initPieces() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j].removePiece();
            }
        }
        grid[6][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Elephant",8));
        grid[2][6].setPiece(new ChessPiece(PlayerColor.RED, "Elephant",8));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Lion", 7));
        grid[0][0].setPiece(new ChessPiece(PlayerColor.RED, "Lion", 7));
        grid[8][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Tiger", 6));
        grid[0][6].setPiece(new ChessPiece(PlayerColor.RED, "Tiger", 6));
        grid[6][4].setPiece(new ChessPiece(PlayerColor.BLUE, "Leopard", 5));
        grid[2][2].setPiece(new ChessPiece(PlayerColor.RED, "Leopard", 5));
        grid[6][2].setPiece(new ChessPiece(PlayerColor.BLUE, "Wolf", 4));
        grid[2][4].setPiece(new ChessPiece(PlayerColor.RED, "Wolf", 4));
        grid[7][5].setPiece(new ChessPiece(PlayerColor.BLUE, "Dog", 3));
        grid[1][1].setPiece(new ChessPiece(PlayerColor.RED, "Dog", 3));
        grid[7][1].setPiece(new ChessPiece(PlayerColor.BLUE, "Cat", 2));
        grid[1][5].setPiece(new ChessPiece(PlayerColor.RED, "Cat", 2));
        grid[6][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Rat", 1));
        grid[2][0].setPiece(new ChessPiece(PlayerColor.RED, "Rat", 1));
}

    private ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    private Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    private int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }

    private ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }

    private void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
            if (isEnemyTrap(point, chessPiece.getOwner())){
                chessPiece.rank = 0;
            } else {
                if (chessPiece.getName().equals("Elephant")){
                    chessPiece.rank = 8;
                }
                if (chessPiece.getName().equals("Lion")){
                    chessPiece.rank = 7;
                }
                if (chessPiece.getName().equals("Tiger")){
                    chessPiece.rank = 6;
                }
                if (chessPiece.getName().equals("Leopard")){
                    chessPiece.rank = 5;
                }
                if (chessPiece.getName().equals("Wolf")){
                    chessPiece.rank = 4;
                }
                if (chessPiece.getName().equals("Dog")){
                    chessPiece.rank = 3;
                }
                if (chessPiece.getName().equals("Cat")){
                    chessPiece.rank = 2;
                }
                if (chessPiece.getName().equals("Rat")){
                    chessPiece.rank = 1;
                }
            }
    }

    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        ChessPiece newChess = removeChessPiece(src);
        setChessPiece(dest, newChess);
        steps.add(new Step(src,dest,newChess.getOwner()));
    }

    public void capture(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        ChessPiece eater = getChessPieceAt(src);
        ChessPiece enemy = getChessPieceAt(dest);
        setChessPiece(dest,eater);
        steps.add(new Step(src,dest,eater.getOwner(),enemy));
    }

    public Cell[][] getGrid() {
        return grid;
    }
    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        return getGridAt(point).getPiece().getOwner();
    }

    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest) {
        ChessPiece myChess = getChessPieceAt(src);
        ChessPiece enemyChess = getChessPieceAt(dest);
        if (myChess == null || enemyChess!= null) {
            return false;
        }
        boolean b =calculateDistance(src,dest) == 1 ;
        if (myChess.getName().equals("Elephant")){
            return  b && !isRiver(dest);
        }
        if (myChess.getName().equals("Lion")){
            return (b && !isRiver(dest)) || canJumpRiver(src, dest);
        }
        if (myChess.getName().equals("Tiger")){
            return (b && !isRiver(dest)) || canJumpRiver(src, dest);
        }
        if (myChess.getName().equals("Leopard")){
            return b && !isRiver(dest);
        }
        if (myChess.getName().equals("Wolf")){
            return b && !isRiver(dest);
        }
        if (myChess.getName().equals("Dog")){
            return b && !isRiver(dest);
        }
        if (myChess.getName().equals("Cat")){
            return b && !isRiver(dest);
        }
        if (myChess.getName().equals("Rat")){
            return b;
        }
        return false;
    }


    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
        ChessPiece eater = getChessPieceAt(src);
        ChessPiece enemy = getChessPieceAt(dest);
        boolean b =calculateDistance(src,dest) == 1 ;
        if (eater==null || enemy==null) {
            return false;
        }
        if (eater.getOwner()==enemy.getOwner()){
            return false;
        }
        if (eater.getName().equals("Elephant")){
            return b && !isRiver(dest) && enemy.getRank() !=1;
        }
        if (eater.getName().equals("Lion")){
            return ((b && !isRiver(dest)) || canJumpRiver(src, dest)) && enemy.getRank()<= 7;
        }
        if (eater.getName().equals("Tiger")){
            return ((b && !isRiver(dest)) || canJumpRiver(src, dest)) && enemy.getRank() <= 6;
        }
        if (eater.getName().equals("Leopard")){
            return b && !isRiver(dest) && enemy.getRank()<= 5;
        }
        if (eater.getName().equals("Wolf")){
            return b && !isRiver(dest) &&enemy.getRank()<= 4;
        }
        if (eater.getName().equals("Dog")){
            return b && !isRiver(dest) && enemy.getRank() <= 3;
        }
        if (eater.getName().equals("Cat")){
            return b && !isRiver(dest) && enemy.getRank() <= 2;
        }
        if (eater.getName().equals("Rat")){
            return b && (enemy.getRank() <= 1 || enemy.getRank() == 8) && !(isRiver(src) && !isRiver(dest));
        }
        return false;
    }
    private boolean isRiver(ChessboardPoint point){
        return point.getRow() >= 3
                && point.getRow() <= 5
                && (point.getCol() == 1 || point.getCol() == 2 || point.getCol() == 4 || point.getCol() == 5);

    }
    private boolean isDens(ChessboardPoint point){
            return (point.getRow() == 8 && point.getCol() == 3)
                    || (point.getRow() == 0 && point.getCol() == 3);
    }

    private boolean isEnemyTrap(ChessboardPoint point, PlayerColor color){
        if (color == PlayerColor.BLUE){
            return (point.getRow() == 1 && point.getCol() == 3)
                    || (point.getRow() == 0 && point.getCol() == 2)
                    || (point.getRow() == 0 && point.getCol() == 4);
        } else {
            return (point.getRow() == 7 && point.getCol() == 3)
                    || (point.getRow() == 8 && point.getCol() == 2)
                    || (point.getRow() == 8 && point.getCol() == 4);
        }
    }
    private boolean canJumpRiver (ChessboardPoint src ,ChessboardPoint dest ){
        if ((src.getRow() == 3 && src.getCol() == 0 && dest.getRow() == 3 && dest.getCol() == 3)
                || (src.getRow() == 3 && src.getCol() == 3 && dest.getRow() == 3 && dest.getCol() == 0)){
            return getChessPieceAt(new ChessboardPoint(3, 1)) == null
                    && getChessPieceAt(new ChessboardPoint(3, 2)) == null;
        }
        if ((src.getRow() == 4 && src.getCol() == 0 && dest.getRow() == 4 && dest.getCol() == 3)
                || (src.getRow() == 4 && src.getCol() == 3 && dest.getRow() == 4 && dest.getCol() == 0)){
            return getChessPieceAt(new ChessboardPoint(4, 1)) == null
                    && getChessPieceAt(new ChessboardPoint(4, 2)) == null;
        }
        if ((src.getRow() == 5 && src.getCol() == 0 && dest.getRow() == 5 && dest.getCol() == 3)
                || (src.getRow() == 5 && src.getCol() == 3 && dest.getRow() == 5 && dest.getCol() == 0)){
            return getChessPieceAt(new ChessboardPoint(5, 1)) == null
                    && getChessPieceAt(new ChessboardPoint(5, 2)) == null;
        }
        if ((src.getRow() == 3 && src.getCol() == 3 && dest.getRow() == 3 && dest.getCol() == 6)
                || (src.getRow() == 3 && src.getCol() == 6 && dest.getRow() == 3 && dest.getCol() == 3)){
            return getChessPieceAt(new ChessboardPoint(3, 4)) == null
                    && getChessPieceAt(new ChessboardPoint(3, 5)) == null;
        }
        if ((src.getRow() == 4 && src.getCol() == 3 && dest.getRow() == 4 && dest.getCol() == 6)
                || (src.getRow() == 4 && src.getCol() == 6 && dest.getRow() == 4 && dest.getCol() == 3)){
            return getChessPieceAt(new ChessboardPoint(4, 4)) == null
                    && getChessPieceAt(new ChessboardPoint(4, 5)) == null;
        }
        if ((src.getRow() == 5 && src.getCol() == 3 && dest.getRow() == 5 && dest.getCol() == 6)
                || (src.getRow() == 5 && src.getCol() == 6 && dest.getRow() == 5 && dest.getCol() == 3)){
            return getChessPieceAt(new ChessboardPoint(5, 4)) == null
                    && getChessPieceAt(new ChessboardPoint(5, 5)) == null;
        }

        if ((src.getRow() == 2 && src.getCol() == 1 && dest.getRow() == 6 && dest.getCol() == 1)
                || (src.getRow() == 6 && src.getCol() == 1 && dest.getRow() == 2 && dest.getCol() == 1)){
            return getChessPieceAt(new ChessboardPoint(3, 1)) == null
                    && getChessPieceAt(new ChessboardPoint(4, 1)) == null
                    && getChessPieceAt(new ChessboardPoint(5, 1)) == null;
        }
        if ((src.getRow() == 2 && src.getCol() == 2 && dest.getRow() == 6 && dest.getCol() == 2)
                || (src.getRow() == 6 && src.getCol() == 2 && dest.getRow() == 2 && dest.getCol() == 2)){
            return getChessPieceAt(new ChessboardPoint(3, 2)) == null
                    && getChessPieceAt(new ChessboardPoint(4, 2)) == null
                    && getChessPieceAt(new ChessboardPoint(5, 2)) == null;
        }
        if ((src.getRow() == 2 && src.getCol() == 4 && dest.getRow() == 6 && dest.getCol() == 4)
                || (src.getRow() == 6 && src.getCol() == 4 && dest.getRow() == 2 && dest.getCol() == 4)){
            return getChessPieceAt(new ChessboardPoint(3, 4)) == null
                    && getChessPieceAt(new ChessboardPoint(4, 4)) == null
                    && getChessPieceAt(new ChessboardPoint(5, 4)) == null;
        }
        if ((src.getRow() == 2 && src.getCol() == 5 && dest.getRow() == 6 && dest.getCol() == 5)
                || (src.getRow() == 6 && src.getCol() == 5 && dest.getRow() == 2 && dest.getCol() == 5)){
            return getChessPieceAt(new ChessboardPoint(3, 5)) == null
                    && getChessPieceAt(new ChessboardPoint(4, 5)) == null
                    && getChessPieceAt(new ChessboardPoint(5, 5)) == null;
        }
        return false;
    }
}
