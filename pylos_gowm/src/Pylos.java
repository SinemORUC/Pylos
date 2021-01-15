import controller.Controller;
import model.Model;
import model.Position;

public class Pylos {

    public static void main(String[] args) {
        Position.initialize();
        Model.initialize();
        Controller.updateView();
        //while (true);
//        Controller.initialize();
    }
}
