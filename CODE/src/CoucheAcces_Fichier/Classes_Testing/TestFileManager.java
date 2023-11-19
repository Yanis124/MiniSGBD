package CoucheAcces_Fichier.Classes_Testing;

import GestionEspaceDisque_et_Buffer.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import CoucheAcces_Fichier.*;

public class TestFileManager {

    public static void main(String[] args){
        DBParams.DBPath = "../../DB";
        DBParams.DMFileCount = 4;
        DBParams.SGBDPageSize = 4096;
        DBParams.FrameCount = 2;

        FileManager fileManager=FileManager.getFileManager();
        BufferManager bufferManager=BufferManager.getBufferManager();
        
        // test createNewHeaderPage

        PageID headerPageId=fileManager.createNewHeaderPage(); //create a headerPage (0,0)
        System.out.println(headerPageId.toString());

        //bufferManager.flushAll();  //test wether the page is in memory or disk

        ByteBuffer byteBuffer=bufferManager.getByteBufferPage(headerPageId); //get the content of the byteBuffer
        HeaderPage headerPage=new HeaderPage(byteBuffer, headerPageId); //get the headerPage
        System.out.println(headerPage.getFreePage().toString());  //we should get (-1,-1) (-1,-1) free and full page
        System.out.println(headerPage.getFullPage().toString());  //we should get (-1,-1) (-1,-1) free and full page

        //test addDataPage
         ArrayList<ColInfo> tableCols = new ArrayList<>();
        tableCols.add(new ColInfo("Column1", ColumnType.INT, 0)); 
        tableCols.add(new ColInfo("Column2", ColumnType.FLOAT, 0)); 
        tableCols.add(new ColInfo("Column3", ColumnType.STRING, 15)); 
        tableCols.add(new ColInfo("Column4", ColumnType.VARSTRING, 50)); 

        TableInfo tableInfo = new TableInfo("Table1", 4, tableCols,headerPageId); //Create the tableInfo

        PageID dataPageId=fileManager.addDataPage(tableInfo);
        ByteBuffer byteBufferHeaderPage=bufferManager.getByteBufferPage(headerPageId); //get the content of the byteBuffer
        HeaderPage headerPagePage=new HeaderPage(byteBufferHeaderPage, headerPageId); //get the headerPage
        System.out.println("the data page : "+dataPageId); //add the dataPage (1,0) that was added
        System.out.println("the free page :  "+headerPagePage.getFreePage().toString());  //the free page (1,0)
        
        DataPages dataPages=new DataPages(dataPageId);
        System.out.println("the next page of the data page : "+dataPages.getNextPage().toString()); //the next page is (-1,-1)





        

        


    }
}