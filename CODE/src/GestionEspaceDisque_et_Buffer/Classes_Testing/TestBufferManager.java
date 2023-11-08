package GestionEspaceDisque_et_Buffer.Classes_Testing;

import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.PageID;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import java.nio.ByteBuffer;

public class TestBufferManager {
    public static void main(String[] args){
        DBParams.DBPath="../../DB";
        DBParams.DMFileCount=4;
        DBParams.SGBDPageSize=4096;
        DBParams.FrameCount=2;

        DiskManager diskManager=DiskManager.getDiskManager();
        BufferManager bufferManager=BufferManager.getBufferManager();

        String dataToAppend = "this data should be appended to the file 1 page 1";
        byte[] dataBytes = dataToAppend.getBytes(); // convert string to a byte code
 

        PageID page1=diskManager.AllocPage();
        ByteBuffer byteBuffer=bufferManager.getPage(page1);
        //ByteBuffer byteBuffer = ByteBuffer.wrap(dataBytes);  //ajouter le contenu de dataToAppend a byteBuffer
        System.out.println(diskManager.readContentOfBuffer(byteBuffer));
        //diskManager.WritePage(page1, byteBuffer);

        // String dataToAppend2 = "this data should be appended to the file 1 page 1";
        // byte[] dataBytes2 = dataToAppend2.getBytes(); // convert string to a byte code
        // ByteBuffer buffWrite2 = ByteBuffer.wrap(dataBytes2);
        
        PageID page2=diskManager.AllocPage();
        byteBuffer=bufferManager.getPage(page2);
        System.out.println(diskManager.readContentOfBuffer(byteBuffer));
        //byteBuffer=ByteBuffer.wrap(dataBytes);
        //diskManager.WritePage(page2, byteBuffer);

        PageID page3=diskManager.AllocPage();
        bufferManager.getPage(page3);
        System.out.println(diskManager.readContentOfBuffer(byteBuffer));

        bufferManager.displaySatetOfFrames();







    }
}
