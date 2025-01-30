package model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Game class contains methods for all game logic.
 * This class is the core of the program.
 * It makes computations for every playing turn.
 */
public class Game {
    int width = 20;
    int height = 15;
    int[][] gameMaze;
    int[][] gameMazeMask;
    Entity mouse;
    Entity cat1;
    Entity cat2;
    Entity cat3;
    int cheeseCollected;
    int cheeseNeeded;
    int[] currentMouseCoords;
    int[] currentCat1Coords;
    int[] currentCat2Coords;
    int[] currentCat3Coords;
    int[] currentCheeseCoords;

    public Game() {
        Maze maze = new Maze(width, height); // random maze generator
        gameMaze = maze.getMaze();
        gameMazeMask = maze.getEmptyMaze();

        mouse = new Entity("mouse");
        cat1 = new Entity("cat1");
        cat2 = new Entity("cat2");
        cat3 = new Entity("cat3");

        currentCheeseCoords = new int[]{0, 0};

        cheeseCollected = 0;
        cheeseNeeded = 5;

        currentMouseCoords = mouse.getCoords();
        gameMaze[currentMouseCoords[1]][currentMouseCoords[0]] = 2;
        gameMazeMask[currentMouseCoords[1]][currentMouseCoords[0]] = 1;
        unmaskSpaces(currentMouseCoords[0], currentMouseCoords[1]);
        currentCat1Coords = cat1.getCoords();
        currentCat2Coords = cat2.getCoords();
        currentCat3Coords = cat3.getCoords();
        cat1.setPrevCoords(cat1.getCoords());
        cat2.setPrevCoords(cat2.getCoords());
        cat3.setPrevCoords(cat3.getCoords());
        gameMaze[currentCat1Coords[1]][currentCat1Coords[0]] = 3;
        gameMaze[currentCat2Coords[1]][currentCat2Coords[0]] = 3;
        gameMaze[currentCat3Coords[1]][currentCat3Coords[0]] = 3;
        generateCheese();
        gameMaze[currentCheeseCoords[1]][currentCheeseCoords[0]] = 4;
    }

    public void refresh() {
        currentMouseCoords = mouse.getCoords();
        gameMaze[currentMouseCoords[1]][currentMouseCoords[0]] = 2;
        unmaskSpaces(currentMouseCoords[0], currentMouseCoords[1]);
        currentCat1Coords = cat1.getCoords();
        currentCat2Coords = cat2.getCoords();
        currentCat3Coords = cat3.getCoords();
        gameMaze[currentCat1Coords[1]][currentCat1Coords[0]] = 3;
        gameMaze[currentCat2Coords[1]][currentCat2Coords[0]] = 3;
        gameMaze[currentCat3Coords[1]][currentCat3Coords[0]] = 3;
        gameMaze[currentCheeseCoords[1]][currentCheeseCoords[0]] = 4;
    }

    public int analyzeTurn() {
        int[] mouseCoords = mouse.getCoords();
        List<int[]> catCoordsList = List.of(
                cat1.getCoords(), cat2.getCoords(), cat3.getCoords(),
                cat1.getPrevCoords(), cat2.getPrevCoords(), cat3.getPrevCoords()
        );

        boolean mouseCaught = catCoordsList.stream()
                .anyMatch(catCoords -> Arrays.equals(mouseCoords, catCoords));

        if (mouseCaught) {
            return -1;
        } else if (Arrays.equals(mouseCoords, currentCheeseCoords)) {
            cheeseCollected++;
            return 1;
        }

        return 0;
    }

