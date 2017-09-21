import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

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
    private SolveNode result;

    private class SolveNode implements Comparable<SolveNode> {
        private Board board;
        private SolveNode previous;
        private int moves;

        public SolveNode(Board board, SolveNode prev) {
            this.board = board;
            this.previous = prev;
            this.moves = prev == null ? 0 : prev.moves + 1;
        }

        public int compareTo(SolveNode other) {
            return this.board.manhattan() + this.moves
                    - other.board.manhattan() - other.moves;
        }
    }

    /**
     * Initializes a new solver.
     * Finds a solution to the initial board (using the A* algorithm).
     *
     * @param  initial board
     */
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("initial");

        MinPQ<SolveNode> pq = new MinPQ<>();
        pq.insert(new SolveNode(initial, null));

        MinPQ<SolveNode> twinPq = new MinPQ<>();
        twinPq.insert(new SolveNode(initial.twin(), null));

        while(true) {
            if (pq.isEmpty() || twinPq.isEmpty()) {
                return;
            }

            SolveNode current = pq.delMin();

            if (current.board.isGoal()) {
                result = current;
                return;
            }

            SolveNode twinCurrent = twinPq.delMin();
            if (twinCurrent.board.isGoal()) {
                return;
            }

            for (Board nextBoard : current.board.neighbors()) {
                if (current.previous == null || !nextBoard.equals(current.previous.board))
                    pq.insert(new SolveNode(nextBoard, current));
            }

            for (Board nextBoard : twinCurrent.board.neighbors()) {
                if (twinCurrent.previous == null || !nextBoard.equals(twinCurrent.previous.board))
                    twinPq.insert(new SolveNode(nextBoard, twinCurrent));
            }
        }
    }

    /**
     * Returns is the initial board solvable?
     *
     * @return is the initial board solvable?
     */
    public boolean isSolvable() {
        return result != null;
    }

    /**
     * Returns min number of moves to solve initial board; -1 if unsolvable
     *
     * @return min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }

        return result.moves;
    }

    /**
     * Returns sequence of boards in a shortest solution; null if unsolvable
     *
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }

        Deque<Board> solution = new ArrayDeque<>();
        SolveNode cur = result;

        while (cur != null) {
            solution.push(cur.board);
            cur = cur.previous;
        }

        return solution;
    }

    /**
     * Tests
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In("tests/8puzzle/puzzle02.txt");
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
