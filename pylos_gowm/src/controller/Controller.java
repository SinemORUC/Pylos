package controller;

import ia.IA;
import model.Ball;
import model.Model;
import model.Player;
import model.Position;
//import view.View;//todo

public abstract class Controller {
//    static View view;//todo

    static int ballRemoved = 0;

    public static void initialize() {
//        Controller.view = view;
    }

    public static void updateView() {
        System.out.println(Model.modelToString() + "\r");
    }

    public static void initTurn() {
        Player currentPlayer = Model.getCurrentPlayer();
        currentPlayer.resetAction();
        ballRemoved = 0;
        updateView();
        if (currentPlayer.allBallsOnBoard()) {
            nextTurn();
        } else {
            setPlayerStatus("Place or mount a ball (right click)");
            if (currentPlayer instanceof IA)
                ((IA) currentPlayer).play();
        }
    }

    public static void finishTurn() {
        if (Model.isWinner()) {
            updateView();
            Player winner = Model.getCurrentPlayer(), loser = winner.other();
            System.out.println(winner + " won");
//            view.setStatus("Player " + winner + " won !\n Player " + loser + "lost :'(");//todo
        } else {
            nextTurn();
        }
    }

    private static void nextTurn() {
        Model.switchPlayers();
        initTurn();
    }

    private static void removeBalls() {
        Model.getCurrentPlayer().removeBalls();
        setPlayerStatus("Remove 1 or 2 balls (remove only one by a right click)");
    }

    private static void setPlayerStatus(String status) {
//        view.setStatus(Model.getCurrentPlayer());//todo
    }

    public static void placeAIBall(Position position, Position[] removables, boolean mount) {
        Player currentPlayer = Model.getCurrentPlayer();
        currentPlayer.putBallOnBoard(position);
        // updateView();
        if (!mount)
            System.out.println("AI place a ball at : " + position);

        if (!currentPlayer.other().allBallsOnBoard() && (mount || currentPlayer.anySquare(position))) {
            for (Position removable : removables) {
                if (mount) {
                    System.out.println("AI mount a ball from : " + removable + " to :" + position);
                    mount = false;
                } else
                    System.out.println("AI removes a ball at : " + removable);
                currentPlayer.removeBall(Model.getBoard().ballAt(removable));
            }
        }

    }

    public static void placePlayerBall(Position position) {
        Player currentPlayer = Model.getCurrentPlayer();
        currentPlayer.putBallOnBoard(position);
        updateView();
        if (currentPlayer.anySquare(position) && !currentPlayer.other().allBallsOnBoard())
            removeBalls();
        else
            finishTurn();
    }

    public static void mountPlayerBall(Ball ball) {
        Model.getCurrentPlayer().mountBall(ball);
//        view.updatePositionsToMount(ball);//todo
        updateView();
        setPlayerStatus("Choose where to place the ball");
    }

    public static void removePlayerBall(Ball ball, boolean lastRemoved) {
        Model.getCurrentPlayer().removeBall(ball);
        updateView();

        if (Model.getCurrentPlayer().getRemovableBalls().isEmpty())
            finishTurn();

        ballRemoved += 1;
        if (lastRemoved || ballRemoved == 2) // finished removing
            finishTurn();
    }
}