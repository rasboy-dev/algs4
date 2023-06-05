/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

public class Board {

    private final int[][] board;
    private int dimension;
    private int emptyCol;
    private int emptyRow;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.dimension = tiles.length;
        this.board = new int[dimension][dimension];
        int maxTile = dimension * dimension - 1;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int tile = tiles[i][j];

                if (tile < 0 || tile > maxTile)
                    throw new IllegalArgumentException(
                            "tile " + tile + " is not in the range 0 .. n^2 - 1");

                board[i][j] = tile;
                if (tile == 0) {
                    emptyRow = i;
                    emptyCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(String.format("%d%n", dimension));
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                string.append(board[i][j]);
                if (j < dimension - 1)
                    string.append(" ");
                else if (i < dimension - 1)
                    string.append(System.lineSeparator());
            }
        }
        return string.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dimension; i++) {
            int firstInRow = i * dimension + 1;
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] != 0 && board[i][j] != firstInRow + j) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dimension; i++) {
            int firstInRow = i * dimension + 1;
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] != 0 && board[i][j] != firstInRow + j) {
                    int goalRow = (board[i][j] - 1) / dimension;
                    int goalColumn = (board[i][j] - 1) % dimension;
                    manhattan += Math.abs(goalRow - i) + Math.abs(goalColumn - j);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        // TODO consider using equals and comparing with goal of size dim*dim
        for (int i = 0; i < dimension; i++) {
            int firstInRow = i * dimension + 1;
            for (int j = 0; j < dimension; j++) {
                boolean isBottomRight = i == dimension - 1 && j == dimension - 1;
                if (!isBottomRight)
                    if (board[i][j] != firstInRow + j)
                        return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board that;
        if (y.getClass() == Board.class) {
            that = (Board) y;
        }
        else {
            return false;
        }
        if (this.dimension != that.dimension)
            return false;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (this.board[i][j] != that.board[i][j])
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();
        if (emptyCol > 0) {
            neighbors.push(neigbor(emptyRow, emptyCol - 1));
        }
        if (emptyCol < dimension - 1) {
            neighbors.push(neigbor(emptyRow, emptyCol + 1));
        }
        if (emptyRow > 0) {
            neighbors.push(neigbor(emptyRow - 1, emptyCol));
        }
        if (emptyRow < dimension - 1) {
            neighbors.push(neigbor(emptyRow + 1, emptyCol));
        }
        return neighbors;
    }

    private Board neigbor(int newEmptyRow, int newEmptyCol) {
        assert Math.abs(newEmptyCol - emptyCol) == 1 ^ Math.abs(newEmptyRow - emptyRow) == 1;
        int[][] tiles = copyArray(board);
        swap(tiles, emptyRow, emptyCol, newEmptyRow, newEmptyCol);
        return new Board(tiles);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] tiles = copyArray(board);
        int i = 0, j = 0;
        if (tiles[i][j] == 0)
            j = dimension - 1;
        int i1 = dimension - 1, j1 = 0;
        if (tiles[i1][j1] == 0)
            j1 = dimension - 1;
        swap(tiles, i, j, i1, j1);
        return new Board(tiles);
    }

    private static int[][] copyArray(int[][] arr) {
        int[][] copy = new int[arr.length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                copy[i][j] = arr[i][j];
            }
        }
        return copy;
    }

    private static void swap(int[][] arr, int i, int j, int l, int k) {
        arr[i][j] += arr[l][k];
        arr[l][k] = arr[i][j] - arr[l][k];
        arr[i][j] = arr[i][j] - arr[l][k];
    }

    // unit testing
    public static void main(String[] args) {
        int[][] tiles1 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        int[][] tiles2 = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 } };
        int[][] tiles3 = { { 4, 5, 6 }, { 1, 2, 3 }, { 7, 8, 0 } };
        int[][] tiles4 = { { 1, 3, 2 }, { 5, 4, 6 }, { 8, 7, 0 } };
        int[][] tiles5 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] tiles2x2 = { { 1, 3 }, { 0, 2 } };

        Board board1 = new Board(tiles1);
        Board board2 = new Board(tiles2);
        Board board3 = new Board(tiles3);
        Board board3Copy = new Board(tiles3);
        Board board4 = new Board(tiles4);
        Board board5 = new Board(tiles5);
        Board board2x2 = new Board(tiles2x2);

        debug(board1, "Board 1");
        debug(board2, "Board 2");
        debug(board3, "Board 3");
        debug(board4, "Board 4");
        debug(board5, "Board 5");
        debug(board2x2, "Board 2x2");

        System.out.println("board2 equals board2 = " + board2.equals(board2) + ", expected true");
        System.out.println(
                "board3 equals board3Copy = " + board3.equals(board3Copy) + ", expected true");
        System.out.println("board2 equals board3 = " + board2.equals(board3) + ", expected false");
        System.out.println(
                "board2x2 equals board1 = " + board2x2.equals(board1) + ", expected false");
    }

    private static void debug(Board board, String boardName) {
        System.out.println(boardName + ":");
        System.out.println(board);
        System.out.printf("%s hamming distance is %d%n", boardName, board.hamming());
        System.out.printf("%s manhattan distance is %d%n", boardName, board.manhattan());
        System.out.println("is goal? " + board.isGoal());
        System.out.println();
    }
}
