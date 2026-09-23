public class Utils {
    // Notes pour le TP : 
    // penser a utiliser les : << et >> (ex : int v = 1; v >> 1 = 0; v << 1 = 2;)
    // byte b3 = (byte)(value 0xFF);
    // memory[offset] = b3;
    // byte b2 = (...)<-ex:B2 (...)<-ex:v...
    // offset + 0 = F0;
    // offset + 1 = A1;
    // offset + 2 = B2;
    // offset + 3 = E3;
    public static int writeInt(byte[] memory, int offset, int value) {
        // Écriture des 4 octets de 'value' dans 'memory' à partir de 'offset', en big-endian.
        memory[offset] = (byte)(value >> 24);
        memory[offset + 1] = (byte)(value >> 16);
        memory[offset + 2] = (byte)(value >> 8);
        memory[offset + 3] = (byte)(value);
        // Retourne les 4 octets
        return 4;
    }

    public static int readInt(byte[] memory, int offset) {
        // Reconstitution de int sur 4 octets, en big-endian.
        int b0 = (memory[offset] & 0xFF) << 24;
        int b1 = (memory[offset + 1] & 0xFF) << 16;
        int b2 = (memory[offset + 2] & 0xFF) << 8;
        int b3 = (memory[offset + 3] & 0xFF);
        // Retourne le int sur 4 octets
        return b0 | b1 | b2 | b3;
    }

    public static int writeShort(byte[] memory, int offset, short value) {
        // Écriture des 2 octets de 'value' dans 'memory' à partir de 'offset'.
        memory[offset] = (byte)(value >> 8);
        memory[offset + 1] = (byte)(value);
        // Retourne les 2 octets
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        // Lit le short sur 2 octets.
        int b0 = (memory[offset] & 0xFF) << 8;
        int b1 = (memory[offset + 1] & 0xFF);
        // Retourne le résultat converti en short
        return (short) (b0 | b1);
    }

    public static int writeLong(byte[] memory, int offset, long value) {
        // Écriture des 8 octets de 'value' dans 'memory' à partir de 'offset', en big-endian.
        memory[offset] = (byte)(value >> 56);
        memory[offset + 1] = (byte)(value >> 48);
        memory[offset + 2] = (byte)(value >> 40);
        memory[offset + 3] = (byte)(value >> 32);
        memory[offset + 4] = (byte)(value >> 24);
        memory[offset + 5] = (byte)(value >> 16);
        memory[offset + 6] = (byte)(value >> 8);
        memory[offset + 7] = (byte)(value);
        // Retourne les 8 octets
        return 8;
    }
    
    public static long readLong(byte[] memory, int offset) {
        // Reconstituer le long sur 8 octets.
        long b0 = (memory[offset] & 0xFFL) << 56;
        long b1 = ((long) memory[offset + 1] & 0xFFL) << 48;
        long b2 = ((long) memory[offset + 2] & 0xFFL) << 40;
        long b3 = ((long) memory[offset + 3] & 0xFFL) << 32;
        long b4 = ((long) memory[offset + 4] & 0xFFL) << 24;
        long b5 = ((long) memory[offset + 5] & 0xFFL) << 16;
        long b6 = ((long) memory[offset + 6] & 0xFFL) << 8;
        long b7 = ((long) memory[offset + 7] & 0xFFL);
        // Retourne le le long sur 8 octets
        return b0 | b1 | b2 | b3 | b4 | b5 | b6 | b7;
    }

    public static int writeString(byte[] memory, int offset, String str, int maxLength) {
        // Convertion la chaine en octets
        byte[] strBytes = str.getBytes();

        // Copie des octets (sans dépasser maxLength)
        int bytesACopier = strBytes.length;
        if (bytesACopier > maxLength) {
            bytesACopier = maxLength;
        } 
        for (int i = 0; i < bytesACopier; i++) {
            memory[offset + i] = strBytes[i];
        }
        
        // Nettoyage du reste de la zone avec des zéros
        for (int i = bytesACopier; i < maxLength; i++) {
            memory[offset + i] = 0;
        }

        // Retourne la taille maximale
        return maxLength;
    }

    public static String readString(byte[] memory, int offset, int maxLength) {
        // Lecture jusqu'au premier octet nul ou jusqu'a maxLength
        int taille = 0;
        while (taille < maxLength && memory[offset + taille] != 0) {
            taille++;
        }

        // Retourne le string avec les octets lus (jusqu'à taille ou maxLength)
        return new String(memory, offset, taille);
    }
}