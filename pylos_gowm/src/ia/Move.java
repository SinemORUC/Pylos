package ia;

import model.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Move {
    private final Type move;
    private final Position place;
    private List<Position> removes = new ArrayList<>();

    public Move(Type type, Position place, Position remove) {
        move = type;
        this.place = place;
        removes.add(remove);
    }

    public Move(Type type, Position place, Position[] removes) {
        move = type;
        this.place = place;
        this.removes.addAll(Arrays.asList(removes));
    }

    public Type getMove() {
        return move;
    }

    public Position getPlace() {
        return place;
    }

    public List<Position> getRemoves() {
        return removes;
    }

    public void addRemove(Position p){
        removes.add(p);
    }

    public enum Type {
        PLACE,
        MOUNT,
    }
}
