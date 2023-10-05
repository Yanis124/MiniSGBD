import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.*;
import java.io.RandomAccessFile;

public class DiskManager {

    public Map<Integer, ArrayList<PageID>> filePageId = new HashMap<>(); // {file:[page]}
    private PageID currentAllocatedPage = new PageID(0, 0);
    private ArrayList<PageID> desalocatedPage = new ArrayList<PageID>(); // for desallocated page

    public DiskManager() {
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

        if (filePageId.containsKey(fileIdx) && pageIdx >= 0) { // pageIdx >= 0, pour l'instant c'est pour éviter
                                                               // l'erreur
            ArrayList<PageID> currentFilePagesTab = filePageId.get(fileIdx); // on va chercher la tableau correspondant
                                                                             // à la clé fileIdx
            int numPages = currentFilePagesTab.size();

            if (pageIdx < numPages) {
                try {

                    String filePath = DBParams.DBPath + "/F" + fileIdx + ".data";
                    RandomAccessFile file = new RandomAccessFile(filePath, "rw");
                    FileChannel fileChannel = file.getChannel();

                    int pageSize = getPageSize(buff);
                    int seekPosition = pageIdx * DBParams.SGBDPageSize + pageSize;
                    file.seek(seekPosition);
                    // Write the data from the ByteBuffer to the file at the specified page
                    fileChannel.write(buff);
                    file.close();

                    System.out.println("the message has been written");
                } catch (IOException e) {
                    System.err.println(e);
                    e.printStackTrace();
                }
            }
        }

    }

    private int getPageSize(ByteBuffer buff) {

        ByteBuffer buffRead = ByteBuffer.allocate(DBParams.SGBDPageSize);
        int pageSize = ReadPage(currentAllocatedPage, buffRead); // get the size of the page
        System.out.println("size of the page " + pageSize);

        return pageSize;
    }

    public int ReadPage(PageID pageId, ByteBuffer buff) {
        int fileIdx = pageId.getFileIdx();
        int pageIdx = pageId.getPageIdx();

        if (filePageId.containsKey(fileIdx) && pageIdx >= 0) {
            ArrayList<PageID> currentFilePagesTab = filePageId.get(fileIdx); // on va chercher la tableau correspondant
                                                                             // // à la clé fileIdx
            int numPages = currentFilePagesTab.size();

            if (pageIdx < numPages) {
                int seekPosition = pageIdx * DBParams.SGBDPageSize;

                try {
                    String filePath = DBParams.DBPath + "/F" + fileIdx + ".data";
                    RandomAccessFile file = new RandomAccessFile(filePath, "r");
                    FileChannel fileChannel = file.getChannel();
                    fileChannel.position(seekPosition);
                    int size = 0;

                    int bytesRead = fileChannel.read(buff);

                    if (bytesRead != -1) {
                        buff.flip(); // Prepare the buffer for reading
                        while (buff.hasRemaining()) { // iterate over the caracters of the page

                            byte currentByte = buff.get();
                            if (currentByte != '\0') {
                                char currentChar = (char) currentByte;
                                System.out.print(currentChar);
                                size++;
                            }
                        }
                        System.out.print("\n");
                        file.close();
                        return size;
                    }
                    else {
                        file.close();
                    }

                } catch (IOException e) {
                    System.err.println("can't access to the file");
                }

            }
        }
        return 0;

    }

    public void DeallocPage(PageID pageId) {

        int fileIdx = pageId.getFileIdx();
        int pageIdx = pageId.getPageIdx();

        if (filePageId.containsKey(fileIdx)) { // pour vérifier que le fichier correspondant à la page existe dans la
                                               // map
            ArrayList<PageID> currentFilePagesTab = filePageId.get(fileIdx); // un tableau temporaire qui va mettre à
                                                                             // jour à la fin la map

            if (pageIdx >= 0 && pageIdx < currentFilePagesTab.size()) { // vérifier que la page existe dans le fichier
                                                                        // 'mieux avec try catch)

                desalocatedPage.add(pageId); // ajout de la page qu'on va désallouer au tableau des pages à désallouer

                currentFilePagesTab.remove(pageIdx);

                filePageId.put(fileIdx, currentFilePagesTab); // mettre à jour la map avec le nouveau tableau sans la
                                                              // page qui a été désallouée

                System.out.println("Voici la page désallouée : Fichier n°" + fileIdx + ", Page n°" + pageIdx);

            } else {
                System.out.println("Ce message dit qu'il faut gérer une exception à cet endroit !");
            }
        } else {
            System.out.println("Ce message dit qu'il faut gérer une exception à cet endroit !");
        }
    }

    public int GetCurrentCountAllocPages() {
        int currentAllocatedPages = 0;
        for (ArrayList<PageID> pages : filePageId.values()) { // pour chaque tableau des map.values
            currentAllocatedPages += pages.size();
        }
        return currentAllocatedPages;
    }

}
