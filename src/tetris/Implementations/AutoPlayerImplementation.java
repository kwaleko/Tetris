package tetris.Implementations;

import static org.junit.Assert.fail;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Queue;

import tetris.autoplay.AutoPlayer;
import tetris.autoplay.AutoPlayer.Move;
import tetris.game.Board;
import tetris.game.GameObserver;
import tetris.game.TetrisGame;
import tetris.game.TetrisGameView;
import tetris.game.pieces.Piece;
import tetris.game.pieces.Point;

public class AutoPlayerImplementation implements AutoPlayer {

    private TetrisGameView game;

    private int best_rot;
    private int best_pos;
    private double best_score;

    private boolean rowsCompleted;
    private boolean gameOver;
    private boolean piecePositionChanged;
    private boolean piecelanded;
    private boolean firstPiece;
    private Queue<Move> moves;

    public AutoPlayerImplementation(TetrisGameView game) {
        this.game = game;
        this.firstPiece = true;
        this.best_score = -1000;
        // this.moves = new LinkedList<Move>() ;
        game.addObserver(this);
    }

    private int metricPoints(long newPoints, Long prevPoints) {
        int diff = (int) (newPoints - prevPoints);
        return diff;
    }

    private int metricHeight(Board board) {
        // average height of the game
        int height = board.getNumberOfColumns() * board.getNumberOfRows();
        for (int i = 0; i < board.getNumberOfColumns(); i++) {
            for (int row = 0; row < board.getNumberOfRows(); row++) {
                if (board.getBoard()[row][i] != null)
                    break;
                height--;
            }
        }
        return height;
    }

 
    private int[] heights(Board board) {
        int[] heights = new int[board.getNumberOfColumns()];
        ;
        for (int i = 0; i < board.getNumberOfColumns(); i++) {
            innerloop: for (int row = 0; row < board.getNumberOfRows(); row++) {
                if (board.getBoard()[row][i] != null) {
                    heights[i] = board.getNumberOfRows() - row;
                    break innerloop;
                }
            }
        }
        return heights;
    }

    private int peakHeights(Board board) {
        int pheight = 0 ; 
        ;
       col: for (int i = 0; i < board.getNumberOfColumns(); i++) {
             for (int row = 19; row > 0; row--) {
                if (board.getBoard()[row][i] != null) {
                    if(row>pheight){
                     pheight = row;
                    }
                }
            }
        }
        if(pheight >12)
        pheight = 1000;
        return pheight;
    }

    private int smothness(Board board) {
        int res = 0;
        int[] heights = heights(board);
        for (int i = 0; i < heights.length - 1; i++) {
            res += Math.abs(heights[i] - heights[i + 1]);
        }
        return res;
    }

    private int metricCreatedSpaces(Board board) {
        int spaces = 0;
        for (int col = 0; col < board.getNumberOfColumns(); col++) {
            for (int row = board.getNumberOfRows() - 1; row > 0; row--) {
                if (board.getBoard()[row][col] == null && board.getBoard()[row - 1][col] != null)
                    spaces++;
            }

        }
        return spaces;
    }

    private int nbOfHolesPos(Board board,int col){
        int spaces = 0;
     
            for (int row = board.getNumberOfRows() - 1; row > 0; row--) {
                if (board.getBoard()[row][col] == null && board.getBoard()[row - 1][col] != null)
                    spaces++;
            }

        if(spaces <=2) return 0;
        return spaces;
    }


    public void best_move() {
        if (game.getCurrentPieceCopy() == null)
            return;
        Queue<Move> seqMoves = new LinkedList<Move>();
        int col = game.getBoardCopy().getNumberOfColumns() / 2;
        int downSteps = 1;
        int nbCompletedRows =0;
        this.best_score = -1000;
        this.best_pos = 0;
        this.best_rot = 0;
        double score;
        rotationLoop : for (int rot = 0; rot < 4; rot++) {
            positionLoop: for (int pos = col * -1; pos < col; pos++) {
                TetrisGame simGame = new TetrisGameImplementation(game.getBoardCopy(), game.getCurrentPieceCopy(),
                game.getNextPieceCopy(), game.getPoints());
                for (int j = 0; j < rot; j++)
                    simGame.rotatePieceClockwise();
                boolean moved = true;
                if(pos > 0)
                    for(int i=0;i<pos;i++){
                        moved = simGame.moveRight();
                        if(!moved) continue positionLoop;

                    }
                if(pos <0 )
                    for(int i=0;i<Math.abs(pos);i++){
                        moved = simGame.moveLeft();
                        if(!moved) continue positionLoop;
                    }
                //simGame.addObserver(null);
                downSteps = 1;
                while (simGame.moveDown()) {
                    downSteps++;
                } 
                int colcurrLego = 5 +pos;

                 for(int nrot = 0;nrot<4;nrot++){
                    npositionloop:for(int npos = -5;npos<5;npos++){

                            
                    int metPoints = simGame.getNumberOfCompletedRows();// this.metricPoints(simGame.getPoints(), game.getPoints());
                    int metSmoothness = this.smothness(simGame.getBoard());
                    int metHeight =  this.metricHeight(simGame.getBoard());
                    int metSpaces =  this.metricCreatedSpaces(simGame.getBoard());
                    int deepWhole = this.nbOfHolesPos(simGame.getBoard(), colcurrLego);
                    int metPeakHeight = this.peakHeights(simGame.getBoard());
                    score =  // metPoints * 0.65433 * 1.4 +
                             metSmoothness * -0.6663898766 // -0.184483
                            + metSpaces * -0.999999 * 2.3999
                            + metHeight * -0.3399866 * 2.01222222;
                            //+ metPeakHeight * -0.88978777889 ;
                    if (score > this.best_score) {
                        this.best_score = score;
                        this.best_pos = pos;
                        this.best_rot = rot;
                    }

                    }
                }
            }
        }
        // after determining the best score
        while (!seqMoves.isEmpty())
            seqMoves.remove();
        for (int i = 0; i < this.best_rot; i++)
            seqMoves.add(Move.ROTATE_CW);
        for (int i = 0; i < Math.abs(this.best_pos); i++) {
            if (this.best_pos > 0)
                seqMoves.add(Move.RIGHT);
            if (this.best_pos < 0)
                seqMoves.add(Move.LEFT);
        }
        for (int i = 0; i <= downSteps; i++)
            seqMoves.add(Move.DOWN);
        this.moves = seqMoves;
    }

    @Override
    public Move getMove() {
        Move m =  Move.DOWN;
        if (this.gameOver)
            return null;

        if (this.piecelanded || firstPiece){

            this.best_move();
            m =moves.peek();
            moves.remove();
        }

        

        if (this.piecePositionChanged && moves.size() > 0){
            m = moves.peek();
            moves.remove();
        }
           

        this.firstPiece = false;
        this.piecePositionChanged = false;
        this.piecelanded = false;
        this.rowsCompleted = false;

     
        return m;
    }

    @Override
    public void rowsCompleted() {
        this.rowsCompleted = true;
    }

    @Override
    public void pieceLanded() {
        this.piecelanded = true;

    }

    @Override
    public void piecePositionChanged() {
        this.piecePositionChanged = true;

    }

    @Override
    public void gameOver() {
        this.gameOver = true;

    }

}
