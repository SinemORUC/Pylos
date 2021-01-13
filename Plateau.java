public class Plateau {
    Niveau [] niveaux = new Niveau[4];

    public Plateau(){
        for (int i=0; i<4; i++)
            niveaux[i] = new Niveau(4 - i);
    }

    public void ajouterBille(int nv, int i, int j){
        niveaux[nv].ajouterBille(i, j);
    }
}
