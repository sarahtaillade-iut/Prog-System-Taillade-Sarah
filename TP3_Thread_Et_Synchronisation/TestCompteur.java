public class TestCompteur {
    private static final int NB_THREADS = 10;
    private static final int NB_TESTS = 50;
    private static final int INCREMENTS_PAR_THREAD = 100_000;
    private static final int VALEUR_ATTENDUE = NB_THREADS * INCREMENTS_PAR_THREAD;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Test Compteur Dangereux ===");

        int minObserve = Integer.MAX_VALUE;
        int maxObserve = Integer.MIN_VALUE;
        int nbSucces = 0;

        for (int test = 1; test <= NB_TESTS; test++) {
            CompteurDangereux.resetCompteur();

            Thread[] threads = new Thread[NB_THREADS];

            for (int i = 0; i < NB_THREADS; i++) {
                threads[i] = new Thread(new CompteurDangereux.Incrementeur("Thread-" + i, INCREMENTS_PAR_THREAD));
                threads[i].start();
            }

            for (int i = 0; i < NB_THREADS; i++) {
                threads[i].join();
            }

            int obtenu = CompteurDangereux.getCompteur();
            System.out.println("Test " + test + " -> Attendu : " + VALEUR_ATTENDUE + " | Obtenu : " + obtenu + " | Perte : " + (VALEUR_ATTENDUE - obtenu));

            // Mise à jour des stats
            if (obtenu < minObserve) minObserve = obtenu;
            if (obtenu > maxObserve) maxObserve = obtenu;
            if (obtenu == VALEUR_ATTENDUE) nbSucces++;
        }

        // Affichage du bilan
        double tauxSucces = ((double) nbSucces / NB_TESTS) * 100;

        System.out.println("\n=== Statistiques ===");
        System.out.println("Succès : " + nbSucces + "/" + NB_TESTS + " (" + tauxSucces + "%)");
        System.out.println("Valeur minimale observée : " + minObserve);
        System.out.println("Valeur maximale observée : " + maxObserve);
    }
}