package GestionEspaceDisque_et_Buffer;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.io.RandomAccessFile;

public class DiskManager {

    private static DiskManager diskManager = new DiskManager();

    public Map<Integer, ArrayList<PageID>> filePageId = new HashMap<>(); // {file:[page]}
    private ArrayList<PageID> desalocatedPage = new ArrayList<PageID>(); // for desallocated page

    private DiskManager() {
        for (int i = 0; i < 4; i++) { // get the available files in the db
            String filePatheName = DBParams.DBPath + "/F" + i + ".data"; // assume that the db should always have DMFileCount

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
        PageID currentAllocatedPage=new PageID();

        if (!desalocatedPage.isEmpty()) { // if a page was desalocated we use it
            currentAllocatedPage = desalocatedPage.get(desalocatedPage.size() - 1);
            desalocatedPage.remove(desalocatedPage.size() - 1);
            currentSmallestFileId = currentAllocatedPage.getFileIdx();
            currentSmallestPageId = currentAllocatedPage.getPageIdx();

        } 
        else {   //if there isn't a disalocated page we use a page from a file with the least allocated page

            PageID currentSmallestPage = getSmallestPage();
            currentSmallestFileId = currentSmallestPage.getFileIdx();
            currentSmallestPageId = currentSmallestPage.getPageIdx();
        }
        currentAllocatedPage.setFileIdx(currentSmallestFileId);
        currentAllocatedPage.setPageIdx(currentSmallestPageId);
        updateFilePageId(currentAllocatedPage); //update the map filePageId by adding currentAllocatedPage to the list associated to its page
        System.out.println("allocated page : " +currentAllocatedPage.toString());
        return currentAllocatedPage;
    }

    private void updateFilePageId(PageID currentAllocatedPage){  //update the map filePageId
        ArrayList<PageID> filePages=new ArrayList<PageID>();
        ArrayList<PageID> filePages1 = filePageId.getOrDefault(currentAllocatedPage.getFileIdx(), new ArrayList<>());
        for(int i=0;i<filePages1.size();i++){
            PageID page=new PageID();
            PageID page1=filePages1.get(i);
            page.setFileIdx(page1.getFileIdx());
            page.setPageIdx(page1.getPageIdx());
            filePages.add(page);
        }
        filePages.add(currentAllocatedPage);
        filePageId.put(currentAllocatedPage.getFileIdx(), filePages);
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

                System.out.println("desalocated page : " +pageId.toString());

            } else {
                System.out.println("this page was not allocated");
            }
        } else {
            System.out.println("this page doesn't belong to any file of the database");
        }
    }

    // write the content of the buffer at the indicated pageId
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

    //get the size of page
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
                return size;
            }
            file.close();

        } catch (IOException e) {
            System.err.println("can't access to the file");
        }

        return 0;
    }

    //get the content of a page to a byteBuffer
    public static ByteBuffer ReadPage(PageID pageId, ByteBuffer buff) {
        if(pageId.isValid()){
           buff.position(0);
            
            int fileIdx = pageId.getFileIdx();
            int pageIdx = pageId.getPageIdx();

            

            int seekPosition = pageIdx * DBParams.SGBDPageSize;
            String filePath = DBParams.DBPath + "/F" + fileIdx + ".data";
            try {
                  buff.order(ByteOrder.BIG_ENDIAN);

                RandomAccessFile file = new RandomAccessFile(filePath, "rw");
                FileChannel fileChannel = file.getChannel();
                fileChannel.position(seekPosition);
                fileChannel.read(buff);
                file.close();

            }

            catch (IOException e) {
                System.err.println("can't access to the file");
            }
        }

        return buff;
    }


    //read the content of a byteBuffer
    public static StringBuilder readContentOfBuffer(ByteBuffer bf) { 
        bf.position(0);                      
        StringBuilder message = new StringBuilder();
        byte currentByte;
        while (bf.hasRemaining()) {
            currentByte= bf.get();
            
            if (currentByte != '\0') {
                char currentChar = (char) currentByte;
                message.append(currentChar);

            }
        }
        bf.position(0);
        return message;
    }

    public int getByteBufferSize(ByteBuffer bf){
        bf.position(0);                      
        int size=0;
        byte currentByte;
        while (bf.hasRemaining()) {
            currentByte= bf.get();
            
            if (currentByte != '\0') {
                size++;
            }
        }
        return size;
    }

    

    
    public int GetCurrentCountAllocPages() {
        int currentAllocatedPages = 0;
        for (ArrayList<PageID> pages : filePageId.values()) { // for each array in map.values
            currentAllocatedPages += pages.size();
        }
        return currentAllocatedPages;
    }

    //get a view of variables
    public String toString(){
        String filePageIdInfo="filePageId : \n";
        for(int fileIdx : filePageId.keySet()){
            filePageIdInfo+=" file : "+fileIdx+"-->"+"[";
            for(PageID page: filePageId.get(fileIdx)){
                filePageIdInfo+=page.toString();
            }
            filePageIdInfo+="] \n";
        }
        filePageIdInfo+=" disalocated Pages : "+"\n [";
        for(PageID page:desalocatedPage){
            filePageIdInfo+=page.toString();
        }
        return filePageIdInfo+"]";
    }

}
