import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    private final int n;
    private final int count;
    private final boolean[][] siteOpenStatuses;
    private final WeightedQuickUnionUF percolation;
    private int openCount = 0;

    public Percolation(int n) {              // create n-by-n siteOpenStatuses, with all sites blocked
        if (n <= 0)
            throw new IllegalArgumentException("n");
        
        this.n = n;
        count = n * n;
        siteOpenStatuses = new boolean[n][n];
        
        percolation = new WeightedQuickUnionUF(count + 2);
    }
       
    public    void open(int row, int col) {   // open site (row, col) if it is not open already
        if (isOpen(row, col)) {
            return;
        }

        int i = row - 1;
        int j = col - 1;

        siteOpenStatuses[i][j] = true;
        openCount++;

        
        int index = i * n + j + 1;
        
        if (i > 0 && siteOpenStatuses[i - 1][j]) {
            percolation.union(index, index - n);
        }

        if (i < n - 1 && siteOpenStatuses[i + 1][j]) {
            percolation.union(index, index + n);
        }

        if (j > 0 && siteOpenStatuses[i][j - 1]) {
            percolation.union(index, index - 1);
        }

        if (j < n - 1 && siteOpenStatuses[i][j + 1]) {
            percolation.union(index, index + 1);
        }
        
        if (i == 0) {
            percolation.union(0, index);
        }
        
        if (i == n - 1) {
            percolation.union(count + 1, index);
        }
    }
    
    public boolean isOpen(int row, int col) { // is site (row, col) open?
        checkIndicies(row, col);
        return siteOpenStatuses[row - 1][col - 1];
    }
    
    public boolean isFull(int row, int col) { // is site (row, col) full?
        checkIndicies(row, col);
        return percolation.connected(0, n * (row-1) + col);           
    }
    
    public     int numberOfOpenSites() {      // number of open sites
        return openCount;
    }
    
    public boolean percolates() {             // does the system percolate? 
        return percolation.connected(0, count + 1);
    }
    
    private void checkIndicies(int row, int col) {
        if (row < 1 || row > n) {
            throw new IllegalArgumentException("row");
        }

        if (col < 1 || col > n) {
            throw new IllegalArgumentException("col");
        }
    }
    
    public static void main(String[] args) {  // test client (optional)
        new Percolation(2);
    }
}