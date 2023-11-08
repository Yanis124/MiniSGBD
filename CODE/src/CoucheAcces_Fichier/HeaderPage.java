package CoucheAcces_Fichier;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.PageID;

import java.nio.ByteBuffer;


public class HeaderPage {

    private ByteBuffer byteBuffer;
    private PageID pageId;

    public HeaderPage(PageID pageId){

        BufferManager bufferManager = BufferManager.getBufferManager();                         //Get Buffer and Disk manager
        DiskManager diskManager = DiskManager.getDiskManager();

        byteBuffer = bufferManager.getPage(pageId);

        PageID pageId1 = new PageID();
        PageID pageId2 = new PageID();

        String str = "" + pageId1.getFileIdx()  + pageId1.getPageIdx()                          //Write in to the buffer
                + pageId2.getFileIdx() + pageId2.getPageIdx();
        byte[] byteStr= str.getBytes();
        byteBuffer = ByteBuffer.wrap(byteStr);

        diskManager.WritePage(pageId, byteBuffer);
        this.pageId = pageId;

    }

    /*
            Le HeaderPage a dans son buffer les 2 pagesID des têtes de 2 autres directory page
            Format XXXX
            2 premier X FreePage
            2 dernier X FullPage


     */
    public void setFreePage(PageID pageId){

        int pos = 0;



    }

    public void setFullPage(PageID pageId){
        int pos = 8;

    }

    public PageID getFreePage(){

        int pos = 0;

    }

    public PageID getFullPage(){
        int pos = 8;

    }

    public void finalize(PageID pageId){

        if(this.pageId.compareTo(pageId)) {
            BufferManager bufferManager = BufferManager.getBufferManager();                         //Get Buffer and Disk manager
            bufferManager.freePage(pageId, true);
        }
        else
            System.out.println("Pas le meme page ID");
    }


    //public PageID getFullPage(){



    //}
}
