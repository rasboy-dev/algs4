/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PercolationMy {
    private final int n;
    private int[][] grid;
    private int[] roots;
    private int[] sizes;
    private final int top;
    private final int bottom;
    private int numOpen;

    // creates n-by-n grid, with all sites initially blocked
    public PercolationMy(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("side length must be a positive integer");
        }
        this.n = n;
        grid = new int[n][n];
        roots = new int[n*n + 2];
        sizes = new int[n*n + 2];
        top = 0;
        bottom = n*n + 1;
        numOpen = 0;

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                grid[i][j] = -1;
                sizes[n * i + j + 1] = 1;
                roots[n * i + j + 1] = n * i + j + 1;
            }
        }
        sizes[top] = 1;
        sizes[bottom] = 1;
        roots[top] = 0;
        roots[bottom] = bottom;
    } // Percolation

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
    } // open

    private void connect(int node1, int node2) {
        if (root(node1) == root(node2)) return;

        if (sizes[root(node1)] >= sizes[root(node2)]) {
            sizes[root(node1)] += sizes[root(node2)];
            roots[root(node2)] = root(node1);
        } else {
            sizes[root(node2)] += sizes[root(node1)];
            roots[root(node1)] = root(node2);
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
        return isOpen(row, col) && root(grid[row - 1][col - 1]) == root(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return root(top) == root(bottom);
    }

    private void validate(int row, int col) {
        if (row < 1 || n < row) {
            throw new IllegalArgumentException("row must be in range 1 to n");
        }
        if (col < 1 || n < col) {
            throw new IllegalArgumentException("col must be in range 1 to n");
        }
    }

    private int root(int node) {
        int componentRoot = roots[node];
        while (componentRoot != roots[componentRoot]) {
            componentRoot = roots[componentRoot];
        }
        int root = node;
        while (roots[root] != componentRoot) {
            roots[root] = componentRoot;
            root = roots[root];
        }
        return componentRoot;
    }

    private void print() {
        StdOut.println(root(top));
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // StdOut.print(grid[i][j]);
                StdOut.print(isOpen(i+1, j+1) ? root(grid[i][j]) : -1);
                StdOut.print(" ");
            }
            StdOut.println();
        }
        StdOut.println(root(bottom));
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        PercolationMy percolation = new PercolationMy(n);
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
