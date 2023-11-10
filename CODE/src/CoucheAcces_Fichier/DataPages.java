package CoucheAcces_Fichier;

import java.nio.ByteBuffer;
import GestionEspaceDisque_et_Buffer.*;

public class DataPages {
    private ByteBuffer byteBuffer;
    private PageID pageId;
    
    public DataPages(PageID pageId){

        this.pageId=pageId;  //set the pageId of the dataPage

        BufferManager bufferManager = BufferManager.getBufferManager(); //Get Buffer and Disk manager
        
        byteBuffer = bufferManager.getPage(pageId);  //the byteBuffer will containe all information (next page/ slot .....)

        PageID pageIdNext = new PageID();   //create an empty page as the next page(-1,-1)

        byteBuffer.putInt(pageIdNext.getFileIdx());
        byteBuffer.putInt(pageIdNext.getPageIdx());
    }

    //get the pageId of the dataPage
    public PageID getPageID(){
        return pageId;
    }

    //set the pageId of the dataPage
    public void getPageID(PageID pageId){
         this.pageId=pageId;
    }


   
}


