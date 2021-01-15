import controller.Controller;
import model.Model;
import model.Position;

public class Pylos {

    public static void main(String[] args) {
        Position.initialize();
        Controller.initialize();
        Controller.initTurn();
        //while (true)
//        Controller.initialize();
    }
}
