package CoucheOperateursRelationnels.Classes_Testing;

import CoucheAcces_Fichier.DatabaseManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import java.io.File;

public class App {
    public static void main(String[] args) { // class for the main method to test DB Tables 

        // Configure DBParams
        DBParams.DBPath = ".." + File.separator + ".." + File.separator + "DB";
        DBParams.DMFileCount = 4;
        DBParams.SGBDPageSize = 4096;
        DBParams.FrameCount = 10;
        DBParams.PageFull=100;
        DBParams.nbPageFile=50;

        
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
        
        
        

        DatabaseManager.Init(); 

        //DatabaseManager.ProcessCommand("RESETDB"); 

        
        //String createFirstTable= "CREATE TABLE R (C1:INT,C2:VARSTRING(3),C3:INT)";
        //DatabaseManager.ProcessCommand(createFirstTable);

        //String insertCommande="INSERT INTO R VALUES (2,ac,2)";
        //DatabaseManager.ProcessCommand(insertCommande);

        //String insertSecondRecods="INSERT INTO R VALUES (1,aab,2)";
        //DatabaseManager.ProcessCommand(insertSecondRecods);

        //String createSecondTable="CREATE TABLE S (AA:INT,BB:INT)";
        //DatabaseManager.ProcessCommand(createSecondTable);

        // String insertfirstRecords="INSERT INTO S VALUES (1,2)";
        // String insertSecondRecods="INSERT INTO S VALUES (3,2)";
        // String insertThirsdRecods="INSERT INTO S VALUES (4,5)";
        // DatabaseManager.ProcessCommand(insertfirstRecords);
        // DatabaseManager.ProcessCommand(insertSecondRecods);
        // DatabaseManager.ProcessCommand(insertThirsdRecods);

        //String firstSelect="SELECT * FROM R,S WHERE R.C1=S.AA";
        //DatabaseManager.ProcessCommand(firstSelect);

        String secondSelect="SELECT * FROM R,S WHERE R.C3=S.BB AND R.C1<=S.BB";
        DatabaseManager.ProcessCommand(secondSelect);
        

        
        //DatabaseManager.Finish(); //write everything into the database

    }
}



