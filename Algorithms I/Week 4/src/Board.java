import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 *  Compilation:  javac Board.java
 *  Execution:    java Board
 *  Dependencies: none
 *
 *  An immutable data type for board of a puzzle.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
public class Board {
    private final int n;
    private final int[][] blocks;

    /**
     * Initializes a new board.
     * construct a board from an n-by-n array of blocks
     * (where blocks[i][j] = block in row i, column j)
     *
     * @param  blocks the blocks of the board
     */
    public Board(int[][] blocks) {
        n = blocks.length;
        this.blocks = copy(blocks);
    }

    private int[][] copy(int[][] source) {
        int[][] dest = new int[source.length][source.length];

        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source.length; j++) {
                dest[i][j] = source[i][j];
            }
        }

        return dest;
    }

    /**
     * Returns the size of the board.
     *
     * @return size of the board
     */
    public int dimension() {
        return n;
    }

    private int hammingScore = -1;

    /**
     * Returns the hamming meter of the board.
     *
     * @return number of blocks out of place
     */
    public int hamming() {
        if (hammingScore != -1) {
            return hammingScore;
        }

        int score = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int block = blocks[i][j];

                if (block != 0 && block != goal(i, j)) {
                    score++;
                }
            }
        }

        hammingScore = score;

        return score;
    }

    private int goal(int i, int j) {
        return i * n + j + 1;
    }

    private int goalRow(int block) {
        return (block - 1) / n;
    }

    private int goalCol(int block) {
        return (block - 1) % n;
    }

    private int manhattanScore = -1;

    /**
     * Returns the Manhattan meter of the board.
     *
     * @return sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
        if (manhattanScore != -1) {
            return manhattanScore;
        }

        int score = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int block = blocks[i][j];

                if (block != 0 && block != goal(i, j)) {
                    score += Math.abs(i - goalRow(block))
                            + Math.abs(j - goalCol(block));
                }
            }
        }

        manhattanScore = score;

        return score;
    }

    /**
     * Returns whether is this board the goal board?
     *
     * @return whether is this board the goal board?
     */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * Returns a twin board?
     *
     * @return a board that is obtained by exchanging any pair of blocks
     */
    public Board twin() {
        int[][] twin = copy(blocks);

        if (twin[0][0] != 0 && twin[0][1] != 0) {
            swap(twin, 0,0, 0, 1);
        } else {
            swap(twin, 1, 0, 1, 1);
        }

        return new Board(twin);
    }

    private void swap(int[][] source, int i1, int j1, int i2, int j2) {
        int tmp = source[i1][j1];
        source[i1][j1] = source[i2][j2];
        source[i2][j2] = tmp;
    }

    /**
     * Equals
     *
     * @return does this board equal y?
     */
    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (getClass() != y.getClass())
            return false;

        Board other = (Board) y;

        if (n != other.n)
            return false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != other.blocks[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns all neighbor boards
     *
     * @return all neighboring boards
     */
    public Iterable<Board> neighbors() {
        List<Board> boards = new ArrayList<>();

        Coord space = getSpaceCoord();


        if (space.row > 0) {
            int[][] copy = copy(blocks);
            swap(copy, space.row, space.col, space.row - 1, space.col);
            boards.add(new Board(copy));
        }

        if (space.row < n - 1) {
            int[][] copy = copy(blocks);
            swap(copy, space.row, space.col, space.row + 1, space.col);
            boards.add(new Board(copy));
        }

        if (space.col > 0) {
            int[][] copy = copy(blocks);
            swap(copy, space.row, space.col, space.row, space.col - 1);
            boards.add(new Board(copy));
        }

        if (space.col < n - 1) {
            int[][] copy = copy(blocks);
            swap(copy, space.row, space.col, space.row, space.col + 1);
            boards.add(new Board(copy));
        }

        return boards;
    }

    private class Coord {
        public Coord(int i, int j) {
            row = i;
            col = j;
        }

        public int row;
        public int col;
    }

    private Coord getSpaceCoord() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    return new Coord(i, j);
                }
            }
        }

        return null;
    }

    /**
     * Returns string representation of this board (in the output format specified below)
     *
     * @return string representation of this board (in the output format specified below)
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(n);
        sb.append('\n');

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * Tests
     */
    public static void main(String[] args) {
        int[][] blocks = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                blocks[i][j] = i * 3 + j + 1;
        Board b1 = new Board(blocks);
        Board b2 = new Board(blocks);

        StdOut.print(b1.equals(b2));

    }
}
