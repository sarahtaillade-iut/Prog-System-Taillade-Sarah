import java.net.*;
import java.io.*;

public class ServeurMulti {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Serveur multi-clients démarré");
        
        try {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Nouveau client : " + client.getInetAddress());

                // Délégation à un thread dédié
                Thread t = new Thread(new ClientHandler(client));
                t.start();
            }
        } catch (Exception e) {
            throw IOException;
        }
    }
}

class ClientHandler implements Runnable {
    private Socket client;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.client = socket;
        this.out = out;
    }

    @Override
    public void run() {
       // Nommer le thread pour faciliter le débogage
        Thread.currentThread().setName("Client-" + client.getPort());

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        ) {
            String ligne;
            // Boucle de lecture jusqu'à la déconnexion du client (readLine retourne null)
            while ((ligne = in.readLine()) != null) {
                System.out.println("[" + Thread.currentThread().getName() + "] Reçu : " + ligne);
                out.println("ECHO: " + ligne);
            }
        } catch (IOException e) {
            throw IOException;
        } finally {
            try { 
                client.close(); 
            } catch (IOException ignored) {}
        } 
    }
}