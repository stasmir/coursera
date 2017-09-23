import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KdTree {
    private Node root = null;
    private int count = 0;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // number of points in the set
    public int size() {
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p");
        }

        if (root == null) {
            root = new Node();
            root.p = p;
            root.rect = new RectHV(0, 0, 1, 1);
            count++;
            return;
        }

        Node current = root;
        Node previous = null;
        boolean vertical = true;
        Comparator<Point2D> comparator = null;

        while (current != null) {
            if (current.p.equals(p)) {
                return;
            }

            previous = current;
            comparator = vertical ? Point2D.X_ORDER : Point2D.Y_ORDER;

            if (comparator.compare(current.p, p) > 0) {
                current = current.lb;
            } else {
                current = current.rt;
            }

            vertical = !vertical;
        }

        vertical = !vertical;
        Node node = new Node();
        node.p = p;

        if (comparator.compare(previous.p, p) > 0) {
            node.rect = vertical ?
                    new RectHV(previous.rect.xmin(), previous.rect.ymin(), previous.p.x(), previous.rect.ymax()):
                    new RectHV(previous.rect.xmin(), previous.rect.ymin(), previous.rect.xmax(), previous.p.y());
            previous.lb = node;
        } else {
            node.rect = vertical ?
                    new RectHV(previous.p.x(), previous.rect.ymin(), previous.rect.xmax(), previous.rect.ymax()):
                    new RectHV(previous.rect.xmin(), previous.p.y(), previous.rect.xmax(), previous.rect.ymax());
            previous.rt = node;
        }

        count++;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p");
        }

        Node current = root;
        boolean vertical = true;

        while (current != null) {
            if (current.p.equals(p)) {
                return true;
            }

            Comparator<Point2D> comp = vertical ? Point2D.X_ORDER : Point2D.Y_ORDER;

            if (comp.compare(current.p, p) > 0) {
                current = current.lb;
            } else {
                current = current.rt;
            }

            vertical = !vertical;
        }

        return false;
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.clear();

        draw(root, true);
    }

    private void draw(Node node, boolean vertical) {
        if (node != null) {
            draw(node.lb, !vertical);

            StdDraw.setPenRadius();
            if (vertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();

            draw(node.rt, !vertical);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rect");
        }

        List<Point2D> pointsInside = new ArrayList<>();

        range(root, rect, pointsInside);

        return pointsInside;
    }

    private void range(Node node, RectHV rect, List<Point2D> pointsInside) {
        if (node != null && rect.intersects(node.rect)) {
            if (rect.contains(node.p)) {
                pointsInside.add(node.p);
            }

            range(node.lb, rect, pointsInside);
            range(node.rt, rect, pointsInside);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p");
        }

        return nearest(root, p, null, true);
    }

    private Point2D nearest(Node node, Point2D p, Point2D min, boolean vertical) {
        if (node != null) {
            if (min == null) {
                min = node.p;
            }

            double minDist = min.distanceSquaredTo(p);
            if (node.rect.distanceSquaredTo(p) < minDist) {
                double nodeDist = node.p.distanceSquaredTo(p);
                if (nodeDist < minDist) {
                    min = node.p;
                }
                if (!vertical && node.p.y() > p.y()
                    || vertical && node.p.x() > p.x()) {
                    min = nearest(node.lb, p, min, !vertical);
                    min = nearest(node.rt, p, min, !vertical);
                } else {
                    min = nearest(node.rt, p, min, !vertical);
                    min = nearest(node.lb, p, min, !vertical);
                }
            }
        }

        return min;
    }

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
