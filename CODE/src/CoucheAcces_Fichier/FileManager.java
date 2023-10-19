package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.PageID;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class FileManager {

    private static FileManager fileManager = new FileManager();
    ;

    private FileManager(){

    }

    public FileManager getFileManager(){
        return fileManager;
    }

    public PageID createNewHeaderPage(){

        DiskManager diskManager = DiskManager.getDiskManager();

        PageID pageId = diskManager.AllocPage();                    //Get PageId

        new HeaderPage(pageId);                                     //Write the page

        return pageId;                                              //Return PageId

    }

    public PageID addDataPage(TableInfo tabInfo){

        BufferManager bufferManager = BufferManager.getBufferManager();                         //Get Buffer and Disk manager
        DiskManager diskManager = DiskManager.getDiskManager();

        PageID pageId = diskManager.AllocPage();

        //tabInfo.
        return pageId;
    }
}
