package chess;
import chess.*;
import chess.ChessPieceImpl.Bishop;
import chess.ChessPieceImpl.Knight;
import chess.ChessPieceImpl.Queen;
import chess.ChessPieceImpl.Rook;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChessGameImpl implements ChessGame {
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> allMoves = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
        Set<ChessMove> allValidMoves = new HashSet<>();;
        for(ChessMove move : allMoves){
            if(validMove(move)) {
                allValidMoves.add(move);
            }
            else{
                continue;
            }
        }
        return allValidMoves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(chessBoard.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("Invalid move.");
        }
        Collection<ChessMove> allValidMoves = validMoves(move.getStartPosition());
        for(ChessMove validMove : allValidMoves){
            if(move.equals(validMove)){
                setTeamTurn(oppositeColor(chessBoard.getPiece(move.getStartPosition()).getTeamColor()));
                if(move.getPromotionPiece() == null){
                    chessBoard.addPiece(move.getEndPosition(), chessBoard.getPiece(move.getStartPosition()));
                }
                else{
                    switch(move.getPromotionPiece()){
                        case QUEEN -> {
                            chessBoard.addPiece(move.getEndPosition(), new Queen(chessBoard.getPiece(move.getStartPosition()).getTeamColor()));
                        }
                        case BISHOP -> {
                            chessBoard.addPiece(move.getEndPosition(), new Bishop(chessBoard.getPiece(move.getStartPosition()).getTeamColor()));
                        }
                        case KNIGHT -> {
                            chessBoard.addPiece(move.getEndPosition(), new Knight(chessBoard.getPiece(move.getStartPosition()).getTeamColor()));
                        }
                        case ROOK -> {
                            chessBoard.addPiece(move.getEndPosition(), new Rook(chessBoard.getPiece(move.getStartPosition()).getTeamColor()));
                        }
                    }
                }
                chessBoard.removePiece(move.getStartPosition());
                return;
            }
        }
        throw new InvalidMoveException("Invalid move.");
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor oppositionColor = oppositeColor(teamColor);
        ChessPosition kingPosition = chessBoard.findKing(teamColor);
        if(kingPosition == null){
            return false;
        }
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
        if(!isInCheck(teamColor)){
            return false;
        }
        Collection<ChessPosition> positions = chessBoard.findAllPositions(teamColor);
        for(ChessPosition position : positions){
            if(!validMoves(position).isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        Collection<ChessPosition> positions = chessBoard.findAllPositions(teamColor);
        for(ChessPosition position : positions){
            if(!validMoves(position).isEmpty()){
                return false;
            }
        }
        return true;
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
    TeamColor teamTurn = TeamColor.WHITE;
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
    public boolean validMove(ChessMove move){  //checks move restores board to original state at the end
        ChessPiece saved = chessBoard.getPiece(move.getEndPosition());
        chessBoard.addPiece(move.getEndPosition(), chessBoard.getPiece(move.getStartPosition()));
        chessBoard.removePiece(move.getStartPosition());
        boolean inCheck = isInCheck(chessBoard.getPiece(move.getEndPosition()).getTeamColor());

        //restoring state of board before call to function
        chessBoard.addPiece(move.getStartPosition(), chessBoard.getPiece(move.getEndPosition()));
        chessBoard.removePiece(move.getEndPosition());
        chessBoard.addPiece(move.getEndPosition(), saved);

        //if it puts king in check the move is invalid
        return !inCheck;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGameImpl chessGame = (ChessGameImpl) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(chessBoard, chessGame.chessBoard);
    }
    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, chessBoard);
    }
}
