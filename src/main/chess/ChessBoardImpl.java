package chess;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPieceImpl.*;
import chess.ChessPosition;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ChessBoardImpl implements ChessBoard {
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        chessBoard[position.getRow()-1][position.getColumn()-1] = piece;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        if(chessBoard[position.getRow()-1][position.getColumn()-1] != null){
            return chessBoard[position.getRow()-1][position.getColumn()-1];
        }
        return null;
    }

    @Override
    public void resetBoard() {
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[i].length; j++) {
                chessBoard[i][j] = null;
            }
        }
        //adding whites
        addPiece(new ChessPositionImpl(1,1), new Rook(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPositionImpl(1,2), new Knight(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPositionImpl(1,3), new Bishop(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPositionImpl(1,4), new Queen(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPositionImpl(1,5), new King(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPositionImpl(1,6), new Bishop(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPositionImpl(1,7), new Knight(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPositionImpl(1,8), new Rook(ChessGame.TeamColor.WHITE));
        for(int i = 0; i < chessBoard.length; ++i){
            addPiece(new ChessPositionImpl(2,i+1), new Pawn(ChessGame.TeamColor.WHITE));
        }
        //adding blacks
        addPiece(new ChessPositionImpl(8,1), new Rook(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPositionImpl(8,2), new Knight(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPositionImpl(8,3), new Bishop(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPositionImpl(8,4), new Queen(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPositionImpl(8,5), new King(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPositionImpl(8,6), new Bishop(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPositionImpl(8,7), new Knight(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPositionImpl(8,8), new Rook(ChessGame.TeamColor.BLACK));
        for(int i = 0; i < chessBoard.length; ++i){
            addPiece(new ChessPositionImpl(7,i+1), new Pawn(ChessGame.TeamColor.BLACK));
        }
    }

    //self defined
    ChessPiece[][] chessBoard = new ChessPiece[8][8];
    public ChessPosition findKing(ChessGame.TeamColor teamColor) {      //returns
        for(int i = 1; i < 9; ++i){
            for(int j = 1; j < 9; ++j){
                ChessPositionImpl possiblePosition = new ChessPositionImpl(i,j);
                if(getPiece(possiblePosition) != null && getPiece(possiblePosition).getPieceType() == ChessPiece.PieceType.KING && getPiece(possiblePosition).getTeamColor() == teamColor) {
                    return possiblePosition;
                }
            }
        }
        return null;
    }
    public Collection<ChessPosition> findAllPositions(ChessGame.TeamColor teamColor){
        Set<ChessPosition> positions = new HashSet<>();
        for(int i = 1; i < 9; ++i){
            for(int j = 1; j < 9; ++j){
                ChessPositionImpl possiblePosition = new ChessPositionImpl(i,j);
                if(getPiece(possiblePosition) != null && getPiece(possiblePosition).getTeamColor() == teamColor) {
                    positions.add(possiblePosition);
                }
            }
        }
        return positions;
    }
    public void removePiece(ChessPosition position) {
        chessBoard[position.getRow()-1][position.getColumn()-1] = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoardImpl that = (ChessBoardImpl) o;
        return Arrays.equals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(chessBoard);
    }

}
