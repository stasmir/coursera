import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Class that allows to create a matrix then allows to open sites
 * and then allows to determine percolation.
 */
public class Percolation {
    private final int size;
    private final boolean[][] openSites;
    private final WeightedQuickUnionUF percolation;
    private final WeightedQuickUnionUF fullness;
    private int openSitesCount = 0;
    private final int topVirtualSiteLinearCoordinate = 0;
    private final int bottomVirtualSiteLinearCoordinate;

    /**
     * Determines whether a matrix of a size of n percolates
     * @param n matrix size
     */
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n");
        
        size = n;
        openSites = new boolean[size][size];
        int sitesCount = size * size;
        bottomVirtualSiteLinearCoordinate = sitesCount + 1;

        percolation = new WeightedQuickUnionUF(sitesCount + 2); // + 2 virtual sites - top and bottom
        fullness = new WeightedQuickUnionUF(sitesCount + 1); // + 1 virtual top site
    }

    /**
     * Opens a site. If a site is already open then does nothing.
     * Row and col are from 1 to n
     * @param row site's row
     * @param col site's col
     */
    public void open(int row, int col) {
        checkIndicies(row, col);

        if (isOpen(row, col)) {
            return;
        }

        openSites[row - 1][col - 1] = true;
        openSitesCount++;

        
        int siteLinearCoordinate = convertMatrixToLinearCoordinate(row, col);
        
        if (row > 1 && isOpen(row - 1, col)) {
            int topNeighborLinearCoordinate = siteLinearCoordinate - size;

            percolation.union(siteLinearCoordinate, topNeighborLinearCoordinate);
            fullness.union(siteLinearCoordinate, topNeighborLinearCoordinate);
        }

        if (row < size && isOpen(row + 1, col)) {
            int bottomNeighborLinearCoordinate = siteLinearCoordinate + size;

            percolation.union(siteLinearCoordinate, bottomNeighborLinearCoordinate);
            fullness.union(siteLinearCoordinate, bottomNeighborLinearCoordinate);
        }

        if (col > 1 && isOpen(row, col - 1)) {
            int leftNeighborLinearCoordinate = siteLinearCoordinate - 1;

            percolation.union(siteLinearCoordinate, leftNeighborLinearCoordinate);
            fullness.union(siteLinearCoordinate, leftNeighborLinearCoordinate);
        }

        if (col < size && isOpen(row, col + 1)) {
            int leftNeighborLinearCoordinate = siteLinearCoordinate + 1;

            percolation.union(siteLinearCoordinate, leftNeighborLinearCoordinate);
            fullness.union(siteLinearCoordinate, leftNeighborLinearCoordinate);
        }
        
        if (row == 1) {
            percolation.union(siteLinearCoordinate, topVirtualSiteLinearCoordinate);
            fullness.union(siteLinearCoordinate, topVirtualSiteLinearCoordinate);
        }
        
        if (row == size) {
            percolation.union(bottomVirtualSiteLinearCoordinate, siteLinearCoordinate);
        }
    }

    /**
     * Determines whether a site is open or not
     * Row and col are from 1 to n
     * @param row site's row
     * @param col site's col
     * @return Is a site is open or not
     */
    public boolean isOpen(int row, int col) {
        checkIndicies(row, col);

        return openSites[row - 1][col - 1];
    }

    /**
     * Determines whether a site is full or not
     * Row and col are from 1 to n
     * @param row site's row
     * @param col site's col
     * @return Is a site is full or not
     */
    public boolean isFull(int row, int col) {
        checkIndicies(row, col);

        return fullness.connected(
                topVirtualSiteLinearCoordinate,
                convertMatrixToLinearCoordinate(row, col));
    }

    /**
     * Gets a number of open sites
     * @return Number of open sites
     */
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    /**
     * Determines whether a matrix percolates
     * @return Percolates or not
     */
    public boolean percolates() {
        return percolation.connected(
                topVirtualSiteLinearCoordinate,
                bottomVirtualSiteLinearCoordinate);
    }

    /**
     * Check indices bounds. Throws if illegal
     * Row and col are from 1 to n
     * @param row Row coordinate
     * @param col Col coordinate
     */
    private void checkIndicies(int row, int col) {
        if (row < 1 || row > size) {
            throw new IllegalArgumentException("row");
        }

        if (col < 1 || col > size) {
            throw new IllegalArgumentException("col");
        }
    }

    /**
     * Converts row and col coordinates to linear coordinate.
     * Linear coordinate (0) is for top virtual site and (sitesCount + 1) is for bottom virtual site
     * Row and col are from 1 to n
     * @param row Row coordinate
     * @param col Col coordinate
     * @return Linear coordinate
     */
    private int convertMatrixToLinearCoordinate(int row, int col) {
        return size * (row - 1) + col;
    }
    
    public static void main(String[] args) {
        new Percolation(2);
    }
}