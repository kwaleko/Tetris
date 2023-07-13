package tetris.Implementations;

import java.util.Arrays;

import tetris.game.Board;
import tetris.game.pieces.Piece.PieceType;
import tetris.game.pieces.Piece;
import tetris.game.pieces.Point;

public class BoardImplementation implements Board {

    private PieceType[][] board;

    public BoardImplementation() {

    }

    public BoardImplementation(PieceType[][] board) {
        this.board = board;
    }

    public BoardImplementation(int rows, int columns) {
        this.board = new PieceType[rows][columns];
    }

    public PieceType[][] getBoard() {
        return board;
    }

    public int getNumberOfRows() {
        return this.getBoard().length;
    }

    public int getNumberOfColumns() {
        return this.getBoard()[0].length;
    }

    public boolean canAddPiece(Piece piece, int row, int col) {

        if (piece == null)
            throw new IllegalArgumentException();

        Point rPoint = piece.getRotationPoint();
        int coordPieceY = row - rPoint.getRow();
        int coordPieceX = col - rPoint.getColumn();

        if ((coordPieceX < 0) || (coordPieceY< 0)
                || (coordPieceY + piece.getHeight() > this.getNumberOfRows())
                || (coordPieceX + piece.getWidth() > this.getNumberOfColumns()))
            return false;
        


         for (int i = 0; i < piece.getHeight(); i++) {
            for (int j = 0; j < piece.getWidth(); j++) {
                if (piece.getBody()[i][j]  && this.getBoard()[i + coordPieceY][j + coordPieceX] != null)
                    return false;
            }
        }
        return true;
    }

    public void addPiece(Piece piece, int row, int col) {
        if (!canAddPiece(piece, row, col))
            throw new IllegalArgumentException();
        else {
            Point rPoint = piece.getRotationPoint();
            int originY = row - rPoint.getRow();
            int originX = col - rPoint.getColumn();

            for (int i = 0; i < piece.getHeight(); i++) {
                for (int j = 0; j < piece.getWidth(); j++) {
                    if (piece.getBody()[i][j])
                        board[originY + i][originX + j] = piece.getPieceType();
                }
            }
        }
    }

    public boolean canRemovePiece(Piece piece, int row, int col) {
        if (piece == null)
            throw new IllegalArgumentException();

        Point rPoint = piece.getRotationPoint();
        int originY = row - rPoint.getRow();
        int originX = col - rPoint.getColumn();

        if ((originX < 0) || (originY < 0)
                || (originY + piece.getHeight() > this.getNumberOfRows())
                || (originX + piece.getWidth() > this.getNumberOfColumns()))
            return false;

        for (int i = 0; i < piece.getHeight(); i++) {
            for (int j = 0; j < piece.getWidth(); j++) {
                if (piece.getBody()[i][j] && this.getBoard()[i + originY][j + originX] != piece.getPieceType())
                    return false;
            }
        }
        return true;
    }

    public void removePiece(Piece piece, int row, int col) {
        if (!canRemovePiece(piece, row, col))
            throw new IllegalArgumentException();
        Point rPoint = piece.getRotationPoint();
        int originY = row - rPoint.getRow();
        int originX = col - rPoint.getColumn();

        for (int i = 0; i < piece.getHeight(); i++) {
            for (int j = 0; j < piece.getWidth(); j++) {
                if (piece.getBody()[i][j])
                    board[originY + i][originX + j] = null;
            }
        }
    }

    public int deleteCompleteRows() {
        int rowMoved = 0;
        for (int i = this.getNumberOfRows()-1; i >= 0; i--) {
            if (rowCompleted(i)) {
                this.moveRowsDown(i);
                rowMoved++;
                i++;
            }
        }
        return rowMoved;
    }

    public boolean rowCompleted(int row) {
        for (int i = 0; i < this.getNumberOfColumns(); i++)
            if (this.getBoard()[row][i] == null)
                return false;
        return true;
    }

    public void moveRowsDown(int row) {
        // move all rows above the given row down
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                this.board[i][j] = this.board[i - 1][j];
            }
        }
        // set the first top row to null.
        for (int j = 0; j < this.getNumberOfColumns(); j++)
            this.board[0][j] = null;
    }

    public Board clone() {
        PieceType[][] body = new PieceType[this.getNumberOfRows()][];
        if (this.getBoard() == null)
            return null;
        for (int i = 0; i < this.getNumberOfRows(); i++)
            body[i] = Arrays.copyOf(this.getBoard()[i], this.getBoard()[i].length);
        return new BoardImplementation(body);
    }

    @Override
    public boolean equals(Object o){
        Board b = (Board)o;
        for(int i=0;i<this.getBoard().length;i++)
            if(!Arrays.equals(b.getBoard()[i], this.getBoard()[i]))
                return false;
        return true;
    }

    public int hashOfCol(PieceType pieceType ){
        int result  = 42;
        switch (pieceType) {
            case I:
                result = result * 2;
                break;
            case J:
                result = result * 3;
                break;
            case L:
    
                result = result * 4;
                break;
            case O:
                result = result * 5;
                break;
            case S:
                result = result * 6;
                break;
            case T:
                result = result * 7;
                break;
            case Z:
                result = result * 8;
                break;
            default:
        }

        return result;

    }

    

    
    public int hashCode(Object o){
        int result = 42;
        return result;
    }

   

}
