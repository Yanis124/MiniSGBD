// package GestionEspaceDisque_et_Buffer;

// import java.nio.*;

// public class Main {
    
//     public static void main(String[] args) {
//         DBParams.DBPath = "../../DB";
//         DBParams.SGBDPageSize = 4096;
//         DBParams.DMFileCount = 4; // max number of file in DB
//         DBParams.FrameCount = 2;

//         DiskManager diskManager = DiskManager.getDiskManager();
//         // ArrayList<Integer> list = new ArrayList<Integer>();
//         // diskManager.AllocPage();
//         // diskManager.AllocPage();
//         // diskManager.AllocPage();
//         // diskManager.AllocPage();
//         // diskManager.AllocPage();
//         // diskManager.AllocPage();
//         // diskManager.AllocPage();
//         // PageID page=diskManager.AllocPage();
        

//         //  String dataToAppend = "This data will be appended to the file " +page.getFileIdx() + " page : "+ page.getPageIdx();
//         // byte[] dataBytes = dataToAppend.getBytes(); // convert string to a byte code
//         // ByteBuffer buffWrite = ByteBuffer.wrap(dataBytes);
//         // diskManager.WritePage(page, buffWrite);

//         // ByteBuffer buffRead = ByteBuffer.allocate(DBParams.SGBDPageSize);
//         // buffRead=diskManager.ReadPage(page, buffRead);
//         // diskManager.readContentOfBuffer(buffRead);

//         PageID page0=new PageID(0,0);
//         PageID page1=new PageID(1,0);
//         PageID page2=new PageID(2,0);
//         PageID page3=new PageID(3,0);

//         BufferManager bm=BufferManager.getBufferManager();
//         bm.getPage(page0);
//         bm.getPage(page0);
//         bm.getPage(page1);
//         bm.getPage(page2); // The frame's page that currently have page 1 will have page 2
//         bm.getPage(page3);
//         bm.displaySatetOfFrames();
        
           
            
        

       

       // }

//}
