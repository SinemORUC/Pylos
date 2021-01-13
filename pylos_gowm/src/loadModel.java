import com.jme3.scene.Spatial;

public class loadModel extends com.jme3.app.SimpleApplication{
    public static void main(String[] args){
        loadModel app= new loadModel();
        app.start();
    }

    public void simpleInitApp() {
        Spatial model= assetManager.loadModel("./ressources/models/board.obj");

        rootNode.attachChild(model);

    }



}
