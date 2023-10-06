package chess.ChessPieceImpl;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Bishop implements ChessPiece {
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.BISHOP;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        //up right
        for(int i = myPosition.getRow() + 1, j = myPosition.getColumn() + 1; i < 9 && j < 9; ++i, ++j){
            ChessPosition endPosition = new ChessPositionImpl(i, j);
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
        //up left
        for(int i = myPosition.getRow() + 1, j = myPosition.getColumn() - 1; i < 9 && j > 0; ++i, --j){
            ChessPosition endPosition = new ChessPositionImpl(i, j);
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
        //down right
        for(int i = myPosition.getRow() - 1, j = myPosition.getColumn() + 1; i > 0 && j < 9; --i, ++j){
            ChessPosition endPosition = new ChessPositionImpl(i, j);
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
        //down left
        for(int i = myPosition.getRow() - 1, j = myPosition.getColumn() - 1; i > 0 && j > 0; --i, --j){
            ChessPosition endPosition = new ChessPositionImpl(i, j);
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
    public Bishop(ChessGame.TeamColor teamColor){
        this.teamColor = teamColor;
    }
    ChessGame.TeamColor teamColor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bishop bishop = (Bishop) o;
        return teamColor == bishop.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor);
    }
}
