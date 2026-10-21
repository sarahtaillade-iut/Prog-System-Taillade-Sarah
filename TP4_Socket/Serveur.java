import java.net.*;
import java.io.*;

public class Serveur {
    public static void main(String[] args) throws Exception {
        // Création du serveur et liaison au port 5000
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Serveur démarré sur le port 5000");

        try {
            while(true) {
                // Acceptation d'une connexion cliente
                Socket client = serverSocket.accept();
                System.out.println("Client connecté : " + client.getInetAddress());

                // Création des canaux input/output
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream())
                );
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                // Boucle d'écho (lecture/écriture)
                String ligne;
                while ((ligne = in.readLine()) != null) {
                    System.out.println("Reçu : " + ligne);
                    out.println("ECHO: " + ligne);
                }

                // Fermeture de la connexion client
                System.out.println("Client déconnecté");
                in.close();
                out.close();
                client.close();
            }
        } catch (Exception e) {
            throw IOException;
        }
    }
}
