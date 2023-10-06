package chess;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ChessGameImpl implements ChessGame {
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {

    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
//        TeamColor teamColor = chessBoard.getPiece(startPosition).getTeamColor();
//        Collection<ChessMove> allMoves = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
//        for(ChessMove move : allMoves){
//            ChessBoard copyBoard = chessBoard.clone();
//            //move the piece on the copied board
//
//            //somehow check if teamColor king is in check on copyBoard maybe make new game for board?
//            if(teamColor king is in check on copyBoard){
//                allMoves.remove(move)     //might be incorrect implementation of method
//            }
//        }
//        if(chessBoard.getPiece(startPosition) != null){
//            return chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
//        }
        return null;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {

    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor oppositionColor = oppositeColor(teamColor);
        ChessPosition kingPosition = chessBoard.findKing(teamColor);
        Collection<ChessMove> allMoves = allPossibleMoves(oppositionColor);
        for(ChessMove move : allMoves){
            if(move.getEndPosition().getRow() == kingPosition.getRow() && move.getEndPosition().getColumn() == kingPosition.getColumn()){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.chessBoard = (ChessBoardImpl) board;
    }

    @Override
    public ChessBoard getBoard() {
        return chessBoard;
    }

    //self defined
    TeamColor teamTurn;
    ChessBoardImpl chessBoard = new ChessBoardImpl();
    private Collection<ChessMove> allPossibleMoves(TeamColor teamColor) {
        Set<ChessMove> moves = new HashSet<>();
        Collection<ChessPosition> positions = chessBoard.findAllPositions(teamColor);
        for(ChessPosition position : positions){
            moves.addAll(chessBoard.getPiece(position).pieceMoves(chessBoard, position));
        }
        return moves;
    }
    public TeamColor oppositeColor(TeamColor teamColor){
        if(teamColor == TeamColor.BLACK){
            return TeamColor.WHITE;
        }
        else{
            return TeamColor.BLACK;
        }
    }
}
