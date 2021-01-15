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
        System.out.print(Model.modelToString() + "\r");
    }

    public static void initTurn() {
        Player currentPlayer = Model.getCurrentPlayer();
        if (currentPlayer.allBallsOnBoard()) {
            nextTurn();
        } else {
            if (currentPlayer instanceof IA)
                ((IA) currentPlayer).play();
            else {
                List<Ball> mount = currentPlayer.getMountableBalls();
//                mount.removeIf(b -> !b.isMountableByCurrentPlayer()); // Useless
                if (mount.isEmpty())
                    placePlayerBall();
                else {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("There is a square of balls to mount !\n" +
                            "Would you like to MOUNT them ? [y/n]");
                    String answer = "";
                    do {
                        answer = scanner.next();
                    } while (!answer.equals("y") && !answer.equals("n"));
                    if (answer.equals("y")) {
//                        System.out.println(Model.getBoard().toStringList(mount));
                        mountPlayerBall();
                    }
                }
            }
        }
    }


    public static void finishTurn() {
        updateView();
        if (Model.isWinner()) {
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

    public static void placeAIBall(Position position, Position[] removables, boolean mount) {
        Player currentPlayer = Model.getCurrentPlayer();
        currentPlayer.putBallOnBoard(position);
        updateView();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                updateView();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void placePlayerBall() {
        Scanner scan = new Scanner(System.in);
        Position position = null;
        do {
            System.out.println("Enter a level (1-4)");
            int z = scan.nextInt() - 1;
            System.out.println("Enter a row (1- " + (Model.HEIGHT - z) + ")");
            int y = scan.nextInt() - 1;
            System.out.println("Enter a column (1- " + (Model.HEIGHT - z) + ")");
            int x = scan.nextInt() - 1;
            if (Position.isValid(x, y, z)) {
                position = Position.at(x, y, z);
            }
        } while (position == null || !Model.canPlaceBallAt(position));
        Player currentPlayer = Model.getCurrentPlayer();
        currentPlayer.putBallOnBoard(position);
        updateView();
        if (currentPlayer.isSquare(position) && !currentPlayer.other().allBallsOnBoard())
            removePlayerBalls();
    }

    private static void removePlayerBalls() {
        List<Ball> removables = Model.getCurrentPlayer().getRemovableBalls();
        Model.getBoard().toStringList(removables);
        Scanner scanner = new Scanner(System.in);
        if (!removables.isEmpty()) {
            System.out.println("Remove 1 or 2 balls : ");
            int i;
            do {
                i = scanner.nextInt();
            } while (i < 1 || i > 2);
            Ball b = null;
            for (int j = 0; j < i; j++) {
                if (j == 0)
                    System.out.println("Select first ball to remove : ");
                else
                    System.out.println("Select second ball to remove : ");
                Position position = null;
                do {
                    System.out.print("Enter a level (1-4) : ");
                    int z = scanner.nextInt() - 1;
                    System.out.print("Enter a row (1- " + (Model.HEIGHT - z) + ") : ");
                    int y = scanner.nextInt() - 1;
                    System.out.print("Enter a column (1- " + (Model.HEIGHT - z) + ") : ");
                    int x = scanner.nextInt() - 1;
                    if (Position.isValid(x, y, z)) {
                        position = Position.at(x, y, z);
                        b = Model.getBoard().ballAt(position);
                    }
                } while (position == null || !removables.contains(b));
                Model.getCurrentPlayer().removeBall(b);
                updateView();
            }
        }
    }

    private static void mountPlayerBall() {
        Scanner scan = new Scanner(System.in);
        Position position;
        Ball ball = null;
        List<Ball> removable = Model.getCurrentPlayer().getRemovableBalls();
        System.out.println("Select the ball to mount (move an upper level) : ");
        do {
            System.out.println("Enter a level (1-4)");
            int z = scan.nextInt() - 1;
            System.out.println("Enter a row (1- " + (Model.HEIGHT - z) + ")");
            int y = scan.nextInt() - 1;
            System.out.println("Enter a column (1- " + (Model.HEIGHT - z) + ")");
            int x = scan.nextInt() - 1;
            if (Position.isValid(x, y, z)) {
                position = Position.at(x, y, z);
                ball = Model.getBoard().ballAt(position);
            }
        } while (!removable.contains(ball));
        Model.getCurrentPlayer().mountBall(ball);
        updateView();
        placePlayerBall();
    }
}
