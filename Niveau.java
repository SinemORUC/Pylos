public class Niveau {
    Bille [][] nv;

    public Niveau(int nb){
        nv = new Bille[nb][nb];
        for (int i=0; i<nb; i++)
            for (int j=0; j<nb; j++)
                nv[i][j] = null;
    }

    public void ajouterBille(int i, int j){
        if (nv[i][j] == null){
            nv[i][j] = 
        }
    }
}
