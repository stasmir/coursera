import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/******************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    java FastCollinearPoints
 *  Dependencies: none
 *
 *  Finds all maximal line segments containing 4 points.
 *
 ******************************************************************************/
public class FastCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    /**
     * Finds maximal line segments containing 4 points.
     *
     * @param  points Array of points to calculate
     */
    public FastCollinearPoints(Point[] points) {
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

        Point[] copyPoints = Arrays.copyOf(points, points.length);

        for (int p = 0; p < points.length; p++) {
            Arrays.sort(copyPoints, points[p].slopeOrder());
            Point pivot = points[p];

            List<Point> collinear = new ArrayList<>();
            double prevSlope = 0;

            for (int i = 1; i < copyPoints.length; i++) {
                double curSlope = pivot.slopeTo(copyPoints[i]);
                if (collinear.isEmpty() || curSlope == prevSlope) {
                    collinear.add(copyPoints[i]);
                } else {
                    if (collinear.size() > 2) {
                        collinear.add(pivot);
                        Collections.sort(collinear);
                        if (collinear.get(0) == pivot) {
                            segments.add(
                                    new LineSegment(pivot, collinear.get(collinear.size() - 1)));
                        }
                    }
                    collinear.clear();
                }
                prevSlope = curSlope;
            }
            if (collinear.size() > 2) {
                collinear.add(pivot);
                Collections.sort(collinear);
                if (collinear.get(0) == pivot) {
                    segments.add(
                            new LineSegment(pivot, collinear.get(collinear.size() - 1)));
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
        In in = new In("out/Week 3/collinear/vertical100.txt");
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
