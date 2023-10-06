package chess.ChessPieceImpl;

import chess.*;

import java.util.HashSet;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class Rook implements ChessPiece {
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.ROOK;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        //up
        for(int i = myPosition.getRow() + 1; i < 9; ++i){
            ChessPosition endPosition = new ChessPositionImpl(i, myPosition.getColumn());
            if(board.getPiece(endPosition) == null){
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }
            else{
                if(board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    moves.add(new ChessMoveImpl(myPosition, endPosition));
                }
                break;
            }
        }
        //down
        for(int i = myPosition.getRow() - 1; i > 0; --i){
            ChessPosition endPosition = new ChessPositionImpl(i, myPosition.getColumn());
            if(board.getPiece(endPosition) == null){
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }
            else{
                if(board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    moves.add(new ChessMoveImpl(myPosition, endPosition));
                }
                break;
            }
        }
        //left
        for(int i = myPosition.getColumn() + 1; i < 9; ++i){
            ChessPosition endPosition = new ChessPositionImpl(myPosition.getRow(), i);
            if(board.getPiece(endPosition) == null){
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }
            else{
                if(board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    moves.add(new ChessMoveImpl(myPosition, endPosition));
                }
                break;
            }
        }
        //right
        for(int i = myPosition.getColumn() - 1; i > 0; --i){
            ChessPosition endPosition = new ChessPositionImpl(myPosition.getRow(), i);
            if(board.getPiece(endPosition) == null){
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }
            else{
                if(board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    moves.add(new ChessMoveImpl(myPosition, endPosition));
                }
                break;
            }
        }
        return moves;
    }

    //self defined
    public Rook(ChessGame.TeamColor teamColor){
        this.teamColor = teamColor;
    }
    ChessGame.TeamColor teamColor;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rook rook = (Rook) o;
        return teamColor == rook.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor);
    }
}
