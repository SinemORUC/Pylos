package ia;

import controller.Controller;
import model.Board;
import model.Model;
import model.Player;
import model.Position;

import static ia.Move.Type.PLACE;

public class IA extends Player {

    public final static int DEPTH = 1;
    public final static int HEIGHT = 4;

    public IA() {
        super(-1);
    }

    public void play() {
        Status status = new Status(Model.getBoard(), this);//todo get board
        Node root = new Node(status);
        bestMove(root, DEPTH, Integer.MIN_VALUE);
        Move bestMove = root.getBest().getMove();
        if (bestMove.move == PLACE){
            Controller.placeAIBall(bestMove.placeAt, (Position[]) bestMove.getRemoves().toArray(), false);
        } else {
            Controller.placeAIBall(bestMove.placeAt, (Position[]) bestMove.getRemoves().toArray(), true);
        }
        Controller.finishTurn();
    }

    public int bestMove(Node node, int depth, int max) {
        if (depth == 0 || node.isEnd())
            return node.status.evaluateStatus();
        node.createChildren();
        for (Node child : node.children) {
            max = maxScore(max, bestMove(child, depth - 1, max), node, child);
        }
        return max;
    }

    private int maxScore(int max, int bestMove, Node node, Node child) {
        if (max < bestMove) {
            node.setBest(child);
            return bestMove;
        } else {
            return max;
        }

    }
}
