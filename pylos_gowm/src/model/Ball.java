package model;

public class Ball {
    private Position position;
    private final Player owner;
    private boolean onBoard = false;

    public Ball(Player owner) {
        this.owner = owner;
        Model.getBoard().balls.add(this);
    }

    public Position getPosition() {
        return position;
    }

    public boolean isOnBoard() {
        return onBoard;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Ball (" + owner.side + ", " + position + ")";
    }

    public static String ballToString(Ball b){
        if (b == null)
            return "   ";
        return b.owner.sideToString();
    }

    public void placeAt(Position position) {
        onBoard = true;
        this.position = position;
        System.out.println(owner + " place a ball at " + position);
    }

    public void removeFromBoard() {
        System.out.println(owner + " remove a ball at " + position);
        onBoard = false;
        // position = null; // Keep the position as it might be useful (e.g.: to mount it)
    }

    public boolean isRemovable() {
        if (!onBoard)
            return false;

        for (int x = position.x - 1; x <= position.x; x++) {
            for (int y = position.y - 1; y <= position.y; y++) {
                if (Position.isValid(x, y, position.z + 1) && Model.getBoard().anyBallAt(Position.at(x, y, position.z + 1)))
                    return false;
            }
        }
        return true;
    }

    public boolean isMountable() {
        return isRemovable() && !Model.getPositionsToMount(this).isEmpty();
    }

    public boolean isMountableByCurrentPlayer() {
        return owner == Model.getCurrentPlayer() && isMountable();
    }

    public boolean isRemovableByCurrentPlayer() {
        return owner == Model.getCurrentPlayer() && isRemovable();
    }
}
