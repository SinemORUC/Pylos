package ia;

import model.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Move {
    public final Type move;
    public final Position placeAt;
    private final List<Position> removes = new ArrayList<>();

    public Move(Type type, Position place, Position remove) {
        move = type;
        this.placeAt = place;
        if (remove != null)
            removes.add(remove);
    }

    public Move(Type type, Position place, Position[] removes) {
        move = type;
        this.placeAt = place;
        this.removes.addAll(Arrays.asList(removes));
    }

    public List<Position> getRemoves() {
        return removes;
    }

    public void addRemove(Position p) {
        removes.add(p);
    }

    public enum Type {
        PLACE,
        MOUNT,
    }
}
