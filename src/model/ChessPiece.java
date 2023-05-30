package model;


public class ChessPiece {
    // the owner of the chess
    private PlayerColor owner;
    private String name;

    public int getRank() {
        return rank;
    }

    public int rank;

    public ChessPiece(PlayerColor owner, String name, int rank) {
        this.owner = owner;
        this.name = name;
        this.rank = rank;
    }
    public String getName() {
        return name;
    }

    public PlayerColor getOwner() {
        return owner;
    }
}
