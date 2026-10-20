public class CompteurDangereux {
    private static int compteurGlobal = 0;
    public static class Incrementeur implements Runnable {

        private final String nom;
        private final int nombreIncrements;

        public Incrementeur(String nom, int nombreIncrements) {
            this.nom = nom;
            this.nombreIncrements = nombreIncrements;
        }

        @Override
        public void run() {
            for (int i = 0; i < nombreIncrements; i++) {
                // Cette ligne pose problème car elle est pas atomique.
                // Elle lit puis ajoute 1 et puis elle réécrit.
                // PS Important : Ne pas oublier que : 
                // 2 threads pas synchro = lisent la même valeur
                //                       = s'écrasent tt les deux
                //                       = race condition et perd des données
                // Penser au mutex
                compteurGlobal++;  // RACE CONDITION ICI !
            }
        }
    }

    public static int getCompteur() {
        return compteurGlobal;
    }

    public static void resetCompteur() {
        compteurGlobal = 0;
    }
}