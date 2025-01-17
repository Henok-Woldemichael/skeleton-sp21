//I UNDERSTAND THIS CODE BASE INSIDE AND OUT! JUST LIKE I WAS SUPPOSED TO WITH THE DUNDERMIFLLIN PROJECT
package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */


    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;
        int emptyTileCount =0;
        //Tile lastSavedTile = null; CANNOT HAVE lastSavedTile be null because I'm checking the value
        Tile lastSavedTile = Tile.create(10,0,0);
        Tile h = lastSavedTile;
        int scoreTracker = this.score();
        this.board.setViewingPerspective(side);

        for (int i = 0; i < this.board.size(); i++) {
            //keeping track of rows
            emptyTileCount = 0;
            lastSavedTile = Tile.create(10,0,0);
            for (int j = this.size() - 1; j >= 0; j--)
            {
                if (this.board.tile(i, j) == null)
                {
                    emptyTileCount += 1;
                }
                else if(this.tile(i,j) != null)  {


                    if (this.tile(i, j).value() == lastSavedTile.value())
                    {
                        this.score += this.tile(i,j).value()*2;
                        scoreTracker += this.tile(i,j).value();
                        this.board.move(lastSavedTile.col(), lastSavedTile.row(), this.board.tile(i, j));
                        changed = true;
                        emptyTileCount += 1;
                        lastSavedTile = h;

                    }

                    else
                    {
                        lastSavedTile = Tile.create(this.board.tile(i,j).value(),i,j);
                        if (emptyTileCount > 0) {
                            this.board.move(i, j + emptyTileCount, this.tile(i, j));
                            lastSavedTile = Tile.create(this.board.tile(i,j+emptyTileCount).value(),i,j+emptyTileCount);
                            changed = true;

                        }
                    }



                }


                    }

                }
        this.board.setViewingPerspective(Side.NORTH);
        return changed;
            }



/*
                    if (this.board.tile(i, j - 1) != null)
                    {
                        if (this.board.tile(i, j - 1).value() == this.board.tile(i, j).value())
                        {
                            this.board.move(i, j, this.board.tile(i, j-1));
                            this.score += this.board.tile(i, j).value();
                            changed = true;
                        }
                    }
                    if (this.board.tile(i, j - 1) == null)
                    {

                        this.board.move(i, j + 1, this.board.tile(i, j));
                        changed = true;
                        Tile t = this.tile(i,j);
                        t.equals(t);
                    */
//top to bottom, incrementing empty tile count


    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {

        //keeping track of the columns
        for (int i = 0; i < b.size(); i++)
        {
            //keeping track of rows
            for (int j = 0; j < b.size(); j++)
            {
                if (b.tile(i,j) == null)
                {
                    return true;
                }

            }
        }

        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        //keeping track of the columns
        for (int row = 0; row < b.size(); row++)
        {
            //keeping track of rows
            for (int col = 0; col < b.size(); col++)
            {
                if (b.tile(col,row) != null && b.tile(col,row).value() == MAX_PIECE)
                {
                    return true;
                }

            }
        }

        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        int dataTracker;
        if(emptySpaceExists(b))
       {
           return true;
       }

       //keeping track of columns

        for (int row = 0; row < b.size(); row++)
        {
            //keeping track of rows
            for (int col = 0; col < b.size(); col++)
            {
               dataTracker = b.tile(col,row).value();
               if(col + 1 < b.size())
               {
                   //Seems to be that it is going out of range here...
                   //Solution may be to check both up and right, unless up or right is out of range...
                   if (b.tile(col + 1, row).value() == dataTracker)
                   {
                       return  true;
                   }

               }

               if(row + 1 < b.size())
                {
                    //Seems to be that it is going out of range here...
                    //Solution may be to check both up and right, unless up or right is out of range...
                    if (b.tile(col , row+1).value() == dataTracker)
                    {
                        return  true;
                    }

                }
            }

        }

        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
