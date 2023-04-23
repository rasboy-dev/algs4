/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PercolationQuickFindLib {
    private int n;
    private int[][] grid;
    private int[] roots;
    private int top;
    private int bottom;
    private int numOpen;
    private QuickFindUF unionFind;

    private void validate(int row, int col) {
        if (row < 1 && n < row) {
            throw new IllegalArgumentException("row must be in range 1 to n");
        }
        if (col < 1 && n < col) {
            throw new IllegalArgumentException("col must be in range 1 to n");
        }
    }

    private void connect(int q, int p) {
        if (unionFind.find(q) == unionFind.find(p)) return;
        unionFind.union(q, p);
    }

    // creates n-by-n grid, with all sites initially blocked
    public PercolationQuickFindLib(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("side length must be a positive integer");
        }
        this.n = n;
        grid = new int[n][n];
        // roots = new int[n*n + 2];
        top = 0;
        bottom = n*n + 1;
        numOpen = 0;
        unionFind = new QuickFindUF(n*n + 2);

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                grid[i][j] = -1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (grid[row - 1][col - 1] == -1) {
            grid[row - 1][col - 1] = n * (row-1) + col;
            ++numOpen;
        }

        int node = grid[row - 1][col - 1];
        if (row == 1) {
            connect(node, top);
        } else {
            if (isOpen(row - 1, col)) {
                connect(node, grid[row - 2][col - 1]);
            }
        }
        if (row == n) {
            connect(node, bottom);
        } else {
            if (isOpen(row + 1, col)) {
                connect(node, grid[row][col - 1]);
            }
        }

        if (col > 1) {
            if (isOpen(row, col - 1)) {
                connect(node, grid[row - 1][col - 2]);
            }
        }
        if (col < n) {
            if (isOpen(row, col + 1)) {
                connect(node, grid[row - 1][col]);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1] != -1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen(row, col) &&
                  unionFind.find(grid[row - 1][col - 1]) == unionFind.find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionFind.find(top) == unionFind.find(bottom);
    }

        private void print() {
        StdOut.println(unionFind.find(top));
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // StdOut.print(grid[i][j]);
                StdOut.print(isOpen(i+1, j+1) ? unionFind.find(grid[i][j]) : -1);
                StdOut.print(" ");
            }
            StdOut.println();
        }
        StdOut.println(unionFind.find(bottom));
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        PercolationQuickFindLib percolation = new PercolationQuickFindLib(n);
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
