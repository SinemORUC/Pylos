package ia;

import controller.Controller;
import model.Board;
import model.Model;
import model.Player;
import model.Position;

import static ia.Move.Type.PLACE;

public class IA extends Player {

    public final static int DEPTH = 2;
    public final static int HEIGHT = 4;

    public IA() {
        super(-1);
    }

    public void play() {
        Status status = new Status(Model.getBoard(), this);//todo get board
        Node root = new Node(status);
        bestMove(root, DEPTH, Integer.MIN_VALUE);
        Move bestMove = root.getBest().move;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (bestMove.move == PLACE) {
            Controller.placeAIBall(bestMove.placeAt, bestMove.getRemoves(), false);
        } else {
            Controller.placeAIBall(bestMove.placeAt, bestMove.getRemoves(), true);
        }
    }

    public int bestMove(Node node, int depth, int max) {
        if (depth == 0 || node.status.isEnd())
            return node.status.evaluateStatus();
        node.createChildren();
        for (Node child : node.children) {
            max = maxScore(max, bestMove(child, depth - 1, max), node, child);
        }
        return max;
    }

    private int maxScore(int max, int bestMove, Node node, Node child) {
//        if (max == bestMove && Math.random() > 0.5) {
//            node.setBest(child);
//            return max;
//        }
        if (max < bestMove) {
            node.setBest(child);
            return bestMove;
        } else {
            return max;
        }

    }
}
