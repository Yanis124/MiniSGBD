package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.PageID;

import java.nio.ByteBuffer;

public class FileManager {

    private static FileManager fileManager = new FileManager();
    
    private FileManager(){
        
    }

    public static FileManager getFileManager(){
        return fileManager;
    }

    //create a new headerPage
    public PageID createNewHeaderPage(){

        DiskManager diskManager=DiskManager.getDiskManager(); 
        BufferManager bufferManager=BufferManager.getBufferManager();

        PageID pageId=diskManager.AllocPage(); //allocate a page

        HeaderPage headerPage=new HeaderPage(pageId); //create a headerPage with the allocated page

        bufferManager.freePage(pageId, true);  //write the free page and full page of the headerPage

        return pageId;                                              //Return PageId
    }

    //add a dataPage to the tableInfo
    public PageID addDataPage(TableInfo tableInfo){

        DiskManager diskManager=DiskManager.getDiskManager(); 
        PageID pageId=diskManager.AllocPage();  //allocate a page

        BufferManager bufferManager=BufferManager.getBufferManager();

        DataPages dataPages=new DataPages(pageId);  //create the new dataPage
        
        HeaderPage headerPage=getHeaderPageOfTableInfo(tableInfo); //get the content of headerPage of tableInfo

        PageID freePageId =headerPage.getFreePage();  //get the currentFreePage
        dataPages.setNextPage(freePageId);   //link the newFreeDataPages to the currentFreeDataPages
        headerPage.setFreePage(pageId);  //set the  newFreeDataPages as the currentFreeDataPages

        bufferManager.freePage(pageId,true); //write the new created page 
        
        return pageId;
    }

    //return the dataPages that has enough space to insert the record
    public PageID getFreePageId(TableInfo tableInfo,int sizeRecord){

        HeaderPage headerPage=getHeaderPageOfTableInfo(tableInfo); //get the headerPage of tableInfo
        DataPages freePageId=getDataPages(headerPage.getFreePage());  //get the first freePage of a tableInfo

        while(freePageId.getAvailableSpace()<=sizeRecord && freePageId.getPageID().isValid()){  //while the freePage doesn't have enough space we check the next
            
            freePageId=getDataPages(freePageId.getNextPage()); // get the next freePage
        }

        if(freePageId.getPageID().isValid()){  //if there is a dataPage that has enought space we return it 
            return freePageId.getPageID();
        }
        return null;  //otherwise we return null

    }

     //add a record to the dataPage
    public RecordId writeRecordToDataPage(Record record,PageID PageIdDataPage){

        BufferManager bufferManager=BufferManager.getBufferManager();

        int sizeRecord=record.sizeRecord();  //get the size of a record
        DataPages dataPages=getDataPages(PageIdDataPage);  //get the dataPage

        if(sizeRecord+8>dataPages.getAvailableSpace()){  //+8 because every time we add a record that take n space we should also add a slot that take 8 bytes so the total space that our page should has is n+8
            System.out.println("not enough space");  //must be written in another page
            return null;
        }

        //if there is enough space we wtite it in this page

        int posFreeSpace=dataPages.getPosFreeSpace(); //get the position where we should write the record
        ByteBuffer byteBuffer=dataPages.getByteBuffer(); //get the content of the dataPage

        record.writeToBuffer(byteBuffer, posFreeSpace);  //writte the record to the byteBuffer of the dataPage

        int numberSlot=dataPages.getNumberSlot();
        byteBuffer.position(DBParams.SGBDPageSize-(numberSlot*8+8+8));// position of the new slot

        //add the slot to the byteBuffer of the dataPage
        byteBuffer.putInt(posFreeSpace);
        byteBuffer.putInt(sizeRecord);

        //update the number of slot and available space
        dataPages.setNumberSlot(numberSlot+1);
        dataPages.setPosFreeSpace(posFreeSpace+sizeRecord);

        bufferManager.freePage(PageIdDataPage, true);  //we edit the page so we should write it

        return new RecordId(PageIdDataPage, numberSlot);  
    }



    //create the headerPage with the content of pageId
    private HeaderPage getHeaderPageOfTableInfo(TableInfo tableInfo){

        PageID pageIdHeaderPage=tableInfo.getHeaderPageId();  //get the pageId of headerPage
        ByteBuffer byteBufferHeaderPage=HeaderPage.getHeaderPage(pageIdHeaderPage); //get the content of the PageIdHeaderPage
        HeaderPage headerPage=new HeaderPage(byteBufferHeaderPage,pageIdHeaderPage); //create a headerPage with the content of the byteBuffer
        
        return headerPage;
    }

    //create a dataPage based on the content of its page
    private DataPages getDataPages(PageID pageId){

        ByteBuffer byteBuffer=DataPages.getDataPage(pageId);  //get the content of the dataPage
        DataPages dataPages=new DataPages(byteBuffer,pageId);  //create the dataPage with the content of the byteBuffer

        return dataPages;

    }


}
