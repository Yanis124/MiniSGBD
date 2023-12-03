package CoucheOperateursRelationnels.Classes_Testing;

import java.io.File;

import CoucheOperateursRelationnels.CreateTableCommand;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.Frame;
import CoucheAcces_Fichier.DatabaseManager;

public class App {
    public static void main(String[] args) { // class for the main method to test DB Tables 

        // Configure DBParams
        DBParams.DBPath = ".." + File.separator + ".." + File.separator + "DB";
        DBParams.DMFileCount = 4;
        DBParams.SGBDPageSize = 4096;
        DBParams.FrameCount = 2;

        // Create DiskManager and BufferManager
        DiskManager.getDiskManager();
        BufferManager bufferManager = BufferManager.getBufferManager();

        // Your other tests with BufferManager go here...

        // Test your CreateTableCommand
        String createTableCommand = "CREATE TABLE table1 (col1:INT,col2:FLOAT,col3:STRING(15))";
        CreateTableCommand createTable = new CreateTableCommand(createTableCommand);

        //test with CREATE TABLE R (X:INT,C2:FLOAT,BLA:STRING(10))
        String createTableCommand2 = "CREATE TABLE R (X:INT,C2:FLOAT,BLA:STRING(10))";
        CreateTableCommand createTable2 = new CreateTableCommand(createTableCommand2);


        // Print the table info
        createTable.printTableInfo();
        createTable2.printTableInfo();

        // Get a frame with the smallest pin count from BufferManager
        Frame frame = bufferManager.getFrameWithSmallestPinCount();

        if (frame != null) {
            // Do something with the frame
            // Use the createTable variable here
            System.out.println(createTable.toString());
        } else {
            System.out.println("Frame is null");
        }

        // reset the DB
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.ProcessCommand("RESETDB");
        

    }
}



