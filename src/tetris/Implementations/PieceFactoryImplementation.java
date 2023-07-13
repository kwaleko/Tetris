package tetris.Implementations;

import java.util.Random;

import tetris.game.pieces.Piece;
import tetris.game.pieces.PieceFactory;
import tetris.game.pieces.Piece.PieceType;



public class PieceFactoryImplementation implements PieceFactory {
    private Random r;

    public PieceFactoryImplementation(Random rand){
        this.r=rand;
     }

    public Piece getIPiece(){
        return new PieceImplementations(PieceType.I);
    }

    public Piece getJPiece(){
        return new PieceImplementations(PieceType.J);
    }

    public Piece getLPiece(){
        return new PieceImplementations(PieceType.L);
    }

    public Piece getOPiece(){
        return new PieceImplementations(PieceType.O);
    }

    public Piece getSPiece(){
        return new PieceImplementations(PieceType.S);
    }

    public Piece getTPiece(){
        return new PieceImplementations(PieceType.T);
    }

    public Piece getZPiece(){
        return new PieceImplementations(PieceType.Z);
    }

    public Piece getNextRandomPiece(){
        Piece p; 
        int pNumber = r.nextInt(7);
        switch (pNumber){
            case 0: 
                p = getIPiece();
                break;
            case 1:
                p = getJPiece();
                break;
            case 2:
                p = getLPiece();
                break;
            case 3:
                p = getOPiece();
                break;
            case 4:
                p = getSPiece();
                break;
            case 5:
                p = getTPiece();
                break;
            case 6:
                p = getZPiece();
                break;
            default: p = getIPiece();
        }
        return p;
    }
    
}
