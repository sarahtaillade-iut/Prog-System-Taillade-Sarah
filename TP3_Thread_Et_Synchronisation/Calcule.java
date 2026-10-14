public class Calcule extends Thread {

    public int valeur;
    public int multiplicateur;
    public int index;
    public long resultat;

    public Calcule (int valeur, int multiplicateur, int index) {
        this.valeur = valeur;
        this.multiplicateur = multiplicateur;
        this.index = index;
    }

    @Override
    public void run() {
        // Calcul intensif simulé par des opérations mathématiques
        long resultat = 0;
        for (int j = 0; j < multiplicateur; j++) {
            resultat += valeur * valeur + valeur;
                            // on pourrait aussi mettre un sleep
        }

        // System.out.println("Traitement de " + valeur + " terminé : " + resultat);
    }

    public getResultat() {
        return resultat;
    }
}