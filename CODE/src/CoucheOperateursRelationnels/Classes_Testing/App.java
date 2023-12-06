package CoucheOperateursRelationnels.Classes_Testing;

import java.io.File;

import CoucheOperateursRelationnels.CreateTableCommand;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.Frame;
import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.DatabaseManager;
import CoucheAcces_Fichier.DatabaseManager;

public class App {
    public static void main(String[] args) { // class for the main method to test DB Tables 

        // Configure DBParams
        DBParams.DBPath = ".." + File.separator + ".." + File.separator + "DB";
        DBParams.DMFileCount = 4;
        DBParams.SGBDPageSize = 4096;
        DBParams.FrameCount = 10;
        DBParams.PageFull=100;

        // Your other tests with BufferManager go here...

        // Test your CreateTableCommand
        //String createTableCommand = "CREATE TABLE table1 (col1:INT,col2:FLOAT,col3:STRING(15))";
        //CreateTableCommand createTable = new CreateTableCommand(createTableCommand);

        //test with CREATE TABLE R (X:INT,C2:FLOAT,BLA:STRING(10))
        //String createTableCommand2 = "CREATE TABLE R (X:INT,C2:FLOAT,BLA:STRING(10))";
        //CreateTableCommand createTable2 = new CreateTableCommand(createTableCommand2);


        // Print the table info
        // createTable.printTableInfo();
        // createTable2.printTableInfo();

        // reset the DB
        
        
        //databaseManager.ProcessCommand("RESETDB");

        DatabaseManager databaseManager = new DatabaseManager();
        
        
        //create a table with CREATE TABLE Profs (Nom:STRING(10),Matiere:STRING(10))
        String createTableCommand3 = "CREATE TABLE Profs (Nom:STRING(10),Matiere:STRING(10))";
        databaseManager.ProcessCommand(createTableCommand3);

        //databaseManager.Finish();

        //createTable3.printTableInfo();

        // test the INSERT command with INSERT INTO Profs VALUES (Ileana,BDDA)
        databaseManager.ProcessCommand("INSERT INTO Profs VALUES (Ileana,BDDA)");

        databaseManager.Finish(); //write everything into the database



        
    }
}


