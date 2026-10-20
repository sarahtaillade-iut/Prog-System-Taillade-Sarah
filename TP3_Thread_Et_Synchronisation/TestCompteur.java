public class TestCompteur {
    private static final int NB_THREADS = 10;
    private static final int NB_TESTS = 10;
    private static final int INCREMENTS_PAR_THREAD = ;
    private static final int VALEUR_ATTENDUE = NB_THREADS = INCREMENTS_PAR_THREAD;

    public static void main(String[] args) {
        System.out.println("Test Compteur Dangereux");

        // Réinitialisation du compteur avant le test
        CompteurDangereux.resetCompteur();

        // Tableau pour conserver les instances des threads
        Thread[] threads = new Thread[NB_THREADS];

        // Création et démarrage de tous les threads
        for (int i = 0; i < NB_THREADS; i++) {
            Runnable task = new CompteurDangereux.Incrementeur("Thread-" + i, INCREMENTS_PAR_THREAD);
            threads[i] = new Thread(task);
            threads[i].start();
        }

        // Attente de la fin de l'exécution de chaque thread
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Thread interrompu : " + e.getMessage());
            }
        }

        // Calcul et affichage des résultats
        int attendu = NB_THREADS * INCREMENTS_PAR_THREAD;
        int obtenu = CompteurDangereux.getCompteur();

    }
}