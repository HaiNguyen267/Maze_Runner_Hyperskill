package maze;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cell implements Serializable {

    public static final String WALL_SYMBOL = "\u2588\u2588";
    public static final String EMPTY_BLOCK_SYMBOL = "  ";
    public static final String ESCAPING_PATH_BLOCK_SYMBOL = "//";

    private String symbol;
    private boolean checked; // used in generate the maze
    private boolean visited; // used in finding the escaping path
    Cell north;
    Cell east;
    Cell west;
    Cell south;

    public Cell() {
        this.symbol = WALL_SYMBOL;
        this.checked = false;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isEmptyBlock() {
        return symbol.equals(EMPTY_BLOCK_SYMBOL);
    }

    public boolean isWallBlock() { return symbol.equals(WALL_SYMBOL);}

    public boolean isEscapingPathBlock() { return symbol.equals(ESCAPING_PATH_BLOCK_SYMBOL);}

    public boolean isRightBorder() {
        return this.east == null;
    }

    public boolean canBeEmptyBlock() {


        // if the cell has 2 null neighbor, it's at the corner, and if it's corner block, it cannot be empty block
        boolean atCorner = Stream.of(north, east, south, west)
                .filter(Objects::isNull)
                .count() >= 2;

        if (atCorner) {
            return false;
        }

        // if a cell both have west and east or both have north and south empty-block neighbors, it cannot be empty block
        //, because if it becomes empty block, it will create a loop
        boolean horizontalConnection = Stream.of(west, east).allMatch(n -> n != null && n.isEmptyBlock());
        boolean verticalConnection = Stream.of(north, south).allMatch(n -> n != null && n.isEmptyBlock());

        if (horizontalConnection || verticalConnection) {
            return false;
        }

        return true; // if everything is fine, the cell can be empty block
    }

    public List<Cell> getNeighbors() {
        return Stream.of(south, east, north, west)
                .collect(Collectors.toList());
    }

    public void setAsEmptyBlock()  {
        this.symbol = EMPTY_BLOCK_SYMBOL;
        this.checked = true;
    }

    public void setAsWallBlock() {
        this.symbol = WALL_SYMBOL;
        this.checked = true;
    }

    public void setAsEscapingPathBlock() {
        this.symbol = ESCAPING_PATH_BLOCK_SYMBOL;
    }

    public void visit() {
        this.visited = true;

    }

    public void setNorth(Cell cell) {
        this.north = cell;
    }

    public void setSouth(Cell cell) {
        this.south = cell;
    }

    public void setWest(Cell cell) {
        this.west = cell;
    }

    public void setEast(Cell cell) {
        this.east = cell;
    }

    @Override
    public String toString() {
        return symbol;
    }

}
