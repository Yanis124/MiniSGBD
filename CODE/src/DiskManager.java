import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.RandomAccessFile;

public class DiskManager {

    private static DiskManager diskManager = new DiskManager();

    public Map<Integer, ArrayList<PageID>> filePageId = new HashMap<>(); // {file:[page]}
    private PageID currentAllocatedPage = new PageID(0, 0);
    private ArrayList<PageID> desalocatedPage = new ArrayList<PageID>(); // for desallocated page

    private DiskManager() {
        for (int i = 0; i < DBParams.DMFileCount; i++) { // get the available files in the db
            String filePatheName = DBParams.DBPath + "/F" + i + ".data"; // assume that the db should always have
                                                                         // DMFileCount

            File file = new File(filePatheName);
            if (!file.exists()) {
                DBParams.createFile();
            }
            filePageId.put(i, new ArrayList<PageID>());
        }
    }

    public static DiskManager getDiskManager() {
        return diskManager;
    }

    public PageID AllocPage() {

        int currentSmallestFileId;
        int currentSmallestPageId;

        if (!desalocatedPage.isEmpty()) { // if a page was desalocated
            currentAllocatedPage = desalocatedPage.get(desalocatedPage.size() - 1);
            desalocatedPage.remove(desalocatedPage.size() - 1);
            currentSmallestFileId = currentAllocatedPage.getFileIdx();
            currentSmallestPageId = currentAllocatedPage.getPageIdx();

        } else {

            PageID currentSmallestPage = getSmallestPage();
            currentSmallestFileId = currentSmallestPage.getFileIdx();
            currentSmallestPageId = currentSmallestPage.getPageIdx();
        }
        currentAllocatedPage.setFileIdx(currentSmallestFileId);
        currentAllocatedPage.setPageIdx(currentSmallestPageId);
        ArrayList<PageID> filePages = filePageId.getOrDefault(currentSmallestFileId, new ArrayList<>());

        filePages.add(currentAllocatedPage);
        filePageId.replace(currentSmallestFileId, filePages);

        System.out.println("current file:   " + currentAllocatedPage.getFileIdx() + "/ page "
                + currentAllocatedPage.getPageIdx() + "\t");

        return currentAllocatedPage;
    }

    private PageID getSmallestPage() { // get the last page of the file with the smallest allocated page
        int smallestFileSize = Integer.MAX_VALUE;
        int fileIdxWithSmallestSize = -1;

        for (int fileIdx : filePageId.keySet()) { // get the file with the less number of pages
            int fileSize = filePageId.get(fileIdx).size();
            if (fileSize < smallestFileSize) {
                smallestFileSize = fileSize;
                fileIdxWithSmallestSize = fileIdx;
            }
        }
        return new PageID(fileIdxWithSmallestSize, smallestFileSize);
    }

    public void WritePage(PageID pageId, ByteBuffer buff) {

        int fileIdx = pageId.getFileIdx();
        int pageIdx = pageId.getPageIdx();

        if (filePageId.containsKey(fileIdx) && pageIdx >= 0) { // pageIdx >= 0, for now, it's a way to avoid errors
            ArrayList<PageID> currentFilePagesTab = filePageId.get(fileIdx); // we'll search the array that corresponds
                                                                             // to the key fileIdx
            int numPages = currentFilePagesTab.size();

            if (pageIdx < numPages) {
                try {

                    String filePath = DBParams.DBPath + "/F" + fileIdx + ".data";
                    RandomAccessFile file = new RandomAccessFile(filePath, "rw");
                    FileChannel fileChannel = file.getChannel();

                    int pageSize = getPageSize(filePath, pageIdx);
                    int seekPosition = pageIdx * DBParams.SGBDPageSize + pageSize;
                    file.seek(seekPosition);
                    // Write the data from the ByteBuffer to the file at the specified page
                    fileChannel.write(buff);

                    file.close();

                    System.out.println("the message has been written");
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }

    }

    private int getPageSize(String filePath, int pageIdx) {

        ByteBuffer buffRead = ByteBuffer.allocate(DBParams.SGBDPageSize);
        int seekPosition = pageIdx * DBParams.SGBDPageSize;
        try {
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            FileChannel fileChannel = file.getChannel();
            fileChannel.position(seekPosition);

            int size = 0;
            int bytesRead = fileChannel.read(buffRead);
            if (bytesRead != -1) {
                buffRead.flip(); // Prepare the buffer for reading
                while (buffRead.hasRemaining()) { // iterate over the caracters of the page

                    byte currentByte = buffRead.get();
                    if (currentByte != '\0') {
                        size++;
                    }
                }
                file.close();
                System.out.println("size : " + size);
                return size;
            }
            file.close();

        } catch (IOException e) {
            System.err.println("can't access to the file");
        }

        return 0;
    }

    public ByteBuffer ReadPage(PageID pageId, ByteBuffer buff) {
        int fileIdx = pageId.getFileIdx();
        int pageIdx = pageId.getPageIdx();

        // if (filePageId.containsKey(fileIdx) && pageIdx >= 0) {
        // ArrayList<PageID> currentFilePagesTab = filePageId.get(fileIdx); // on va
        // chercher la tableau correspondant
        // // // à la clé fileIdx
        // int numPages = currentFilePagesTab.size();

        // if (pageIdx < numPages) {

        // dans BufferManager on sera amené a lire une page sans l'allouer donc pour
        // l'instant je met en commentaire les conditions que la page est bien allouée

        int seekPosition = pageIdx * DBParams.SGBDPageSize;
        try {
            String filePath = DBParams.DBPath + "/F" + fileIdx + ".data";
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            FileChannel fileChannel = file.getChannel();
            fileChannel.position(seekPosition);
            file.close();

            fileChannel.read(buff);

        }

        catch (IOException e) {
            System.err.println("can't access to the file");
        }
        return buff;
    }

    public void DeallocPage(PageID pageId) {

        int fileIdx = pageId.getFileIdx();
        int pageIdx = pageId.getPageIdx();

        if (filePageId.containsKey(fileIdx)) { // "To check if the file corresponding to the page exists 
                                               // in the map
            ArrayList<PageID> currentFilePagesTab = filePageId.get(fileIdx); // "A temporary array that will
                                                                             // update the map at the end

            if (pageIdx >= 0 && pageIdx < currentFilePagesTab.size()) { // verify that the page exists in the file
                                                                        // 'better with the try catch)

                desalocatedPage.add(pageId); // adding the page that we'll desalocate to the array of desallocated pages

                currentFilePagesTab.remove(pageIdx);

                filePageId.put(fileIdx, currentFilePagesTab); // update the map with the new array but without
                                                              // the desallocated page

                System.out.println("Voici la page désallouée : Fichier n°" + fileIdx + ", Page n°" + pageIdx);

            } else {
                System.out.println("Ce message dit qu'il faut gérer une exception à cet endroit !");
            }
        } else {
            System.out.println("Ce message dit qu'il faut gérer une exception à cet endroit !");
        }
    }

    public void readContentOfBuffer(ByteBuffer bf) { // for test
        bf.flip();
        while (bf.hasRemaining()) {

            byte currentByte = bf.get();
            if (currentByte != '\0') {
                char currentChar = (char) currentByte;
                System.out.print(currentChar);
            }
        }
    }

    public int GetCurrentCountAllocPages() {
        int currentAllocatedPages = 0;
        for (ArrayList<PageID> pages : filePageId.values()) { // for each array in map.values
            currentAllocatedPages += pages.size();
        }
        return currentAllocatedPages;
    }

}
