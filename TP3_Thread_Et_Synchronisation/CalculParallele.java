public class CalculParallele {
    private static final int[] DONNEES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int MULTIPLICATEUR = 1000;

    public static void main(String[] args) {
        System.out.println("=== Calcul Parallèle ===");

        long debut = System.nanoTime();
        
        // Tableau pour garder la référence de chaque thread
        Calcule[] threads = new Calcule[DONNEES.length];

        // Instanciation des threads
        for (int i = 0; i < DONNEES.length; i++) {
            threads[i] = new Calcule(DONNEES[i], MULTIPLICATEUR, i);
        }

        // Démarrage de tous les threads en parallèle (start)
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        // Attente de la fin de l'exécution de tous les threads (join)
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.err.println("Le thread principal a été interrompu : " + e.getMessage());
            }
        }

        // Résultats
        long sommeTotal = 0;
        for (int i = 0; i < threads.length; i++) {
            sommeTotal += threads[i].getResultat();
        }

        long duree = (System.nanoTime() - debut) / 1_000_000;
        System.out.println("Résultat total : " + sommeTotal);
        System.out.println("Durée : " + duree + " ms");
    }
}