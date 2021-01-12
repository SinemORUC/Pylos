package model;

public class Ball {
    public Position position;
    public final Player owner;
    public boolean onBoard = false;

    public Ball(Player owner) {
        this.owner = owner;

    }

    //methode pour placer la bille
    public void placer(Position p){
        
    }
    //methode pour enlever la bille
    //methode pour verifier si c'est enlevable
    //methode pour verifier si on peut mettre dessus
    //methode pour verifier si on peut enlever dessus
}
