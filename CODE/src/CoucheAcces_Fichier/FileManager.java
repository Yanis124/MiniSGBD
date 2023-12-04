package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.PageID;

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
        
        PageID pageId = diskManager.AllocPage(); // allocate a page

        HeaderPage headerPage = new HeaderPage(pageId); // create a headerPage with the allocated page

        headerPage.finalize(); //write the page

        return pageId; // Return PageId
    }

    // add a dataPage to the tableInfo
    public PageID addDataPage(TableInfo tableInfo) {

        DiskManager diskManager = DiskManager.getDiskManager();
        PageID pageId = diskManager.AllocPage(); // allocate a page

        DataPages dataPages = new DataPages(pageId); // create the new dataPage

        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage of tableInfo

        PageID freePageId = headerPage.getFreePage(); // get the currentFreePage

        headerPage.setFreePage(pageId); // set the newFreeDataPages as the currentFreeDataPages
        headerPage.setFullPage(headerPage.getFullPage()); // this line should be deleted but when deleted it seems like
                                                          // the postion of free space is gone
        dataPages.setNextPage(freePageId); // link the newFreeDataPages to the currentFreeDataPages
        dataPages.getPosFreeSpace(); // this line should be deleted but when deleted it seems like the postion of
                                     // free space is gone

        headerPage.finalize(); //Write the HeaderPage
        dataPages.finalize(); //write the dataPage
        //bufferManager.flushAll();
        
        return pageId;
    }

    // return the dataPages that has enough space to insert the record
    public PageID getFreePageId(TableInfo tableInfo, int sizeRecord) {

        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage of tableInfo
        DataPages freePage = getDataPages(headerPage.getFreePage()); // get the first freePage of a tableInfo
        
        headerPage.finalize();  //

        if (freePage.getPageID().isValid()) {
            while (freePage.getAvailableSpace() <= sizeRecord) { // while the freePage
                                                                   // doesn't have
                                                                   // enough space we
                                                                   // check the next

                freePage = getDataPages(freePage.getNextPage()); // get the next freePage
            }

            if (freePage.getPageID().isValid()) { // if there is a dataPage that has enought space we return it
                freePage.finalize();
                return freePage.getPageID();
            }
        }
        return new PageID(); // otherwise we return an non valide page
        
    }

    // add a record to the dataPage
    public RecordId writeRecordToDataPage(Record record, PageID pageIdDataPage) {

        int sizeRecord = record.sizeRecord(); // get the size of a record
        DataPages dataPages = getDataPages(pageIdDataPage); // get the dataPage

        int posFreeSpace = dataPages.getPosFreeSpace(); // get the position where we should write the record

        ByteBuffer byteBuffer = dataPages.getByteBuffer(); // get the content of the dataPage
        System.out.println("inside function "+dataPages.toString());

        record.writeToBuffer(byteBuffer, posFreeSpace); // writte the record to the byteBuffer of the dataPage

        int numberSlot = dataPages.getNumberSlot();
        byteBuffer.position(DBParams.SGBDPageSize - (numberSlot * 8 + 8 + 8));// position of the new slot

        // add the slot to the byteBuffer of the dataPage
        byteBuffer.putInt(posFreeSpace);
        byteBuffer.putInt(sizeRecord);

        // update the number of slot and available space
        dataPages.setPosFreeSpace(posFreeSpace + sizeRecord);
        dataPages.setNumberSlot(numberSlot + 1);

        System.out.println("inside function "+dataPages.toString());

        dataPages.finalize(); 
        // check if the page is full
        if (dataPages.getAvailableSpace() <= DBParams.PageFull) {
            updateToFullPage(pageIdDataPage, record.getTableInfo());
        }

        dataPages.finalize();

        return new RecordId(pageIdDataPage, numberSlot);
    }

    // add all the record of a dataPage to a list
    public ArrayList<Record> getRecordsInDataPage(TableInfo tableInfo, PageID pageId) {

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

        dataPage.finalize(); //free the dataPage 

        return listRecords;
    }

    // get all the pageId of the dataPage of a tableInfo
    public ArrayList<PageID> getDataPages(TableInfo tableInfo) {

        ArrayList<PageID> listDataPagesId = new ArrayList<>(); // create the list
        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage of tableInfo
        PageID freeDataPageId = headerPage.getFreePage(); // get the first freePageId
        PageID fullPageId = headerPage.getFullPage(); // get the first fullpageId


        headerPage.finalize();

        while (freeDataPageId.isValid()) {
            listDataPagesId.add(freeDataPageId);
            DataPages freeDataPage = getDataPages(freeDataPageId);
            freeDataPageId = freeDataPage.getNextPage();
            freeDataPage.toString();
            freeDataPage.finalize();
        }

        while (fullPageId.isValid()) {
            listDataPagesId.add(fullPageId);
            DataPages fullDataPage = getDataPages(fullPageId);
            fullPageId = fullDataPage.getNextPage();
            fullDataPage.toString();
            fullDataPage.finalize();
        }
        
        return listDataPagesId;
    }

    // insert a record into a relation
    public RecordId InsertRecordIntoTable(Record record) {

        DiskManager diskManager=DiskManager.getDiskManager();
        //BufferManager bufferManager=BufferManager.getBufferManager();

        TableInfo tableInfo = record.getTableInfo(); // get the relation of a record
        PageID freePageId = getFreePageId(tableInfo, record.sizeRecord()); // get a dataPage that has enough space
        PageID currentAllocatedPageId=diskManager.getCurrentAllocatedPage();
        int numberCurrentAllocated=DBParams.DMFileCount*currentAllocatedPageId.getPageIdx()+currentAllocatedPageId.getFileIdx();

        
        ArrayList<PageID> listPages=getDataPages(record.getTableInfo());
        
        if(freePageId.isValid()){  //we should allocate all page until the freePage
            if(listPages.size()>0){
                int numberAlloc=DBParams.DMFileCount*freePageId.getPageIdx()+freePageId.getFileIdx()-numberCurrentAllocated;
                for(int i=0;i<numberAlloc;i++){
                    diskManager.AllocPage();
                }
            }
        }

        if(!freePageId.isValid()){ //alocate all page
            if(listPages.size()>0){
                PageID lastPageId=listPages.get(0);
                int numberAlloc=DBParams.DMFileCount*lastPageId.getPageIdx()+lastPageId.getFileIdx()-numberCurrentAllocated;
                for(int i=0;i<numberAlloc;i++){
                    diskManager.AllocPage();
                }
            }
            
            freePageId = addDataPage(tableInfo);
        }

        RecordId recordId= writeRecordToDataPage(record, freePageId);
        //displayPages(tableInfo);
        return recordId;
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

        HeaderPage headerPage = getHeaderPageOfTableInfo(tableInfo); // get the headerPage
        PageID fullPage = headerPage.getFullPage();
        DataPages newFullPage = getDataPages(newFullPageId);

        headerPage.finalize();

        DataPages freePage = getDataPages(headerPage.getFreePage()); // get the freePage

        PageID nextFreePageId = headerPage.getFreePage();
        DataPages currentfreePages = getDataPages(nextFreePageId);

        while (!nextFreePageId.equals(newFullPageId)) { // update the nextfreePage of the previous freePage

            nextFreePageId = currentfreePages.getPageID(); // get the pageId of the next freePageId

            if (nextFreePageId.equals(newFullPageId)) {
                DataPages nextFreePage = getDataPages(nextFreePageId);
                currentfreePages.setNextPage(nextFreePage.getNextPage());
                nextFreePage.finalize();
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

        currentfreePages.finalize();
        
        newFullPage.finalize();

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

    //afficher les pages d'une relation
    public String displayPages(TableInfo tableInfo){
        String content="";
        ArrayList<PageID> listPages=getDataPages(tableInfo);
        content+="number record : "+listPages.size()+"\n";
        for(PageID page:listPages){
            DataPages dataPages=getDataPages(page);
            content+=dataPages.toString()+"\n";
        }
        return content;
    }

    //supprimer tous les fichiers du dossier DB
    public void resetFileDB(){
        DiskManager diskManager=DiskManager.getDiskManager();
        diskManager.deleteAllDBFiles();
    }


}
