package model;

import java.util.*;

public class Position {
    public static final int HEIGHT = 4;
    public static final int BALLS = 30;

    public static final Position[] all = new Position[BALLS];
    private static final Position[][][] positions = new Position[HEIGHT][][];
    private static Position top;
    private static final Map<Position, List<List<Position>>> squares = new HashMap<>();
    private static final Map<Position, List<Position>> toMount = new HashMap<>();

    public final int x, y, z;

    private Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public static void initialize() {
        int all_index = 0;
        Position pos;
        for (int z = 0; z < HEIGHT; z++) {
            positions[z] = new Position[HEIGHT - z][HEIGHT - z];
            for (int y = 0; y < HEIGHT - z; y++) {
                for (int x = 0; x < HEIGHT - z; x++) {
                    pos = new Position(x, y, z);
                    positions[z][y][x] = pos;
                    all[all_index] = pos;
                    all_index++;
                }
            }
        }
        top = positions[HEIGHT - 1][0][0];
        for (Position position : all) {
            squares.put(position, position.fourSquare());
        }
    }

    public static Position at(int x, int y, int z) {
        return positions[z][y][x];
    }

    public static boolean isTop(Position p) {
        return p == top;
    }

    public static Position getTop() {
        return top;
    }

    public static List<Position> getToMount(Position p) {
        return toMount.get(p);
    }

    public static List<List<Position>> getSquares(Position p) {
        return squares.get(p);
    }

    public static boolean isValid(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < HEIGHT - z && y < HEIGHT - z && z < HEIGHT;
    }

    public static boolean isMiddle(int x, int y, int z) {
        if (z >= 2)
            return false;
        if (z == 1)
            return x == 1 && y == 1;
        return x > 0 && x < 3 && y > 0 && y < 3;
    }

    private List<Position> square() {
        List<Position> square = new ArrayList<>();

        for (int x = this.x; x <= this.x + 1; x++) {
            for (int y = this.y; y <= this.y + 1; y++) {
                if (!isValid(x, y, z))
                    return null;
                square.add(Position.at(x, y, z));
            }
        }
        return square;
    }

    private List<List<Position>> fourSquare() {
        List<List<Position>> squares = new ArrayList<>();
        List<Position> square;
        for (int ix = x - 1; ix <= x; ix++) {
            for (int iy = y - 1; iy <= y; iy++) {
                if (isValid(ix, iy, z)) {
                    square = at(ix, iy, z).square();
                    if (square != null) {
                        squares.add(square);
                        if (isValid(ix, iy, z + 1))
                            toMount.put(positions[z + 1][iy][ix], square);
                    }
                }
            }
        }
        return squares;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
