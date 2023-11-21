package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.PageID;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class FileManager {

    private static FileManager fileManager = new FileManager();

    private FileManager() {

    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    // create a new headerPage
    public PageID createNewHeaderPage() {

        DiskManager diskManager = DiskManager.getDiskManager();
        BufferManager bufferManager = BufferManager.getBufferManager();

        PageID pageId = diskManager.AllocPage(); // allocate a page

        HeaderPage headerPage = new HeaderPage(pageId); // create a headerPage with the allocated page

        bufferManager.freePage(pageId, true);

        return pageId; // Return PageId
    }

    // add a dataPage to the tableInfo
    public PageID addDataPage(TableInfo tableInfo) {

        DiskManager diskManager = DiskManager.getDiskManager();
        PageID pageId = diskManager.AllocPage(); // allocate a page

        BufferManager bufferManager = BufferManager.getBufferManager();

        DataPages dataPages = new DataPages(pageId); // create the new dataPage

        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage of tableInfo

        PageID freePageId = headerPage.getFreePage(); // get the currentFreePage

        headerPage.setFreePage(pageId); // set the newFreeDataPages as the currentFreeDataPages
        headerPage.setFullPage(headerPage.getFullPage()); // this line should be deleted but when deleted it seems like
                                                          // the postion of free space is gone
        dataPages.setNextPage(freePageId); // link the newFreeDataPages to the currentFreeDataPages
        dataPages.getPosFreeSpace(); // this line should be deleted but when deleted it seems like the postion of
                                     // free space is gone

        bufferManager.freePage(pageId, true); // write the new created page
        bufferManager.freePage(tableInfo.getHeaderPageId(), true); // headerPage was modifie so we should set the flag
                                                                   // to true
        return pageId;
    }

    // return the dataPages that has enough space to insert the record
    public PageID getFreePageId(TableInfo tableInfo, int sizeRecord) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage of tableInfo
        DataPages freePageId = getDataPages(headerPage.getFreePage()); // get the first freePage of a tableInfo

        if (freePageId.getPageID().isValid()) {
            while (freePageId.getAvailableSpace() <= sizeRecord) { // while the freePage
                                                                   // doesn't have
                                                                   // enough space we
                                                                   // check the next

                freePageId = getDataPages(freePageId.getNextPage()); // get the next freePage
            }

            if (freePageId.getPageID().isValid()) { // if there is a dataPage that has enought space we return it
                return freePageId.getPageID();
            }
        }

        bufferManager.freePage(headerPage.getPageID(), false);
        bufferManager.freePage(freePageId.getPageID(), false);

        return new PageID(); // otherwise we return null
    }

    // add a record to the dataPage
    public RecordId writeRecordToDataPage(Record record, PageID pageIdDataPage) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        int sizeRecord = record.sizeRecord(); // get the size of a record
        DataPages dataPages = getDataPages(pageIdDataPage); // get the dataPage

        if (sizeRecord + 8 > dataPages.getAvailableSpace()) { // +8 because every time we add a record that take n space
                                                              // we should also add a slot that take 8 bytes so the
                                                              // total space that our page should has is n+8
            System.out.println("not enough space"); // must be written in another page
            return null;
        }

        // if there is enough space we wtite it in this page

        int posFreeSpace = dataPages.getPosFreeSpace(); // get the position where we should write the record

        ByteBuffer byteBuffer = dataPages.getByteBuffer(); // get the content of the dataPage

        record.writeToBuffer(byteBuffer, posFreeSpace); // writte the record to the byteBuffer of the dataPage

        int numberSlot = dataPages.getNumberSlot();
        byteBuffer.position(DBParams.SGBDPageSize - (numberSlot * 8 + 8 + 8));// position of the new slot

        // add the slot to the byteBuffer of the dataPage
        byteBuffer.putInt(posFreeSpace);
        byteBuffer.putInt(sizeRecord);

        // update the number of slot and available space
        dataPages.setPosFreeSpace(posFreeSpace + sizeRecord);
        dataPages.setNumberSlot(numberSlot + 1);

        bufferManager.freePage(pageIdDataPage, true); // we edit the page so we should write it

        // check if the page is full
        if (dataPages.getAvailableSpace() <= DBParams.PageFull) {

            updateToFullPage(pageIdDataPage, record.getTableInfo());
        }

        

        return new RecordId(pageIdDataPage, numberSlot);
    }

    // add all the record of a dataPage to a list
    public ArrayList<Record> getRecordsInDataPage(TableInfo tableInfo, PageID pageId) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        ArrayList<Record> listRecords = new ArrayList<>(); // create the list of record
        DataPages dataPage = getDataPages(pageId); // create the dataPage
        int numberRecod = dataPage.getNumberSlot(); // get number of record in the dataPage

        ByteBuffer byteBuffer = dataPage.getByteBuffer();
        for (int i = 0; i < numberRecod; i++) {
            Record record = new Record(tableInfo);// create an empty record
            int positionRecord = dataPage.getPosRecord(i);

            record.readFromBuffer(byteBuffer, positionRecord);
            listRecords.add(record);
        }

        bufferManager.freePage(pageId, false);

        return listRecords;
    }

    // get all the pageId of the dataPage of a tableInfo
    public ArrayList<PageID> getDataPages(TableInfo tableInfo) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        ArrayList<PageID> listDataPagesId = new ArrayList<>(); // create the list
        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage of tableInfo
        PageID freeDataPageId = headerPage.getFreePage(); // get the first freePageId
        PageID fullPageId = headerPage.getFullPage(); // get the first fullpageId

        bufferManager.freePage(headerPage.getPageID(), false);

        while (freeDataPageId.isValid()) {
            listDataPagesId.add(freeDataPageId);
            DataPages freeDataPage = getDataPages(freeDataPageId);
            freeDataPageId = freeDataPage.getNextPage();
            bufferManager.freePage(freeDataPage.getPageID(), false);
        }

        while (fullPageId.isValid()) {
            listDataPagesId.add(fullPageId);
            DataPages fullDataPage = getDataPages(fullPageId);
            fullPageId = fullDataPage.getNextPage();
            bufferManager.freePage(fullDataPage.getPageID(), false);
        }

        return listDataPagesId;
    }

    // insert a record into a relation
    public RecordId InsertRecordIntoTable(Record record) {

        TableInfo tableInfo = record.getTableInfo(); // get the relation of a record
        PageID freePageId = getFreePageId(tableInfo, record.sizeRecord()); // get a dataPage that has enough space
        

        if (!freePageId.isValid()) { // if all Datapages are full we add another dataPage
            freePageId = addDataPage(tableInfo);
        }
        System.out.println("free page id : "+freePageId);

        return writeRecordToDataPage(record, freePageId);
    }



    //get the record from a relation
    public ArrayList<Record> getAllRecords (TableInfo tabInfo){

        ArrayList <Record> listRecords=new ArrayList<>();  //list of record
        ArrayList<PageID> listPages=getDataPages(tabInfo);  //list of pages inside a relation
        
        for(int i=0;i<listPages.size();i++){
            ArrayList<Record> listRecordsPage=getRecordsInDataPage(tabInfo, listPages.get(i)); //get the record of a page
            
            
            for(Record record:listRecordsPage){
                listRecords.add(record);
            }
        }
        return listRecords;
    }

    // update a dataPage from a freePage to a fullPage
    public void updateToFullPage(PageID newFullPageId, TableInfo tableInfo) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage
        PageID fullPage = headerPage.getFullPage();
        DataPages newFullPage = getDataPages(newFullPageId);

        DataPages freePage = getDataPages(headerPage.getFreePage()); // get the freePage

        PageID nextFreePageId = headerPage.getFreePage();
        DataPages currentfreePages = getDataPages(nextFreePageId);

        while (!nextFreePageId.equals(newFullPageId)) { // update the nextfreePage of the previous freePage

            nextFreePageId = currentfreePages.getPageID(); // get the pageId of the next freePageId

            if (nextFreePageId.equals(newFullPageId)) {
                DataPages nextFreePage = getDataPages(nextFreePageId);
                currentfreePages.setNextPage(nextFreePage.getNextPage());
                bufferManager.freePage(nextFreePageId, false);
            }
            currentfreePages = getDataPages(currentfreePages.getNextPage());

        }

        // if the newFullPage is the firstFreePage we set the freePage to the next
        // freePage
        if (freePage.getPageID().equals(newFullPageId)) {
            headerPage.setFreePage(currentfreePages.getNextPage());
        }
        // update the fullPage

        newFullPage.setNextPage(fullPage); // link the newfullPage to the fullpage of headerPage
        newFullPage.getPosFreeSpace();

        headerPage.setFullPage(nextFreePageId); // set the fullPage of the headerPage to the newFullPage

        bufferManager.freePage(headerPage.getPageID(), true);
        bufferManager.freePage(currentfreePages.getPageID(), true);
        bufferManager.freePage(newFullPageId, false);

    }

    // create the headerPage with the content of pageId
    public HeaderPage getHeaderPageOfTableInfo(TableInfo tableInfo) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        PageID pageIdHeaderPage = tableInfo.getHeaderPageId(); // get the pageId of headerPage
        ByteBuffer byteBufferHeaderPage = bufferManager.getByteBufferPage(pageIdHeaderPage); // get the content of the
                                                                                             // PageIdHeaderPag
        HeaderPage headerPage = new HeaderPage(byteBufferHeaderPage, pageIdHeaderPage); // create a headerPage with the
                                                                                        // content of the byteBuffer
        return headerPage;
    }

    // create a dataPage based on the content of its page
    public DataPages getDataPages(PageID pageId) {

        BufferManager bufferManager = BufferManager.getBufferManager();

        ByteBuffer byteBuffer = bufferManager.getByteBufferPage(pageId); // get the content of the dataPage
        DataPages dataPages = new DataPages(byteBuffer, pageId); // create the dataPage with the content of the
                                                                 // byteBuffer

        return dataPages;
    }

}
