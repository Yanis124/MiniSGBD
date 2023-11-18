package CoucheAcces_Fichier;
import GestionEspaceDisque_et_Buffer.*;

import java.nio.ByteBuffer;


public class HeaderPage {

    private ByteBuffer byteBuffer;
    private PageID pageId;

    public HeaderPage(PageID pageId){

        this.pageId = pageId;

        BufferManager bufferManager = BufferManager.getBufferManager();                         //Get Buffer and Disk manager
        //DiskManager diskManager = DiskManager.getDiskManager();

        byteBuffer = bufferManager.getPage(pageId);

        PageID freePageId = new PageID();  //the page with free space
        PageID fullPageId = new PageID();   //the full page

        byteBuffer.putInt(freePageId.getFileIdx());   //write the pageId of both page in a byteBuffer
        byteBuffer.putInt(freePageId.getPageIdx());
        byteBuffer.putInt(fullPageId.getFileIdx());
        byteBuffer.putInt(fullPageId.getPageIdx());
    }

    //create a headerPage with the content of the byteBuffer
    public HeaderPage(ByteBuffer byteBuffer,PageID pageId){
        this.byteBuffer=byteBuffer;
        this.pageId=pageId;
    }

    /*
            Le HeaderPage a dans son buffer les 2 pagesID des têtes de 2 autres directory page
            Format XXXX
            2 premier X FreePage
            2 dernier X FullPage


     */
    // set the pageId of the freePage
    public void setFreePage(PageID pageId){
        int pos = 0;
        byteBuffer.position(pos);
        int freeFileIdx=pageId.getFileIdx();
        int freePageIdx=pageId.getPageIdx();

        byteBuffer.putInt(freeFileIdx);
        byteBuffer.putInt(freePageIdx);
    }

    // set the pageId of the fullPage
    public void setFullPage(PageID pageId){
        int pos = 8;
        byteBuffer.position(pos);
        int fullFileIdx=pageId.getFileIdx();
        int fullPageIdx=pageId.getPageIdx();

        byteBuffer.putInt(fullFileIdx);
        byteBuffer.putInt(fullPageIdx);
    }

    // get the pageId of the freePage
    public PageID getFreePage(){

        int pos = 0;
        byteBuffer.position(pos);
        return new PageID(byteBuffer.getInt(),byteBuffer.getInt());
    }

    //get the PageId of the fullPage
    public PageID getFullPage(){
        int pos = 8;
        byteBuffer.position(pos);
        return new PageID(byteBuffer.getInt(),byteBuffer.getInt());
    }

    //get the pageId of the headerPage
    public PageID getPageID(){
        return pageId;
    }

    //set the pageId of the headerPage
    public void setPageID(PageID pageId){
         this.pageId=pageId;
    }

    //return the content of a created HeaderPage
    public static ByteBuffer getHeaderPage(PageID pageId){

        BufferManager bufferManager=BufferManager.getBufferManager();
        
        return bufferManager.getPage(pageId); 
    }

    // //free the headerPage and write its content 
    // public void finalize(PageID pageId){

    //     if(this.pageId.equals(pageId)) {

    //         BufferManager bufferManager = BufferManager.getBufferManager();                     
    //         bufferManager.freePage(pageId, true);
    //     }
    //     else
    //         System.out.println("Pas le meme page ID");
    // }

}
