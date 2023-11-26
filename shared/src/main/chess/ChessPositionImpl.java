package chess;

import java.util.Objects;

public class ChessPositionImpl implements ChessPosition {
    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    //self defined
    public ChessPositionImpl(int row, int column){
        this.row = row;
        this.column = column;
    }
    public ChessPositionImpl(){
        this.row = 0;
        this.column = 0;
    }
    private int row;
    private int column;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImpl that = (ChessPositionImpl) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
