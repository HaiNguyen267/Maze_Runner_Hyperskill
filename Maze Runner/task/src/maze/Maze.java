package maze;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Maze{

    private final int ROWS;
    private final int COLS;
    private final Cell[][] GRID;
    private boolean foundExit;

    public Maze(int rows, int cols) {
        this.ROWS = rows;
        this.COLS = cols;
        this.GRID = new Cell[ROWS][COLS];
    }

    public void generate() {
        initGrid();

        Deque<Cell> queue = new ArrayDeque<Cell>();
        Cell entrance = chooseRandomEntrance();
        queue.offer(entrance);
        Cell current;

        while (!queue.isEmpty()) {
            current = queue.pollLast();

            if (!current.canBeEmptyBlock()) {
                current.setAsWallBlock();
            } else {
                // when the first cell of the right side is reached, it is set to empty block to represent the exit
                //, other cells in the right are set as be walls
                if (current.isRightBorder()) {
                    if (!foundExit) {
                        current.setAsEmptyBlock();
                        foundExit = true;
                        setRightWall();
                    }
                } else {
                    current.setAsEmptyBlock();
                    List<Cell> neighbors = current.getNeighbors();
                    // remove all null and visited neighbors
                    neighbors.removeIf(Objects::isNull);
                    neighbors.removeIf(Cell::isVisited);

                    // shuffle the list of unvisited neighbors
                    neighbors = shuffleList(neighbors);

                    // add unvisited neighbors to the queue in random order
                    for (Cell neighbor : neighbors) {
                        if (!queue.contains(neighbor)) {
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }
    }

    public void display() {
        for (Cell[] row : GRID) {
            for (Cell cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    private void initGrid() {
        // fill the grid will cells
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GRID[i][j] = new Cell();

                // set the left, upper, lower side and cells at even index to be walls
                // the right side will be set later
                if (j == 0 || i == 0 || i == ROWS - 1 || (i % 2 == 0 && j % 2 == 0)) {
                    GRID[i][j].setAsWallBlock();
                }
            }
        }

        // set neighbors for each cell
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // north
                if (i > 0) {GRID[i][j].setNorth(GRID[i-1][j]);}
                // south
                if (i < ROWS-1) {GRID[i][j].setSouth(GRID[i+1][j]);}
                // west
                if (j > 0) {GRID[i][j].setWest(GRID[i][j-1]);}
                // east
                if (j < COLS-1) {GRID[i][j].setEast(GRID[i][j+1]);}
            }
        }
    }

    private void setRightWall() {
        for (int i = 0; i < ROWS; i++) {
            if (!GRID[i][COLS-1].isEmptyBlock()) {
                GRID[i][COLS-1].setAsWallBlock();
            }
        }
    }

    private Cell chooseRandomEntrance() {
        // choose a random cell in the left side to set as the entrance
        // create a random index from 1 to ROWS - 2
        int randomIndex = (int) (Math.random() * (ROWS - 2) + 1);
        return GRID[randomIndex][0];
    }

    private List<Cell> shuffleList(List<Cell> list) {
        List<Cell> newList = new ArrayList<>();

        while (!list.isEmpty()) {
            int randomIndex = (int) (Math.random() * list.size());
            newList.add(list.get(randomIndex));
            list.remove(randomIndex);
        }

        return  newList;
    }
}
