package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.PageID;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;


public class FileManager {

    private static FileManager fileManager = new FileManager();
    private HeaderPage headerPage;
    
    
    private FileManager(){
        
    }

    public static FileManager getFileManager(){
        return fileManager;
    }

    //create a new headerPage
    public PageID createNewHeaderPage(){

        DiskManager diskManager=DiskManager.getDiskManager(); //allocate a page
        PageID pageId=diskManager.AllocPage();

        headerPage=new HeaderPage(pageId); //create a headerPage

        return pageId;                                              //Return PageId
    }

    //add a dataPage
    public PageID addDataPage(TableInfo tableInfo){
        DiskManager diskManager=DiskManager.getDiskManager();
        PageID pageId=diskManager.AllocPage();

        DataPages dataPages=new DataPages(pageId);
        HeaderPage headerPageId=new HeaderPage(tableInfo.getHeaderPageId()); //get the headerPage of tableInfo
        
        PageID freePageId =headerPageId.getFreePage();  //get the currentFreePage
        dataPages.setNextPage(freePageId);   //set the next page
        headerPageId.setFreePage(pageId);  

        headerPageId.finalize(tableInfo.getHeaderPageId());
        
        return pageId;
    }

    public PageID getFreePageId(TableInfo tableInfo,int sizeRecord){

        HeaderPage headerPage=new HeaderPage(tableInfo.getHeaderPageId()); //get the headerPage of tableInfo
        DataPages freePageId=new DataPages(headerPage.getFreePage());

        PageID currentNextFreePage=freePageId.getNextPage();
        do{
            
            if(freePageId.getAvailableSpace()>=sizeRecord){

                return freePageId.getPageID();
            }
            freePageId.setPageID(currentNextFreePage);
            currentNextFreePage= freePageId.getNextPage();   // a refaire

        }while(currentNextFreePage.isValid());

    }

     //add a record to the dataPage
    public void addRecord(Record record,PageID dataPage){


        int sizeRecord=record.sizeRecord();
        if(sizeRecord+8>dataPage.getAvailableSpace()){
            System.out.println("not enough space");  //must be written in another page
            return ;
        }
        int posFreeSpace=getPosFreeSpace();
        record.writeToBuffer(byteBuffer, posFreeSpace);  //writte the record

        int numberSlot=getNumberSlot();
        byteBuffer.position(DBParams.SGBDPageSize-(numberSlot*8+8+8));// position of the new slot

        //add the slot
        byteBuffer.putInt(posFreeSpace);
        byteBuffer.putInt(sizeRecord);

        //update the number of slot and available space
        setNumberSlot(numberSlot+1);
        setPosFreeSpace(posFreeSpace+sizeRecord);
    }

    public HeaderPage getHeaderPage(){
        return headerPage;
    }


}
