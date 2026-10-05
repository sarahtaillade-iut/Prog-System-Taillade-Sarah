public class Inode {

    private MemoryManager memoryManager;
    private int inodeNumber;

    public static final int INODE_SIZE = 128;
    public static final int DIRECT_POINTERS = 10;

    public Inode(MemoryManager memoryManager, int inodeNumber) {
        this.memoryManager = memoryManager;
        this.inodeNumber = inodeNumber;
    }

    public int getInodeOffset() {
        // Calculer l'offset exact de l'inode.
        return MemoryManager.INODE_TABLE_OFFSET + (inodeNumber * INODE_SIZE);
    }

    public int getFileType() {
        // Lire le type à offset + 4.
        return Utils.readInt(memoryManager.getFilesystemMemory(), getInodeOffset() + 4);
    }

    public int getFileSize() {
        // Lire la taille à offset + 8.
        return Utils.readInt(memoryManager.getFilesystemMemory(), getInodeOffset() + 8);
    }

    public int[] getDirectPointers() {
        byte[] memory = memoryManager.getFilesystemMemory();
        int[] pointers = new int[DIRECT_POINTERS];
	// Lire les 10 pointeurs directs.
	int offset = getInodeOffset() + 28;
	for (int i = 0; i < DIRECT_POINTERS; i++) {
	    pointers[i] = Utils.readInt(memory, offset + (i * 4));
	}        

        return pointers;
    }

    public void writeToMemory(
        int fileType,
        int fileSize,
        long creationTime,
        long modificationTime,
        int[] directPointers,
        int indirectPointer,
        short permissions,
        int linkCount) {

        byte[] memory = memoryManager.getFilesystemMemory();
        int curseur = getInodeOffset();

        // 1. Numéro d'inode
        Utils.writeInt(memory, curseur, inodeNumber);
        curseur += 4;

        // 2. Type
        Utils.writeInt(memory, curseur, fileType);
        curseur += 4;

        // 3. Taille
        Utils.writeInt(memory, curseur, fileSize);
        curseur += 4;

        // 4. Création
        Utils.writeLong(memory, curseur, creationTime);
        curseur += 8;

        // 5. Modification
        Utils.writeLong(memory, curseur, modificationTime);
        curseur += 8;

        // 6. 10 pointeurs directs
        for( int i = 0; i < DIRECT_POINTERS; i++) {
	    int ptr = (directPointers != null && i < directPointers.length) ? directPointers[i] : 0;
    	    Utils.writeInt(memory, curseur, ptr);
	    curseur += 4;
        }

        // 7. Pointeur indirect
        Utils.writeInt(memory, curseur, indirectPointer);
        curseur += 4;

        // 8. Permissions
        Utils.writeShort(memory, curseur, permissions);
        curseur += 2;

        // 9. Nombre de liens
        Utils.writeInt(memory, curseur, linkCount);
    }
}