package controller;

import ia.IA;
import model.Ball;
import model.Model;
import model.Player;
import model.Position;

import java.util.List;
import java.util.Scanner;
//import view.View;//todo

public abstract class Controller {
//    static View view;//todo

    static int ballRemoved = 0;

    public static void initialize() {
//        Controller.view = view;
        Model.initialize(true);
        updateView();
    }

    public static void updateView() {
        System.out.println(Model.modelToString() + "\r");
    }

    public static void initTurn() {
        Player currentPlayer = Model.getCurrentPlayer();
        if (currentPlayer.allBallsOnBoard()) {
            nextTurn();
        } else {
            if (currentPlayer instanceof IA)
                ((IA) currentPlayer).play();
            placePlayerBall();
        }
    }

    public static void finishTurn() {
        if (Model.isWinner()) {
            updateView();
            Player winner = Model.getCurrentPlayer(), loser = winner.other();
            System.out.println(winner + " won");
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
        List<Ball> removables = Model.getCurrentPlayer().getRemovableBalls();
        Model.getBoard().toStringList(removables);
        Scanner scanner = new Scanner(System.in);
        if (removables.size() >= 2) {
            System.out.println("Remove 1 or 2 balls");
            int i;
            do {
                i = scanner.nextInt();
            } while (i < 1 || i > 2);

            for (int j = 0; j < i; j++) {
                System.out.println("Select");
                Position position = null;
                do {
                    System.out.println("Enter a level (1-4)");
                    int z = scanner.nextInt() - 1;
                    System.out.println("Enter a row (1- " + (Model.HEIGHT - z) + ")");
                    int y = scanner.nextInt() - 1;
                    System.out.println("Enter a column (1- " + (Model.HEIGHT - z) + ")");
                    int x = scanner.nextInt() - 1;
                    if (Position.isValid(x, y, z))
                        position = Position.at(x, y, z);
                } while (position == null || !removables.contains(position));
            }
        } else {
        }

    }

    public static void placeAIBall(Position position, Position[] removables, boolean mount) {
        Player currentPlayer = Model.getCurrentPlayer();
        currentPlayer.putBallOnBoard(position);
        // updateView();
        if (!mount)
            System.out.println("AI place a ball at : " + position);

        if (!currentPlayer.other().allBallsOnBoard() && (mount || currentPlayer.isSquare(position))) {
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

    public static void placePlayerBall() {
        Scanner scan = new Scanner(System.in);
        Position position = null;
        position = Position.at(1,1,0);
//        do {
//            System.out.println("Enter a level (1-4)");
//            int z = scan.nextInt() - 1;
//            System.out.println("Enter a row (1- " + (Model.HEIGHT - z) + ")");
//            int y = scan.nextInt() - 1;
//            System.out.println("Enter a column (1- " + (Model.HEIGHT - z) + ")");
//            int x = scan.nextInt() - 1;
//            if (Position.isValid(x, y, z))
//                position = Position.at(x, y, z);
//        } while (position == null || !Model.canPlaceBallAt(position));

        Player currentPlayer = Model.getCurrentPlayer();
        currentPlayer.putBallOnBoard(position);
        updateView();
        if (currentPlayer.isSquare(position) && !currentPlayer.other().allBallsOnBoard())
            removeBalls();
        else
            finishTurn();

    }

    public static void mountPlayerBall(Ball ball) {
        Model.getCurrentPlayer().mountBall(ball);
//        view.updatePositionsToMount(ball);//todo
        updateView();
        System.out.println("Choose where to place the ball");
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
