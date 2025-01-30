package ui;

import model.Game;

import java.io.IOException;

/**
 * GameUI class provides the UI for the game.
 * It handles input/output from/to the user.
 */
public class GameUI {
    Game game;
    int width = 20;
    int height = 15;
    int[][] gameMaze;
    int[][] gameMazeMask;

    public void launch() throws IOException {

        displayWelcomeMessage();
        displayHelp();
        // initialize a new game
        game = new Game();
        gameMaze = game.getMaze();
        gameMazeMask = game.getMazeMask();
        printMaze();

        while (true) {
            System.out.println("Cheese collected: " + game.getCheeseCollected() + " of " + game.getCheeseNeeded());
            label:
            while (true) {
                System.out.println("Enter your move:");
                int userInput = System.in.read();
                System.in.read();
                String input = String.valueOf((char) userInput).toLowerCase();
                System.out.println();
                boolean blockedFlag = false;
                switch (input) {
                    case "m":
                    case "c":
                        game.cheatCodes(input);
                        break label;
                    case "?":
                        displayHelp();
                        break label;
                    case "w":
                    case "a":
                    case "s":
                    case "d":
                        int moveResult = game.moveMouse(input);
                        if (moveResult == 0) {
                            break label;
                        } else {
                            System.out.println("Invalid move, you can't move through walls!");
                            blockedFlag = true;
                        }
                    default:
                        if (!blockedFlag) {
                            System.out.println("Invalid move, enter W (up), A (down), S (left), D (right).");
                        }
                }
            }
            game.refresh();
            int turnResult = game.analyzeTurn();
            if (turnResult == -1) {
                game.loseGame();
                printMaze();
                System.out.println("Cheese collected: " + game.getCheeseCollected() + " of " + game.getCheeseNeeded());
                System.out.println("A cat ate you, you lose!");
                System.out.println();
                game.cheatCodes("m");
                printMaze();
                System.out.println("GAME OVER");
                break;

            } else if (turnResult == 1) {
                if (game.getCheeseCollected() == game.getCheeseNeeded()) {
                    game.winGame();
                    printMaze();
                    System.out.println("Cheese collected: " + game.getCheeseCollected() + " of " + game.getCheeseNeeded());
                    System.out.println("You win! Congrats!");
                    System.out.println();
                    game.cheatCodes("m");
                    printMaze();
                    System.out.println("GAME OVER");
                    break;
                }
                game.generateCheese();
                printMaze();
            } else {
                printMaze();
            }
        }
    }

    public void printMaze() {
        System.out.println("Maze:");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (gameMaze[y][x] == 1 && gameMazeMask[y][x] == 1) { // uncovered wall (borders will always show)
                    System.out.print("#");
                } else if (gameMaze[y][x] == 1 && gameMazeMask[y][x] == 0) { // masked wall
                    System.out.print(".");
                } else if (gameMaze[y][x] == 0 && gameMazeMask[y][x] == 1) { // uncovered empty space
                    System.out.print(" ");
                } else if (gameMaze[y][x] == 0 && gameMazeMask[y][x] == 0) { // masked empty space
                    System.out.print(".");
                } else if (gameMaze[y][x] == 2) { // mouse
                    System.out.print("@");
                } else if (gameMaze[y][x] == 3) { // cat
                    System.out.print("!");
                } else if (gameMaze[y][x] == 4) { // cheese
                    System.out.print("$");
                } else if (gameMaze[y][x] == 9) { // dead mouse
                    System.out.print("X");
                } else if (gameMaze[y][x] == 8) { // mouse win
                    System.out.print("@");
                }
            }
            System.out.println();
        }
    }

    public void displayWelcomeMessage() {
        System.out.println("----------------------------------------");
        System.out.println("Welcome to Cat and Mouse Maze Adventure!");
        System.out.println("By Seong Hyeon (Kevin) Park");
        System.out.println("----------------------------------------");
        System.out.println();
    }

    public void displayHelp() {
        System.out.println("GAME DIRECTIONS:");
        System.out.println("\tCollect 5 cheese before a cat eats you!");
        System.out.println("MAZE LEGEND:");
        System.out.println("\t#: Wall");
        System.out.println("\t@: You (the mouse)");
        System.out.println("\t!: Cat (the enemy)");
        System.out.println("\t$: Cheese");
        System.out.println("\t.: Unexplored space");
        System.out.println("MOVES:");
        System.out.println("\tPress enter after every move:");
        System.out.println("\tw: Move up");
        System.out.println("\ta: Move left");
        System.out.println("\ts: Move down");
        System.out.println("\td: Move right");
        System.out.println();
    }
}

