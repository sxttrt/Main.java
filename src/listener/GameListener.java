package listener;

import model.ChessboardPoint;
import view.CellComponent;
import view.animal.Animal;


public interface GameListener {

    void onPlayerClickCell(ChessboardPoint point, CellComponent component);


    void onPlayerClickChessPiece(ChessboardPoint point, Animal component);

}
