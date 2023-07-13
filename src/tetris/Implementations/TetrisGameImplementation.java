package tetris.Implementations;

import tetris.game.TetrisGame;
import tetris.game.Board;
import tetris.game.pieces.Piece;
import tetris.game.GameObserver;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.Random;

public class TetrisGameImplementation implements TetrisGame {

    private Board playingField;
    private Random rand;
    private Piece currentPiece, nextPiece;
    private int row, column;
    private long points;
    private int nbCompletedRows;
    private boolean gameOver;
    private ArrayList<GameObserver> observers;

    public TetrisGameImplementation(Random rand) {
        this.rand = rand;
        this.playingField = new BoardImplementation(20, 10); // to check
        this.nextPiece = new PieceFactoryImplementation(this.rand).getNextRandomPiece();
        this.observers = new ArrayList<GameObserver>();
    }

    public TetrisGameImplementation(Board board, Piece currentPiece, Piece nextPiece, long points) {
        this.playingField = board;
        this.currentPiece = currentPiece;
        this.nextPiece = nextPiece;
        this.points = points;
        this.row = 2;
        this.column = board.getNumberOfColumns() / 2;
        this.rand = new Random(123);
        this.observers = new ArrayList<GameObserver>();
    }

    @Override
    public Piece getCurrentPiece() {
        return this.currentPiece;
    }

    @Override
    public Board getBoard() {
        return this.playingField;
    }

    @Override
    public Piece getNextPiece() {
        return this.nextPiece;
    }

    @Override
    public int getNumberOfCompletedRows() {
        return nbCompletedRows;
    }

    @Override
    public int getPieceColumn() {
        return this.column;
    }

    @Override
    public int getPieceRow() {
        return this.row;
    }

    @Override
    public long getPoints() {
        return this.points;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public boolean moveDown() {
        //if (this.currentPiece == null)
        //    return false;
        try {
            playingField.removePiece(currentPiece, this.row, this.column);
            playingField.addPiece(currentPiece, this.row + 1, this.column);
            this.row = this.row + 1;
            if (observers != null)
                observers.stream().forEach(observer -> observer.piecePositionChanged());
            return true;

        } catch (IllegalArgumentException e) {
            playingField.addPiece(currentPiece, row, column);
            return false;
        }
    }

    @Override
    public boolean moveLeft() {
        //if (this.currentPiece == null)
        //    return false;
        try {
            playingField.removePiece(currentPiece, row, column);
            playingField.addPiece(currentPiece, row, column - 1);
            this.column = this.column - 1;
            if (observers != null)
                observers.stream().forEach(observer -> observer.piecePositionChanged());
            return true;
        } catch (IllegalArgumentException e) {
            playingField.addPiece(currentPiece, row, column);
            return false;
        }
    }

    @Override
    public boolean moveRight() {
        //if(this.currentPiece == null) 
        //    return false;
        try {
            playingField.removePiece(currentPiece, row, column);
            playingField.addPiece(currentPiece, row, column + 1);
            this.column = this.column + 1;
            if (observers != null)
                observers.stream().forEach(observer -> observer.piecePositionChanged());
            return true;

        } catch (IllegalArgumentException e) {
            playingField.addPiece(currentPiece, row, column);
            return false;
        }
    }

    @Override
    public boolean newPiece() {
        int comp = playingField.deleteCompleteRows();
        this.nbCompletedRows+= comp;
        if (comp == 1)
            this.points += 100;
        if (comp == 2)
            this.points += 300;
        if (comp == 3)
            this.points += 500;
        if (comp == 4)
            this.points += 1000;

        if (comp > 0) {
            if (observers != null)
                observers.stream().forEach(observer -> observer.rowsCompleted());
        }

        try {
            this.currentPiece = this.nextPiece;
            row = 2;
            column = playingField.getNumberOfColumns() / 2;
            playingField.addPiece(this.currentPiece, row, column);
            this.nextPiece = new PieceFactoryImplementation(rand).getNextRandomPiece();
            return true;

        } catch (IllegalArgumentException e) {
            this.setGameOver();
            return false;
        }

    }

    @Override
    public boolean rotatePieceClockwise() {
        try {
            Piece rotated = currentPiece.getClockwiseRotation();
            playingField.removePiece(currentPiece, row, column);
            playingField.addPiece(rotated, row, column);
            this.currentPiece = rotated;
            if (observers != null)
                observers.stream().forEach(observer -> observer.piecePositionChanged());
            return true;
        } catch (IllegalArgumentException e) {
            playingField.addPiece(currentPiece, row, column);
            return false;
        }
    }

    @Override
    public boolean rotatePieceCounterClockwise() {
        try {
            Piece rotated = currentPiece.getCounterClockwiseRotation();
            playingField.removePiece(currentPiece, row, column);
            playingField.addPiece(rotated, row, column);
            this.currentPiece = rotated;
            if (observers != null)
                observers.stream().forEach(observer -> observer.piecePositionChanged());
            return true;

        } catch (IllegalArgumentException e) {
            playingField.addPiece(currentPiece, row, column);
            return false;
        }
    }

    @Override
    public void setGameOver() {
        this.gameOver = true;
        observers.stream().forEach(observer -> observer.gameOver());
    }

    @Override
    public void step() {
        if (!gameOver) {
            if (this.currentPiece == null)
                this.newPiece();
            else {
                boolean moved = this.moveDown();
                if (!moved) {
                    observers.stream().forEach(observer -> observer.pieceLanded());
                    this.newPiece();
                }  
            }
        }
    }

    @Override
    public void addObserver(GameObserver observer) {
        if (observer != null ) //&& !observers.contains(observer))
            observers.add(observer);
    }

    @Override
    public void removeObserver(GameObserver observer) {
        if (observer != null && observers.contains(observer))
            observers.remove(observer);

    }

}