    public void cheatCodes(String userInput) {
        if (userInput.equals("m")) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    gameMazeMask[y][x] = 1;
                }
            }
        } else if (userInput.equals("c")) {
            cheeseNeeded = 1;
        }
    }

    public int moveMouse(String direction) {
        switch (direction) {
            case "w" -> {
                int[] currentMouseCoords = mouse.getCoords();
                if (gameMaze[currentMouseCoords[1] - 1][currentMouseCoords[0]] != 1) {
                    mouse.moveEntity(currentMouseCoords[0], currentMouseCoords[1] - 1);
                    gameMaze[currentMouseCoords[1]][currentMouseCoords[0]] = 0;
                    moveCat(cat1);
                    moveCat(cat2);
                    moveCat(cat3);
                    return 0;
                } else {
                    return -1;
                }
            }
            case "a" -> {
                int[] currentMouseCoords = mouse.getCoords();
                if (gameMaze[currentMouseCoords[1]][currentMouseCoords[0] - 1] != 1) {
                    mouse.moveEntity(currentMouseCoords[0] - 1, currentMouseCoords[1]);
                    gameMaze[currentMouseCoords[1]][currentMouseCoords[0]] = 0;
                    moveCat(cat1);
                    moveCat(cat2);
                    moveCat(cat3);
                    return 0;
                } else {
                    return -1;
                }
            }
            case "s" -> {
                int[] currentMouseCoords = mouse.getCoords();
                if (gameMaze[currentMouseCoords[1] + 1][currentMouseCoords[0]] != 1) {
                    mouse.moveEntity(currentMouseCoords[0], currentMouseCoords[1] + 1);
                    gameMaze[currentMouseCoords[1]][currentMouseCoords[0]] = 0;
                    moveCat(cat1);
                    moveCat(cat2);
                    moveCat(cat3);
                    return 0;
                } else {
                    return -1;
                }
            }
            case "d" -> {
                int[] currentMouseCoords = mouse.getCoords();
                if (gameMaze[currentMouseCoords[1]][(currentMouseCoords[0]) + 1] != 1) {
                    mouse.moveEntity(currentMouseCoords[0] + 1, currentMouseCoords[1]);
                    gameMaze[currentMouseCoords[1]][currentMouseCoords[0]] = 0;
                    moveCat(cat1);
                    moveCat(cat2);
                    moveCat(cat3);
                    return 0;
                } else {
                    return -1;
                }
            }
        }
        return 0;
    }

    private int countCatMovement(int[] space) {
        int x = space[0];
        int y = space[1];
        int count = 0;
        // up
        if ((y - 1) > 0 && gameMaze[y - 1][x] != 1) {
            count++;
        }
        // left
        if ((x - 1) > 0 && gameMaze[y][x - 1] != 1) {
            count++;
        }
        // down
        if (((y + 1) < (height - 1)) && gameMaze[y + 1][x] != 1) {
            count++;
        }
        // right
        if (((x + 1) < (width - 1)) && gameMaze[y][x + 1] != 1) {
            count++;
        }
        return count;
    }

    private void moveCat(Entity cat) {
        String[] moves = {"w", "a", "s", "d"};
        String[] oppositeMoves = {"s", "d", "w", "a"};
        cat.setPrevCoords(cat.getCoords());
        int validDirections = countCatMovement(cat.getCoords());
        while (true) {
            int randomMove;
            String prohibitedNextMove;
            while (true) {
                Random random = new Random();
                randomMove = random.nextInt(4);
                prohibitedNextMove = cat.getProhibitedNextMove();
                if (validDirections == 1 || !moves[randomMove].equals(prohibitedNextMove)) {
                    cat.setProhibitedNextMove(oppositeMoves[randomMove]);
                    break;
                }
            }
            switch (moves[randomMove]) {
                case "w" -> {
                    int[] currentCatCoords = cat.getCoords();
                    if (gameMaze[currentCatCoords[1] - 1][currentCatCoords[0]] != 1) {
                        cat.moveEntity(currentCatCoords[0], currentCatCoords[1] - 1);
                        gameMaze[currentCatCoords[1]][currentCatCoords[0]] = 0;
                        return;
                    }
                    cat.setProhibitedNextMove(prohibitedNextMove);
                }
                case "a" -> {
                    int[] currentCatCoords = cat.getCoords();
                    if (gameMaze[currentCatCoords[1]][currentCatCoords[0] - 1] != 1) {
                        cat.moveEntity(currentCatCoords[0] - 1, currentCatCoords[1]);
                        gameMaze[currentCatCoords[1]][currentCatCoords[0]] = 0;
                        return;
                    }
                    cat.setProhibitedNextMove(prohibitedNextMove);
                }
                case "s" -> {
                    int[] currentCatCoords = cat.getCoords();
                    if (gameMaze[currentCatCoords[1] + 1][currentCatCoords[0]] != 1) {
                        cat.moveEntity(currentCatCoords[0], currentCatCoords[1] + 1);
                        gameMaze[currentCatCoords[1]][currentCatCoords[0]] = 0;
                        return;
                    }
                    cat.setProhibitedNextMove(prohibitedNextMove);
                }
                case "d" -> {
                    int[] currentCatCoords = cat.getCoords();
                    if (gameMaze[currentCatCoords[1]][currentCatCoords[0] + 1] != 1) {
                        cat.moveEntity(currentCatCoords[0] + 1, currentCatCoords[1]);
                        gameMaze[currentCatCoords[1]][currentCatCoords[0]] = 0;
                        return;
                    }
                    cat.setProhibitedNextMove(prohibitedNextMove);
                }
            }
        }
    }

    private void unmaskSpaces(int x, int y) {
        // top layer
        if (y - 1 > 0) {
            gameMazeMask[y - 1][x] = 1;
            if (x - 1 > 0) {
                gameMazeMask[y - 1][x - 1] = 1;
            }
            if (x + 1 < width) {
                gameMazeMask[y - 1][x + 1] = 1;
            }
        }
        // side layer
        if (x - 1 > 0) {
            gameMazeMask[y][x - 1] = 1;
        }
        if (x + 1 < width) {
            gameMazeMask[y][x + 1] = 1;
        }

        // bottom layer
        if (y + 1 < height) {
            gameMazeMask[y + 1][x] = 1;
            if (x - 1 > 0) {
                gameMazeMask[y + 1][x - 1] = 1;
            }
            if (x + 1 < width) {
                gameMazeMask[y + 1][x + 1] = 1;
            }
        }
    }

    public void generateCheese() {
        // reset cheese on the board
        if (currentCheeseCoords[0] != 0) {
            gameMaze[currentCheeseCoords[1]][currentCheeseCoords[0]] = 2;
        }

        Random random = new Random();
        while (true) {
            int x = random.nextInt(width - 2) + 1;

            int y = random.nextInt((height - 2) + 1);

            // cheese can be placed in empty space or cat space
            if (gameMaze[y][x] == 0 || gameMaze[y][x] == 3) {
                gameMaze[y][x] = 4;
                currentCheeseCoords[0] = x;
                currentCheeseCoords[1] = y;
                break;
            }
        }
    }

    public int[][] getMaze() {
        return gameMaze;
    }

    public int[][] getMazeMask() {
        return gameMazeMask;
    }

    public int getCheeseCollected() {
        return cheeseCollected;
    }

    public int getCheeseNeeded() {
        return cheeseNeeded;
    }

    public void loseGame() {
        gameMaze[currentMouseCoords[1]][currentMouseCoords[0]] = 9;
    }

    public void winGame() {
        gameMaze[currentMouseCoords[1]][currentMouseCoords[0]] = 8;
    }
}
