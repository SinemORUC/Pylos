package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Position {
    public static final int LEVELS = 4;
    public static final int BALLS = 30;

    private static final Position[][][] positions = new Position[LEVELS][][];
    private static final Position[] all = new Position[BALLS];
    private static Position top;
    private static Map<Position, List<List<Position>>> fourSquare = new HashMap<Position, List<List<Position>>>();
    private static Map<Position, List<List<Position>>> lines = new HashMap<Position, List<List<Position>>>();

    private final int x, y, z;

    // private ?
    private Position(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public static void initialize(){
        int all_index = 0;
        Position pos;
        for (int level=0; level < LEVELS; level++){
            positions[level] = new Position[LEVELS - level][LEVELS - level];
            for (int y = 0; y < LEVELS - level; y++){
                for (int x = 0; x < LEVELS - level; x++){
                    pos = new Position(x, y, level);
                    positions[level][y][x] = pos;
                    all[all_index++] = pos;
                }
            }
        }

        top = Position.at(0, 0, LEVELS - 1);

        for (Position position : all){
            fourSquare.put(position, position.fourSquare());
            lines.put(position, position.lines());
        }
    }

    public static Position at(int x, int y, int z){
        return positions[z][y][x];
    }

    public static boolean isValid(int x, int y, int z){
        return x >= 0 && y >= 0 && z >= 0 && x < LEVELS - z && y < LEVELS - z && z < LEVELS;
    }

    private List<Position> square() {
        List<Position> square = new LinkedList<Position>();

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
        List<List<Position>> fourSquare = new LinkedList<List<Position>>();
        List<Position> square;
        for (int x = this.x - 1; x <= this.x; x++) {
            for (int y = this.y - 1; y <= this.y; y++) {
                if (isValid(x, y, z)) {
                    square = at(x, y, z).square();
                    if (square != null)
                        fourSquare.add(square);
                }
            }
        }
        return fourSquare;
    }

    private List<List<Position>> lines() {
        List<List<Position>> lines = new LinkedList<List<Position>>();
        List<Position> line;

        line = new LinkedList<Position>();
        for (int x = 0; x < LEVELS - z; x++) {
            line.add(at(x, y, z));
        }
        lines.add(line);

        line = new LinkedList<Position>();
        for (int y = 0; y < LEVELS - z; y++) {
            line.add(at(x, y, z));
        }
        lines.add(line);

        if (onFirstDiagonal()) {
            line = new LinkedList<Position>();
            for (int xy = 0; xy < LEVELS - z; xy++) {
                if (isValid(xy, xy, z))
                    line.add(at(xy, xy, z));
            }
            lines.add(line);
        }

        if (onSecondDiagonal()) {
            line = new LinkedList<Position>();
            for (int xy = 0; xy < LEVELS - z; xy++) {
                if (isValid(xy, LEVELS - 1 - z - xy, z))
                    line.add(at(xy, LEVELS - 1 - z - xy, z));
            }
            lines.add(line);
        }

        return lines;
    }

    private boolean onSecondDiagonal() {
        return x + y == LEVELS - 1 - z;
    }

    private boolean onFirstDiagonal() {
        return x == y;
    }



}
