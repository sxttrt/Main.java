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

    public void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public void initPieces() {
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

    private boolean checkTrap(ChessboardPoint point) {
        ChessPiece enemy = getChessPieceAt(point);
            if (isEnemyTrap(point, enemy.getOwner())){
               return true;
            }
            return false;
    }

    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        ChessPiece newChess = removeChessPiece(src);
        getGridAt(dest).setPiece(newChess);
        steps.add(new Step(src,dest,newChess.getOwner()));
    }

    public void capture(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        ChessPiece eater = removeChessPiece(src);
        ChessPiece enemy = removeChessPiece(dest);
        getGridAt(dest).setPiece(eater);
        if (enemy.getOwner() == PlayerColor.BLUE) {
            deadRedChess.add(enemy);
        }
        else {
            deadRedChess.add(enemy);
        }
        steps.add(new Step(src, dest, eater.getOwner(), enemy));
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
        if (myChess.getName().equals("Elephant")){
            return  checkDistance(src,dest) && !isRiver(dest);
        }
        if (myChess.getName().equals("Lion")){
            return (checkDistance(src,dest) && !isRiver(dest)) || canJumpRiver(src, dest);
        }
        if (myChess.getName().equals("Tiger")){
            return (checkDistance(src,dest) && !isRiver(dest)) || canJumpRiver(src, dest);
        }
        if (myChess.getName().equals("Leopard")){
            return checkDistance(src,dest) && !isRiver(dest);
        }
        if (myChess.getName().equals("Wolf")){
            return checkDistance(src,dest) && !isRiver(dest);
        }
        if (myChess.getName().equals("Dog")){
            return checkDistance(src,dest) && !isRiver(dest);
        }
        if (myChess.getName().equals("Cat")){
            return checkDistance(src,dest) && !isRiver(dest);
        }
        if (myChess.getName().equals("Rat")){
            return checkDistance(src,dest);
        }
        return false;
    }


    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
        ChessPiece eater = getChessPieceAt(src);
        ChessPiece enemy = getChessPieceAt(dest);
        if (eater==null || enemy==null) {
            return false;
        }
        if (eater.getOwner()==enemy.getOwner()){
            return false;
        }
        if (eater.getName().equals("Elephant")){
            return checkDistance(src,dest) && !isRiver(dest) && (enemy.getRank() !=1||checkTrap(dest));
        }
        if (eater.getName().equals("Lion")){
            return ((checkDistance(src,dest) && !isRiver(dest)) || canJumpRiver(src, dest)) && (enemy.getRank()<= 7||checkTrap(dest));
        }
        if (eater.getName().equals("Tiger")){
            return ((checkDistance(src,dest) && !isRiver(dest)) || canJumpRiver(src, dest)) && (enemy.getRank() <= 6||checkTrap(dest));
        }
        if (eater.getName().equals("Leopard")){
            return checkDistance(src,dest) && !isRiver(dest) && (enemy.getRank()<= 5||checkTrap(dest));
        }
        if (eater.getName().equals("Wolf")){
            return checkDistance(src,dest) && !isRiver(dest) && (enemy.getRank()<= 4||checkTrap(dest));
        }
        if (eater.getName().equals("Dog")){
            return checkDistance(src,dest) && !isRiver(dest) && (enemy.getRank() <= 3||checkTrap(dest));
        }
        if (eater.getName().equals("Cat")){
            return checkDistance(src,dest) && !isRiver(dest) && (enemy.getRank() <= 2||checkTrap(dest));
        }
        if (eater.getName().equals("Rat")){
            return checkDistance(src,dest) && (enemy.getRank() <= 1 || enemy.getRank() == 8||checkTrap(dest)) && !(isRiver(src) && !isRiver(dest));
        }
        return false;
    }
    private boolean checkDistance(ChessboardPoint src,ChessboardPoint dest){
        return calculateDistance(src,dest) == 1;
    }
    private boolean isRiver(ChessboardPoint point){
        return point.getRow() >= 3
                && point.getRow() <= 5
                && (point.getCol() == 1 || point.getCol() == 2 || point.getCol() == 4 || point.getCol() == 5);

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
