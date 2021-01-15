package ia;

import model.Board;
import model.Player;

import static ia.Move.Type.PLACE;

public class IA extends Player {

    public final static int DEPTH = 4;
    public final static int HEIGHT = 4;

    public IA() {
        super();
    }

    public void play() {
        Status status = new Status(new Board(), this);//todo get board
        Node root = new Node(status);
        bestMove(root, DEPTH, Integer.MIN_VALUE);
        Move bestMove = root.getBest().getMove();
        if (bestMove.getMove() == PLACE){
            System.out.println("WIN");
        } else {
            System.out.println("DOUBLE DRAGON");
        }
    }

    public int bestMove(Node node, int depth, int max) {
        if (depth == 0 || node.isEnd())
            return node.getStatus().evaluateStatus();
        node.createChildren();
        for (Node child : node.getChildren()) {
            max = max(max, bestMove(child, depth - 1, max), node, child);
        }
        return max;
    }

    private int max(int max, int bestMove, Node node, Node child) {
        if (max < bestMove) {
            node.setBest(child);
            return bestMove;
        } else {
            return max;
        }

    }
}
