package chess.ChessPieceImpl;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Pawn implements ChessPiece {
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.PAWN;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            whiteMoves(board, myPosition, moves);
        }
        else{
            blackMoves(board, myPosition, moves);
        }
        return moves;
    }

    //self defined
    public Pawn(ChessGame.TeamColor teamColor){
        this.teamColor = teamColor;
    }
    ChessGame.TeamColor teamColor;

    public void whiteMoves(ChessBoard board, ChessPosition myPosition, Set<ChessMove> moves){
        ChessPosition endPosition = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        if(board.getPiece(endPosition) != null && endPosition.getColumn() < 9 && board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            if(endPosition.getRow() == 8){
                promotionMoves(myPosition, endPosition, moves);
            }
            else{
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }
        }
        endPosition = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if(board.getPiece(endPosition) != null && 0 < endPosition.getColumn() && board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            if(endPosition.getRow() == 8){
                promotionMoves(myPosition, endPosition, moves);
            }
            else{
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }
        }
        endPosition = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn());
        if(board.getPiece(endPosition) == null){
            if(endPosition.getRow() == 8){
                promotionMoves(myPosition, endPosition, moves);
            }
            else{
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }
            if(myPosition.getRow() == 2){   //special case if pawn hasn't moved, spot in-between must be null
                endPosition = new ChessPositionImpl(myPosition.getRow() + 2, myPosition.getColumn());
                if(board.getPiece(endPosition) == null){
                    moves.add(new ChessMoveImpl(myPosition, endPosition));
                }
            }
        }

    }

    public void blackMoves(ChessBoard board, ChessPosition myPosition, Set<ChessMove> moves){
        ChessPosition endPosition = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        if(board.getPiece(endPosition) != null && endPosition.getColumn() < 9 && board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            if(endPosition.getRow() == 1){
                promotionMoves(myPosition, endPosition, moves);
            }
            else{
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }
        }
        endPosition = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        if(board.getPiece(endPosition) != null && 0 < endPosition.getColumn() && board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            if(endPosition.getRow() == 1){
                promotionMoves(myPosition, endPosition, moves);
            }
            else{
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }
        }
        endPosition = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn());
        if(board.getPiece(endPosition) == null){
            if(endPosition.getRow() == 1){
                promotionMoves(myPosition, endPosition, moves);
            }
            else{
                moves.add(new ChessMoveImpl(myPosition, endPosition));
            }
            if(myPosition.getRow() == 7){   //special case if pawn hasn't moved, spot in-between must be null
                endPosition = new ChessPositionImpl(myPosition.getRow() - 2, myPosition.getColumn());
                if(board.getPiece(endPosition) == null){
                    moves.add(new ChessMoveImpl(myPosition, endPosition));
                }
            }
        }
    }

    void promotionMoves(ChessPosition myPosition, ChessPosition endPosition, Set<ChessMove> moves){
            moves.add(new ChessMoveImpl(myPosition, endPosition, PieceType.ROOK));
            moves.add(new ChessMoveImpl(myPosition, endPosition, PieceType.KNIGHT));
            moves.add(new ChessMoveImpl(myPosition, endPosition, PieceType.QUEEN));
            moves.add(new ChessMoveImpl(myPosition, endPosition, PieceType.BISHOP));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pawn pawn = (Pawn) o;
        return teamColor == pawn.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor);
    }
}
