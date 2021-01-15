package ia;

import model.Position;

import java.util.ArrayList;
import java.util.List;

import static ia.Move.Type.PLACE;
import static ia.Move.Type.MOUNT;

public class Node {
    private Status status;
    private final Move move;
    private Node best;
    private List<Node> children = new ArrayList<>();

    public Node(Status status) {
        this.status = status;
        move = null;
    }

    private Node(Status status, Move move) {
        this.status = status.makeMove(move);
        this.move = move;
    }

    public Status getStatus() {
        return status;
    }

    public Move getMove() {
        return move;
    }

    public Node getBest() {
        return best;
    }

    public void setBest(Node node) {
        best = node;
    }

    public List<Node> getChildren() {
        return children;
    }

    public boolean isEnd(){
        return status.isEnd();
    }

    public void createChildren() {
        List<Position> playables = status.getPlayables();
        for (Position p : playables) {
            if (Position.isTop(p)) {
                children.add(new Node(status, new Move(PLACE, p, (Position) null)));
                return;
            }
            if (status.squareOn(p)) {
                if (status.isMountable(p))
                    mountedSquareChildren(p);
                else
                    squareChildren(p);
            } else if (status.isMountable(p)) {
                mountedChildren(p);
            }
        }
    }

    public void mountedSquareChildren(Position p) {
        List<Position> removables = status.getRemovables();
        List<Position> toMount = Position.getToMount(p);
        Node child;
        for (Position remove : status.removablesToMount(p)) {
            if (removables.isEmpty()) {
                child = new Node(status, new Move(MOUNT, p, p));
                children.add(child);
            } else {
                for (Position rem : removables) {
                    for (Position r : removables) {
                        if (rem == remove || r == remove || (toMount.contains(rem) && r != p) || (toMount.contains(r) && rem != p))
                            continue;
                        if (rem != r) {
                            child = new Node(status, new Move(MOUNT, p, new Position[]{remove, rem, r}));
                        } else {
                            child = new Node(status, new Move(MOUNT, p, new Position[]{remove, rem}));
                        }
                        children.add(child);
                    }
                }
            }
        }
    }

    public void squareChildren(Position p) {
        List<Position> removables = status.getRemovables();
        Node child;
        if (removables.isEmpty()) {
            child = new Node(status, new Move(PLACE, p, p));
            children.add(child);
        } else {
            for (Position rem : removables) {
                for (Position r : removables) {
                    if (rem != r)
                        child = new Node(status, new Move(PLACE, p, new Position[]{r, rem}));
                    else
                        child = new Node(status, new Move(PLACE, p, new Position[]{r, p}));
                    children.add(child);
                }
            }
        }
    }

    public void mountedChildren(Position p) {
        List<Position> removables = status.removablesToMount(p);
        Node child;
        for (Position remove : removables) {
            child = new Node(status, new Move(MOUNT, p, remove));
            children.add(child);
        }
    }
}
