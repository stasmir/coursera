import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    java Solver
 *  Dependencies: none
 *
 *  An immutable data type for Solver of a puzzle.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
public class Solver {
    private int moves = 0;
    private boolean solvable = true;
    private final List<Board> solution = new ArrayList<>();

    /**
     * Initializes a new solver.
     * Finds a solution to the initial board (using the A* algorithm).
     *
     * @param  initial board
     */
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("initial");

        if (initial.isGoal())
            return;

        Board searchNode = initial;
        Board previous = null;
        MinPQ pq = new MinPQ();

        boolean solved = false;
        do {

            for (Board next : searchNode.neighbors()) {
                if (!next.equals(previous))
                    pq.insert(next);
            }

            previous = searchNode;
            searchNode = (Board) pq.delMin();


        }
        while (!solved);
    }

    /**
     * Returns is the initial board solvable?
     *
     * @return is the initial board solvable?
     */
    public boolean isSolvable() {
        return solvable;
    }

    /**
     * Returns min number of moves to solve initial board; -1 if unsolvable
     *
     * @return min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        return moves;
    }

    /**
     * Returns sequence of boards in a shortest solution; null if unsolvable
     *
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        return solution;
    }

    /**
     * Tests
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
