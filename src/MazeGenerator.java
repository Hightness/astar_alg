package astar;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;

public class MazeGenerator {

    private Stack<MazeNode> stack = new Stack<>();
    private Random rand = new Random();
    public int[][] maze;
    private int dimension;

    public MazeGenerator(int dim) {
        maze = new int[dim][dim];
        dimension = dim;
    }

    public void generateMaze() {
        stack.push(new MazeNode(0,0));
        while (!stack.empty()) {
            MazeNode next = stack.pop();
            if (validNextNode(next)) {
                maze[next.y][next.x] = 1;
                ArrayList<MazeNode> neighbors = findNeighbors(next);
                randomlyAddNodesToStack(neighbors);
            }
        }
    }

    private boolean validNextNode(MazeNode node) {
        int numNeighboringOnes = 0;
        for (int y = node.y-1; y < node.y+2; y++) {
            for (int x = node.x-1; x < node.x+2; x++) {
                if (pointOnGrid(x, y) && pointNotNode(node, x, y) && maze[y][x] == 1) {
                    numNeighboringOnes++;
                }
            }
        }
        return (numNeighboringOnes < 3) && maze[node.y][node.x] != 1;
    }

    private void randomlyAddNodesToStack(ArrayList<MazeNode> nodes) {
        int targetIndex;
        while (!nodes.isEmpty()) {
            targetIndex = rand.nextInt(nodes.size());
            stack.push(nodes.remove(targetIndex));
        }
    }

    private ArrayList<MazeNode> findNeighbors(MazeNode node) {
        ArrayList<MazeNode> neighbors = new ArrayList<>();
        for (int y = node.y-1; y < node.y+2; y++) {
            for (int x = node.x-1; x < node.x+2; x++) {
                if (pointOnGrid(x, y) && pointNotCorner(node, x, y)
                        && pointNotNode(node, x, y)) {
                    neighbors.add(new MazeNode(x, y));
                }
            }
        }
        return neighbors;
    }

    private Boolean pointOnGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < dimension && y < dimension;
    }

    private Boolean pointNotCorner(MazeNode node, int x, int y) {
        return (x == node.x || y == node.y);
    }

    private Boolean pointNotNode(MazeNode node, int x, int y) {
        return !(x == node.x && y == node.y);
    }
}