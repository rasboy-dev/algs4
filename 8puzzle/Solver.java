/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private final boolean solvable;

    private final int moves;

    private final SearchNode goalSearchNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Constructor argument is null");
        }
        Board initialTwin = initial.twin();

        final MinPQ<SearchNode> priorityQueue = new MinPQ<>();
        final MinPQ<SearchNode> alternativePriorityQueue = new MinPQ<>();

        priorityQueue.insert(new SearchNode(initial, 0, null));
        alternativePriorityQueue.insert(new SearchNode(initialTwin, 0, null));

        SearchNode goalSearchNode = null;
        boolean solvable = false;
        int moves = 0;

        while (!priorityQueue.isEmpty() && !alternativePriorityQueue.isEmpty()) {
            final SearchNode node = process(priorityQueue);
            if (node.board.isGoal()) {
                goalSearchNode = node;
                solvable = true;
                moves = node.moves;
                break;
            }

            final SearchNode alternativeNode = process(alternativePriorityQueue);
            if (alternativeNode.board.isGoal()) {
                solvable = false;
                moves = -1;
                break;
            }
        }

        this.goalSearchNode = goalSearchNode;
        this.solvable = solvable;
        this.moves = moves;
    }

    private static SearchNode process(MinPQ<SearchNode> priorityQueue) {
        SearchNode node = priorityQueue.delMin();
        Board previousBoard = node.previous != null ? node.previous.board : null;
        for (Board neighbor : node.board.neighbors()) {
            if (!neighbor.equals(previousBoard))
                priorityQueue.insert(new SearchNode(neighbor, node.moves + 1, node));
        }
        return node;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }
        Stack<Board> stack = new Stack<>();
        SearchNode node = goalSearchNode;
        while (node != null) {
            stack.push(node.board);
            node = node.previous;
        }
        return stack;
    }

    private static class SearchNode implements Comparable<SearchNode> {

        private final Board board;

        private final int moves;

        private final SearchNode previous;

        private final int priority;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;

            int distance = board.manhattan();
            // int distance = board.hamming();
            this.priority = distance + moves;
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    // test client
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);

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
