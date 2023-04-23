/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private boolean[][] grid;
    private final int top;
    private boolean[] isBottomConnected;
    private int numOpen;
    private final WeightedQuickUnionUF unionFind;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("side length must be a positive integer");
        }
        this.n = n;
        grid = new boolean[n][n];
        top = 0;
        isBottomConnected = new boolean[n*n + 1];
        numOpen = 0;
        unionFind = new WeightedQuickUnionUF(n*n + 2 + 1);

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                grid[i][j] = false;
                isBottomConnected[n * i + j + 1] = false;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (!grid[row - 1][col - 1]) {
            grid[row - 1][col - 1] = true;
            ++numOpen;
        }

        int node = n * (row - 1) + col;
        if (row == 1) {
            connect(node, top);
        } else {
            if (isOpen(row - 1, col)) {
                connect(node, id(row - 1, col));
            }
        }
        if (row == n) {
            isBottomConnected[unionFind.find(node)] = true;
        } else {
            if (isOpen(row + 1, col)) {
                connect(node, id(row + 1, col));
            }
        }

        if (col > 1) {
            if (isOpen(row, col - 1)) {
                connect(node, id(row, col - 1));
            }
        }
        if (col < n) {
            if (isOpen(row, col + 1)) {
                connect(node, id(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen(row, col) &&
                  unionFind.find(id(row, col)) == unionFind.find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return isBottomConnected[unionFind.find(top)];
    }

    private void validate(int row, int col) {
        if (row < 1 || n < row) {
            throw new IllegalArgumentException("row must be in range 1 to n");
        }
        if (col < 1 || n < col) {
            throw new IllegalArgumentException("col must be in range 1 to n");
        }
    }

    private void connect(int q, int p) {
        if (unionFind.find(q) == unionFind.find(p)) return;
        boolean bottomConnected =
                isBottomConnected[unionFind.find(q)] || isBottomConnected[unionFind.find(p)];
        unionFind.union(q, p);
        if (bottomConnected) {
            isBottomConnected[unionFind.find(p)] = true;
        }
    }

    private int id(int row, int col) {
        return n * (row-1) + col;
    }

    private void print() {
        StdOut.println(unionFind.find(top));
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                int row = i+1;
                int col = i+1;
                StdOut.print(isOpen(row, col) ? unionFind.find(id(row, col)) : "-");
                StdOut.print(" ");
            }
            StdOut.println();
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);
        while (!StdIn.isEmpty()) {
             int i = StdIn.readInt();
             int j = StdIn.readInt();
             percolation.open(i, j);
             // percolation.print();
        }

        percolation.print();
        StdOut.println(percolation.percolates());
    }
}
