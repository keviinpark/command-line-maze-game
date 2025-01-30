package model;

import java.util.Objects;

/**
 * Entity class provides coordinate tracking for game entities.
 * Entities are initialized with default values.
 * This class also provides functionality for prohibition of
 * back-tracking for cat movements.
 */
public class Entity {
    private int[] coords;
    private String prohibitedNextMove = "n"; // only for cat
    private int[] prevCoords;

    public Entity(String type) {
        int boardWidth = 20;
        int boardHeight = 15;

        // mouse
        if (Objects.equals(type, "mouse")) {
            coords = new int[]{1, 1};
        }

        // cat 1
        else if (Objects.equals(type, "cat1")) {
            coords = new int[]{1, boardHeight - 2};
        }

        // cat 2
        else if (Objects.equals(type, "cat2")) {
            coords = new int[]{boardWidth - 2, 1};
        }

        // cat 3
        else if (Objects.equals(type, "cat3")) {
            coords = new int[]{boardWidth - 2, boardHeight - 2};
        }

    }

    public void moveEntity(int x, int y) {
        coords = new int[]{x, y};
    }

    public int[] getCoords() {
        return coords;
    }

    public String getProhibitedNextMove() {
        return prohibitedNextMove;
    }

    public void setProhibitedNextMove(String move) {
        this.prohibitedNextMove = move;
    }

    public int[] getPrevCoords() {
        return prevCoords;
    }

    public void setPrevCoords(int[] prevCoords) {
        this.prevCoords = prevCoords;
    }
}
