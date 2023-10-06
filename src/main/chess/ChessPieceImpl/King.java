package chess.ChessPieceImpl;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class King implements ChessPiece {
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.KING;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        Set<ChessPosition> endPositions = new HashSet<>();
        endPositions.add(new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() - 1));
        endPositions.add(new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn()));
        endPositions.add(new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() + 1));
        endPositions.add(new ChessPositionImpl(myPosition.getRow(), myPosition.getColumn() + 1));
        endPositions.add(new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() + 1));
        endPositions.add(new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn()));
        endPositions.add(new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() - 1));
        endPositions.add(new ChessPositionImpl(myPosition.getRow(), myPosition.getColumn() - 1));
        for(ChessPosition position : endPositions){
            if(0 < position.getColumn() && position.getColumn() < 9 && 0 < position.getRow() && position.getRow() < 9){
                if(board.getPiece(position) == null){
                    moves.add(new ChessMoveImpl(myPosition, position));
                }
                else if(board.getPiece(position).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    moves.add(new ChessMoveImpl(myPosition, position));
                }
            }
        }
        return moves;
    }

    //self defined
    public King(ChessGame.TeamColor teamColor){
        this.teamColor = teamColor;
    }
    ChessGame.TeamColor teamColor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        King king = (King) o;
        return teamColor == king.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor);
    }
}
