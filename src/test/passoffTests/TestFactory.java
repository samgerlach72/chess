package passoffTests;

import chess.*;

/**
 * Used for testing your code
 * Add in code using your classes for each method for each FIXME
 */
public class TestFactory {

    //Chess Functions
    //------------------------------------------------------------------------------------------------------------------
    public static ChessBoard getNewBoard(){
		return new ChessBoardImpl();
    }

    public static ChessGame getNewGame(){
		return new ChessGameImpl();
    }

    public static ChessPiece getNewPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        switch(type){
            case KING -> {
                return new chess.ChessPieceImpl.King(pieceColor);
            }
            case QUEEN -> {
                return new chess.ChessPieceImpl.Queen(pieceColor);
            }
            case BISHOP -> {
                return new chess.ChessPieceImpl.Bishop(pieceColor);
            }
            case KNIGHT -> {
                return new chess.ChessPieceImpl.Knight(pieceColor);
            }
            case ROOK -> {
                return new chess.ChessPieceImpl.Rook(pieceColor);
            }
            case PAWN -> {
                return new chess.ChessPieceImpl.Pawn(pieceColor);
            }
        }
        return null;
    }

    public static ChessPosition getNewPosition(Integer row, Integer col){
		return new ChessPositionImpl(row, col);
    }

    public static ChessMove getNewMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece){
		return new ChessMoveImpl(startPosition, endPosition, promotionPiece);
    }
    //------------------------------------------------------------------------------------------------------------------


    //server.Server API's
    //------------------------------------------------------------------------------------------------------------------
    public static String getServerPort(){
        return "8080";
    }
    //------------------------------------------------------------------------------------------------------------------


    //Websocket Tests
    //------------------------------------------------------------------------------------------------------------------
    public static Long getMessageTime(){
        /*
        Changing this will change how long tests will wait for the server to send messages.
        3000 Milliseconds (3 seconds) will be enough for most computers. Feel free to change as you see fit,
        just know increasing it can make tests take longer to run.
        (On the flip side, if you've got a good computer feel free to decrease it)
         */
        return 500L;
    }
    //------------------------------------------------------------------------------------------------------------------
}
