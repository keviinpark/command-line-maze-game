package model;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Maze class generates a random maze for gameplay
 * The maze adheres to assignment constraints.
 * CODE FOR GENERATING RANDOM MAZE WAS INITIALLY BASED OFF OF
 * ITERATIVE DFW WITH A STACK BUT IT WAS MODIFIED TO ADHERE
 * TO ASSIGNMENT CONSTRAINTS.
 */
public class Maze {
    int width;
    int height;
    int[][] mazeBoard;
    int[][] mazeBoardEmpty;
    int[][] visitedSpace;
    int[][] initialSpaces;
    int[][] endSpaces;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        initialSpaces = new int[][]{{1, 1}, {1, height - 2}, {width - 2, 1}, {width - 2, height - 2}};
        endSpaces = new int[][]{{width - 2, height - 2}, {width - 2, 1}, {1, height - 2}, {width - 2, height - 2}, {1, 1}};
        resetMaze();

        while (true) {
            System.out.println("Generating maze...");
            int removedWalls = 0;
            for (int i = 0; i < 4; i++) {
                removedWalls += generateMaze(new int[]{1, 1}, endSpaces[i]);
                removedWalls += generateMaze(endSpaces[i], new int[]{1, 1});
                removedWalls += generateMaze(new int[]{1, height - 2}, new int[]{9, 6});
                removedWalls += generateMaze(new int[]{width - 2, 1}, new int[]{9, 6});
                removedWalls += generateMaze(new int[]{width - 2, height - 2}, new int[]{9, 6});
            }
            removedWalls += removeRandomWalls();
            if (blankSpaceConnected() && catsCanMove() && !findBlankSquares() && !findSquares() && removedWalls > 150) {
                addLoops();
                System.out.println();
                break;
            }
            resetMaze();
        }
    }

    private void addLoops() {
        for (int y = 1; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                if ((x + 1) < width - 1 && (x + 2) < width - 1) {
                    if (mazeBoard[y][x] == 1 && mazeBoard[y][x + 1] == 1 && mazeBoard[y][x + 2] == 1) {
                        mazeBoard[y][x + 1] = 0;
                    }
                }
            }
        }
    }

    private boolean catsCanMove() {
        if (mazeBoard[height - 3][1] == 1 && mazeBoard[height - 2][2] == 1) {
            return false;
        }
        if (mazeBoard[1][width - 3] == 1 && mazeBoard[2][width - 2] == 1) {
            return false;
        }
        return mazeBoard[height - 2][width - 3] != 1 || mazeBoard[height - 3][width - 2] != 1;
    }

    private boolean blankSpaceConnected() {
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if ((x + 1) < width - 1 && (y + 1) < height - 1) {
                    if (mazeBoard[y][x] == 0 || mazeBoard[y][x + 1] == 0 ||
                            mazeBoard[y + 1][x] == 0 || mazeBoard[y + 1][x + 1] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean findBlankSquares() {
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if ((x + 1) < width - 1 && (y + 1) < height - 1) {
                    if (mazeBoard[y][x] == 0 && mazeBoard[y][x + 1] == 0 &&
                            mazeBoard[y + 1][x] == 0 && mazeBoard[y + 1][x + 1] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean findSquares() {
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if ((x + 1) < width - 1 && (y + 1) < height - 1) {
                    if (mazeBoard[y][x] == 1 && mazeBoard[y][x + 1] == 1 &&
                            mazeBoard[y + 1][x] == 1 && mazeBoard[y + 1][x + 1] == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void resetMaze() {
        this.mazeBoard = new int[height][width];
        this.mazeBoardEmpty = new int[height][width];
        this.visitedSpace = new int[height][width];
        IntStream.range(0, height).forEach(y ->
                IntStream.range(0, width).forEach(x -> {
                    mazeBoard[y][x] = 1;
                    if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                        //mazeBoard[y][x] = 1;
                        mazeBoardEmpty[y][x] = 1;
                        visitedSpace[y][x] = 1;
                    }
                })
        );
    }

    private int removeRandomWalls() {
        AtomicInteger removedWalls = new AtomicInteger();
        IntStream.range(0, height).forEach(y ->
                IntStream.range(0, width).forEach(x -> {
                    Random rand = new Random();
                    if ((y > 0 && y < height - 1) && (x > 0 && x < width - 1)) {
                        int randomInt = rand.nextInt(2);
                        if (randomInt == 1) {
                            if (countVisitedNeighbours(new int[]{x, y}) < 1) {
                                mazeBoard[y][x] = 0;
                                removedWalls.getAndIncrement();
                            }
                        }
                    }
                })
        );
        return removedWalls.get();
    }

    private int generateMaze(int[] initialSpace, int[] endSpace) {
        int removedWalls = 0;
        visitedSpace[initialSpace[1]][initialSpace[0]] = 0;
        mazeBoard[initialSpace[1]][initialSpace[0]] = 0;
        Stack<int[]> stack = new Stack<>();
        stack.push(initialSpace);
        while (!stack.isEmpty()) {
            int[] space = stack.pop();
            if (Arrays.equals(space, endSpace)) {
                break;
            }
            ArrayList<int[]> unvisitedNeighbors = getUnvisitedNeighbours(space);
            if (!unvisitedNeighbors.isEmpty()) {
                stack.push(space);
                Random random = new Random();
                int[] neighbor = unvisitedNeighbors.get(random.nextInt(unvisitedNeighbors.size()));
                mazeBoard[neighbor[1]][neighbor[0]] = 0;
                removedWalls++;
                visitedSpace[neighbor[1]][neighbor[0]] = 1;
                stack.push(neighbor);
            }
        }
        return removedWalls;
    }

    private ArrayList<int[]> getUnvisitedNeighbours(int[] space) {
        int x = space[0];
        int y = space[1];
        ArrayList<int[]> neighbours = new ArrayList<>();

        // up
        if ((y - 1) > 0 && visitedSpace[y - 1][x] == 0 && countVisitedNeighbours(new int[]{x, y - 1}) < 2) {
            neighbours.add(new int[]{x, y - 1});
        }
        // left
        if ((x - 1) > 0 && visitedSpace[y][x - 1] == 0 && countVisitedNeighbours(new int[]{x - 1, y}) < 2) {
            neighbours.add(new int[]{x - 1, y});
        }
        // down
        if (((y + 1) < (height - 1)) && visitedSpace[y + 1][x] == 0 && countVisitedNeighbours(new int[]{x, y + 1}) < 2) {
            neighbours.add(new int[]{x, y + 1});
        }
        // right
        if (((x + 1) < (width - 1)) && visitedSpace[y][x + 1] == 0 && countVisitedNeighbours(new int[]{x + 1, y}) < 2) {
            neighbours.add(new int[]{x + 1, y});
        }
        return neighbours;
    }

    private int countVisitedNeighbours(int[] space) {
        int x = space[0];
        int y = space[1];
        int count = 0;
        // up
        if ((y - 1) > 0 && visitedSpace[y - 1][x] == 1) {
            count++;
        }
        // left
        if ((x - 1) > 0 && visitedSpace[y][x - 1] == 1) {
            count++;
        }
        // down
        if (((y + 1) < (height - 1)) && visitedSpace[y + 1][x] == 1) {
            count++;
        }
        // right
        if (((x + 1) < (width - 1)) && visitedSpace[y][x + 1] == 1) {
            count++;
        }
        return count;
    }

    public int[][] getMaze() {
        return mazeBoard;
    }

    public int[][] getEmptyMaze() {
        return mazeBoardEmpty;
    }
}
