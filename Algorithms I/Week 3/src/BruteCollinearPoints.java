import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints
 *  Dependencies: none
 *
 *  Finds all line segments containing 4 points.
 *
 ******************************************************************************/
public class BruteCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    /**
     * Finds all line segments containing 4 points.
     *
     * @param  points Array of points to calculate
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("points at " + i);
            }

            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException("points at " + j);
                }
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException(
                            "Duplicates at " + i + " and " + j);
                }
            }
        }

        for (int p = 0; p < points.length - 3; p++) {
            for (int q = p + 1; q < points.length - 2; q++) {
                for (int r = q + 1; r < points.length - 1; r++) {
                    for (int s = r + 1; s < points.length; s++) {
                        Point pp = points[p];
                        Point pq = points[q];
                        Point pr = points[r];
                        Point ps = points[s];

                        if (pp.slopeTo(pq) == pp.slopeTo(pr)
                                && pp.slopeTo(pr) == pp.slopeTo(ps)) {
                            Point max = pp;
                            Point min = ps;

                            if (max.compareTo(pq) < 0) {
                                max = pq;
                            }

                            if (max.compareTo(pr) < 0) {
                                max = pr;
                            }

                            if (max.compareTo(ps) < 0) {
                                max = ps;
                            }

                            if (min.compareTo(pq) > 0) {
                                min = pq;
                            }

                            if (min.compareTo(pr) > 0) {
                                min = pr;
                            }

                            if (min.compareTo(pp) > 0) {
                                min = pp;
                            }

                            segments.add(new LineSegment(min, max));
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the number of line segments.
     *
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     * Returns the line segments.
     *
     * @return the line segments.
     */
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("out/Week 3/collinear/input8.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.01);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        StdDraw.setPenRadius(0.001);

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}