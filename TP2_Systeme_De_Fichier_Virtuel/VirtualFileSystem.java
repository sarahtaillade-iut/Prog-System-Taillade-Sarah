import java.util.*;

public class VirtualFileSystem {

    private MemoryManager memoryManager;

    public VirtualFileSystem() {
        this.memoryManager = new MemoryManager();
    }

    private int allocateInode() {
        byte[] memory =memoryManager.getFilesystemMemory();

        // Parcourir les inodes de 0 à MAX_INODES - 1.
        // Identifier le premier inode libre.
        // Retourner son numéro.
	for (int i = 0; i < MemoryManager.MAX_INODES; i++) {
            Inode inode = new Inode(memoryManager, i);
            if (inode.getFileType() == 0) {
                return i;
            }
        }
        return -1;
    }

    public boolean createFile(String directory, String filename) {
        int inodeNum = allocateInode();
        if (inodeNum == -1) {
            return false;
        }

        // Construire l'inode.
        // L'initialiser comme fichier vide.
	Inode inode = new Inode(memoryManager, inodeNum);
        long now = System.currentTimeMillis();
        int[] emptyPointers = new int[Inode.DIRECT_POINTERS];

        inode.writeToMemory(
                1,              // fileType (1 = fichier)
                0,              // fileSize
                now,            // creationTime
                now,            // modificationTime
                emptyPointers,  // directPointers
                0,              // indirectPointer
                (short) 0644,   // permissions
                1               // linkCount
        );

        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }

    public boolean writeFile(int inodeNum, byte[] data) {
        int blocksNeeded =
                (data.length
                + MemoryManager.BLOCK_SIZE - 1)
                / MemoryManager.BLOCK_SIZE;

        if (blocksNeeded > Inode.DIRECT_POINTERS) {
            return false;
        }
   
        int[] blockPointers = new int[Inode.DIRECT_POINTERS];
        // Allouer blocksNeeded blocs.
	for (int i = 0; i < blocksNeeded; i++) {
            int allocated = memoryManager.allocateBlock();
            if (allocated == -1) {
                return false;
            }
            blockPointers[i] = allocated;
        }

        byte[] memory = memoryManager.getFilesystemMemory();
        int bytesRemaining =data.length;
        int dataSrcOffset = 0;

        // Pour chaque bloc :
        // - calculer la quantité à copier ;
        // - récupérer le numéro du bloc ;
        // - calculer son offset physique ;
        // - copier les données.
	for (int i = 0; i < blocksNeeded; i++) {
            int toCopy = Math.min(bytesRemaining, MemoryManager.BLOCK_SIZE);
            int blockNum = blockPointers[i];
            int physicalOffset = blockNum * MemoryManager.BLOCK_SIZE;
            System.arraycopy(data, dataSrcOffset, memory, physicalOffset, toCopy);
            dataSrcOffset += toCopy;
            bytesRemaining -= toCopy;
        }

        // Mettre à jour l'inode.
	Inode inode = new Inode(memoryManager, inodeNum);
        long now = System.currentTimeMillis();

        inode.writeToMemory(
                inode.getFileType(),
                data.length,
                now,
                now,
                blockPointers,
                0,
                (short) 0644,
                1
        );

        return true;
    }

    public byte[] readFile(int inodeNum) {
        Inode inode = new Inode(memoryManager, inodeNum);
        int fileSize = inode.getFileSize();
        if (fileSize == 0) {
            return new byte[0];
        }

        byte[] fileData = new byte[fileSize];
        byte[] memory = memoryManager.getFilesystemMemory();
        int[] blockPointers = inode.getDirectPointers();

        // Parcourir les blocs utilisés.
        // Copier chaque fragment vers fileData.
	int bytesRemaining = fileSize;
        int dstOffset = 0;
        for (int i = 0; i < blockPointers.length && bytesRemaining > 0; i++) {
            int blockNum = blockPointers[i];
            if (blockNum == 0) break;

            int toCopy = Math.min(bytesRemaining, MemoryManager.BLOCK_SIZE);
            int physicalOffset = blockNum * MemoryManager.BLOCK_SIZE;

            System.arraycopy(memory, physicalOffset, fileData, dstOffset, toCopy);

            dstOffset += toCopy;
            bytesRemaining -= toCopy;
        }

        return fileData;
    }
}
