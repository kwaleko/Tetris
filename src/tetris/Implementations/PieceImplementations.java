package tetris.Implementations;

import java.util.Arrays;
import tetris.game.pieces.Piece.PieceType;

import tetris.game.pieces.Piece;
import tetris.game.pieces.Point;

public class PieceImplementations implements Piece {

    private PieceType pieceType;
    private boolean[][] body;
    private Point rotationPoint;

    public PieceImplementations(PieceType pType, boolean[][] body, Point rPoint) {
        this.body = body;
        this.rotationPoint = rPoint;
        this.pieceType = pType;
    }

    public PieceImplementations(PieceType pType) {
        this.pieceType = pType;
        switch (pType) {
            case I:
                this.body = PieceImplementations.createIShape();
                this.rotationPoint = new Point(1, 0);
                break;
            case J:
                this.body = PieceImplementations.createJShape();
                this.rotationPoint = new Point(1, 1);
                break;
            case L:
                this.body = PieceImplementations.createLShape();
                this.rotationPoint = new Point(1, 0);
                break;
            case O:
                this.body = PieceImplementations.createOShape();
                this.rotationPoint = new Point(1, 1);
                break;
            case S:
                this.body = PieceImplementations.createSShape();
                this.rotationPoint = new Point(1, 1);
                break;
            case T:
                this.body = PieceImplementations.createTShape();
                this.rotationPoint = new Point(0, 1);
                break;
            case Z:
                this.body = PieceImplementations.createZShape();
                this.rotationPoint = new Point(1, 1);
                break;
            default:
        }
    }

    private static boolean[][] createIShape() {
        return new boolean[][] { { true },
                { true },
                { true },
                { true } };
    }

    private static boolean[][] createSShape() {
        return new boolean[][] { { false, true, true },
                { true, true, false } };
    }

    private static boolean[][] createJShape() {
        return new boolean[][] { { false, true },
                { false, true },
                { true, true } };
    }

    private static boolean[][] createLShape() {
        return new boolean[][] { { true, false },
                { true, false },
                { true, true } };
    }

    private static boolean[][] createOShape() {
        return new boolean[][] { { true, true },
                { true, true } };
    }

    private static boolean[][] createTShape() {
        return new boolean[][] { { true, true, true },
                { false, true, false } };
    }

    private static boolean[][] createZShape() {
        return new boolean[][] { { true, true, false },
                { false, true, true } };
    }

    @Override
    public int getWidth() {
        return body[0].length;

    }

    @Override
    public int getHeight() {
        return body.length;

    }

    @Override
    public boolean[][] getBody() {
        return this.body;

    }

    @Override
    public Piece getClockwiseRotation() {
        int height = this.getHeight();
        int width = this.getWidth();
        boolean nBody[][] = new boolean[width][height];
        Point imgRotPoint;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Point imageP = mapPoint(i, j, height);
                nBody[imageP.getRow()][imageP.getColumn()] = this.getBody()[i][j];
            }
        }
        imgRotPoint = mapPoint(this.getRotationPoint().getRow(), this.getRotationPoint().getColumn(), height);
        return new PieceImplementations(this.pieceType, nBody, imgRotPoint);

    }

    @Override
    public Piece getCounterClockwiseRotation() {
        int height = this.getHeight();
        int width = this.getWidth();
        boolean nBody[][] = new boolean[width][height];
        Point imgRotPoint;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Point imageP = mapPointCounterClock(i, j, width);
                nBody[imageP.getRow()][imageP.getColumn()] = this.getBody()[i][j];
            }
        }
        imgRotPoint = mapPointCounterClock(this.getRotationPoint().getRow(), this.getRotationPoint().getColumn(), width);
        return new PieceImplementations(this.pieceType, nBody, imgRotPoint);

    }

    @Override
    public Point getRotationPoint() {
        return this.rotationPoint;
    }

    @Override
    public PieceType getPieceType() {
        return this.pieceType;

    }

    public Point getRotationPoint(PieceType pType) {
        return this.rotationPoint;
    }

    public static Point mapPoint(int i, int j, int height) {
        // i represent the row of the point
        // j represent the column of the point
        int nRow, nColumn;
        nRow = j;
        nColumn = height - i - 1;
        return new Point(nRow, nColumn);
    }

        public static Point mapPointCounterClock(int i, int j, int width) {
        // i represent the row of the point
        // j represent the column of the point
        int nRow, nColumn;
        nRow = width - j -1 ;
        nColumn = i;// height - i - 1;
        return new Point(nRow, nColumn);
    }

    @Override
    public boolean equals(Object p) {
        if (p instanceof PieceImplementations) {
            PieceImplementations p1 = (PieceImplementations) p;
            return (p1.getPieceType() == this.pieceType &&
                    p1.getRotationPoint().equals(this.rotationPoint) &&
                    p1.getWidth() == this.getWidth() &&
                    p1.getHeight() == this.getHeight());
        } else
            return false;
    }

    @Override
    public Piece clone() {
        Point newRPoint;
        boolean[][] copy = new boolean[body.length][];
        if (this.body == null)
            return null;
        else {
            for (int i = 0; i < body.length; i++) {
                copy[i] = Arrays.copyOf(body[i], body[i].length);
            }
        }
        newRPoint = new Point(this.rotationPoint.getRow(), this.rotationPoint.getColumn());

        return new PieceImplementations(this.pieceType, copy, newRPoint);

    }

 
    public int hashCode(Object o){
        int result = 42;

        return result;
    }

}
