import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;

public class ProducteurConsommateur {
    private static final int TAILLE_BUFFER = 5;
    private final Queue<Integer> buffer = new LinkedList<>();

    // Déclaration des sémaphores
    private final Semaphore placesLibres = new Semaphore(TAILLE_BUFFER);
    private final Semaphore elementsDisponibles = new Semaphore(0);
    private final Semaphore mutexBuffer = new Semaphore(1);

    private int genererProduit() {
        return (int) (Math.random() * 100) + 1;
    }

    class Producteur implements Runnable {
        private final String nom;

        public Producteur(String nom) {
            this.nom = nom;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int produit = genererProduit();

                    // Attendre une place libre
                    placesLibres.acquire();

                    // Acquérir l'accès exclusif au buffer
                    mutexBuffer.acquire();

                    // Ajouter l'élément au buffer
                    buffer.add(produit);
                    System.out.println("[" + nom + "] Produit : " + produit + " | Taille buffer : " + buffer.size());

                    // Libérer l'accès au buffer
                    mutexBuffer.release();

                    // Signaler qu'un élément est disponible
                    elementsDisponibles.release();

                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println(nom + " interrompu : " + e.getMessage());
                }
            }
        }
    }

    class Consommateur implements Runnable {
        private final String nom;

        public Consommateur(String nom) {
            this.nom = nom;
        }

        @Override
        public void run() {
            // Passage de 7 à 10 (sinon buffer rempli)
            for (int i = 0; i < 10; i++) {
                try {
                    // Attendre qu'un élément soit disponible
                    elementsDisponibles.acquire();

                    // Acquérir l'accès exclusif au buffer
                    mutexBuffer.acquire();

                    // Retirer un élément du buffer
                    Integer produit = buffer.poll();
                    System.out.println("  [" + nom + "] Consommé : " + produit + " | Taille buffer : " + buffer.size());

                    // Libérer l'accès au buffer
                    mutexBuffer.release();

                    // Signaler qu'une place est libre
                    placesLibres.release();

                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    System.err.println(nom + " interrompu : " + e.getMessage());
                }
            }
        }
    }

    public Producteur createProducteur(String nom) {
        return new Producteur(nom);
    }

    public Consommateur createConsommateur(String nom) {
        return new Consommateur(nom);
    }
}