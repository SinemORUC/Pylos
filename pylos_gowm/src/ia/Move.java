package ia;

import model.Position;

public class Move {
    public final Type move;
    public final Position placeAt;
    private Position[] removes;

    public Move(Type type, Position place, Position remove) {
        move = type;
        this.placeAt = place;
        if (remove != null) {
            removes = new Position[]{remove};
        }
    }

    public Move(Type type, Position place, Position[] removes) {
        move = type;
        this.placeAt = place;
        this.removes = removes;
    }

    public Position[] getRemoves() {
        return removes;
    }

    public enum Type {
        PLACE,
        MOUNT,
    }
}
