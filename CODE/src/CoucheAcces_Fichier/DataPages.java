package CoucheAcces_Fichier;

import java.nio.ByteBuffer;
import GestionEspaceDisque_et_Buffer.*;

public class DataPages {
    private ByteBuffer byteBuffer;
    private PageID pageId;

    public DataPages(PageID pageId) {

        this.pageId = pageId; // set the pageId of the dataPage

        BufferManager bufferManager = BufferManager.getBufferManager(); // Get Buffer and Disk manager

        byteBuffer = bufferManager.getPage(pageId); // the byteBuffer will containe all information (next page/ records/
                                                    // slot .....)

        PageID pageIdNext = new PageID(); // create an empty page as the next page(-1,-1)

        // add the next page
        setNextPage(pageIdNext);

        // add position of available space to the DataPage
        int posFreeSpace = 8;
        setPosFreeSpace(posFreeSpace);

        // add the number of slot
        int numberSlot = 0;
        setNumberSlot(numberSlot);

    }

    // create a dataPage with the content of the byteBuffer
    public DataPages(ByteBuffer byteBuffer, PageID pageId) {

        this.byteBuffer = byteBuffer;
        if (byteBuffer == null) {
            this.pageId = new PageID(); // if nothing is written in the byteBuffer it means that the page is (-1,-1)
        } else {
            this.pageId = pageId;
        }
    }

    // get the pageId of the dataPage
    public PageID getPageID() {
        return pageId;
    }

    // set the pageId of the dataPage
    public void setPageID(PageID pageId) {
        this.pageId = pageId;
    }

    // set the next page
    public void setNextPage(PageID pageId) {
        int pos = 0;
        byteBuffer.position(pos);
        byteBuffer.putInt(pageId.getFileIdx());
        byteBuffer.putInt(pageId.getPageIdx());
    }

    public PageID getNextPage() {
        int pos = 0;
        byteBuffer.position(pos);
        return new PageID(byteBuffer.getInt(), byteBuffer.getInt());
    }

    // return the position of the first empty byte
    public int getPosFreeSpace() {

        int pos =DBParams.SGBDPageSize-4;
        byteBuffer.position(pos);
        return byteBuffer.getInt();
    }

    // set the position of free space
    public void setPosFreeSpace(int freeSpace) {
        int pos =DBParams.SGBDPageSize-4;
        byteBuffer.position(pos);
        byteBuffer.putInt(freeSpace);

    }

    // get the number of slot in the dataPage
    public int getNumberSlot() {

        int pos = DBParams.SGBDPageSize - 8;
        byteBuffer.position(pos);
        return byteBuffer.getInt();
    }

    public void setNumberSlot(int numberSlot) {
        int pos = DBParams.SGBDPageSize - 8;
        byteBuffer.position(pos);
        byteBuffer.putInt(numberSlot);
    }

    // return the available space in the dataPage
    public int getAvailableSpace() {
        int posFreeSpace = getPosFreeSpace();
        int numberSlot = getNumberSlot();
        int availableSpace = DBParams.SGBDPageSize - (posFreeSpace + (numberSlot * 8) + 8);

        return availableSpace;
    }

    // get the position of a record in a dataPage
    public int getPosRecord(int indexRecod) {
        int pos = DBParams.SGBDPageSize - (16 + 8 * indexRecod);
        byteBuffer.position(pos);
        return byteBuffer.getInt();
    }

    // get the space taken by a record
    public int getSpaceRecod(int indexRecod) {
        int pos = DBParams.SGBDPageSize - (12 + 8 * indexRecod);
        byteBuffer.position(pos);
        return byteBuffer.getInt();
    }

    // get the content of the page
    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    // return the content of a created DataPage
    public static ByteBuffer getDataPage(PageID pageId) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        return bufferManager.getPage(pageId);
    }

    public String toString() {
        String content = "";
        if (byteBuffer != null) {
            content+="page : "+pageId+"\n";
            content += "next page : " + this.getNextPage() + "\n";
            content += "position free space : " + this.getPosFreeSpace() + "\n";
            content += "number slot : " + this.getNumberSlot() + "\n";
            content += "available space : " + this.getAvailableSpace() + "\n";

        }
        return content;
    }

    // // free the dataPage
    public void finalize() {

        BufferManager bufferManager = BufferManager.getBufferManager();
        bufferManager.freePage(this.pageId, true);
    }

}
