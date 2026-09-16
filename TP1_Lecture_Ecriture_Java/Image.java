import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;

public class Image {
    private int width;
    private int height;
    private int[][][] pixels;
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    /**
     * Constructeur : initialise une image vide.
     */
    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[height][width][3];
    }

    /**
     * Définit la couleur d'un pixel à la position (x, y)
     */
    public void setPixel(int x, int y, int r, int g, int b) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[y][x][0] = r;
            pixels[y][x][1] = g;
            pixels[y][x][2] = b;
        }
    }

    /**
     * Sauvegarde de l'image au format texte PPM
     */
    public void save_txt(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("P3\n");
        writer.write(width + " " + height + "\n");
        writer.write("255\n");

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.write(pixels[y][x][0] + " " + pixels[y][x][1] + " " + pixels[y][x][2] + " ");
            }
            writer.write("\n");
        }
        writer.close();
    }
	
	/**
     * Sauvegarde l'image au format binaire
     */
    public void save_bin(String filename) throws IOException {
        FileOutputStream writer = new FileOutputStream(filename);
        // Phrase de présentation de l'entête du fichier binarie PPM
        String entete = "P6\n" + width + " " + height + "\n255\n";
        writer.write(entete.getBytes());
        byte[] pixel = new byte[3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixel[0] = (byte) pixels[y][x][0];
                pixel[1] = (byte) pixels[y][x][1];
                pixel[2] = (byte) pixels[y][x][2];
                writer.write(pixel);
            }
        }
        writer.close();
    }
}
