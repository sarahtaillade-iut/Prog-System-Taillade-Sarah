public class Utils {

    public static int writeInt(byte[] memory, int offset, int value) {
        // TODO: Écrire les 4 octets de 'value' dans 'memory'
        // à partir de 'offset', en big-endian
        
        //byte b3 = (byte)(value 0xFF);
        //memory[offset] = b3;
        //byte b2 = (...)<-ex:B2 (...)<-ex:v...
        //offset + 0 = F0;
        //offset + 1 = A1;
        //offset + 2 = B2;
        //offset + 3 = E3;
        return 4;
    }

    public static int readInt(byte[] memory, int offset) {
        // TODO: Reconstituer le int sur 4 octets.
        return 0;
    }

    public static int writeShort(byte[] memory, int offset, short value) {
        // TODO: Écrire les 2 octets de 'value'.
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        // TODO: Lire le short sur 2 octets.
        return 0;
    }
}
