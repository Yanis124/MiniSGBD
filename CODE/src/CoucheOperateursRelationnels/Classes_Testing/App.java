package CoucheOperateursRelationnels.Classes_Testing;

import java.io.File;

import GestionEspaceDisque_et_Buffer.DBParams;
import CoucheAcces_Fichier.DatabaseManager;
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

        //create a table with CREATE TABLE Profs (Nom:STRING(10),Matiere:STRING(10))
        //String createTableCommand3 = "CREATE TABLE Profs (Nom:STRING 10, Matiere:VARSTRING 13 ,Age:INT)";
        //DatabaseManager.ProcessCommand(createTableCommand3);

        
        //String importDataFromFileCommande1="IMPORT INTO Profs ../../DB/file.csv";
        //DatabaseManager.ProcessCommand(importDataFromFileCommande1);

        // // test the SELECT command with SELECT * FROM Profs

        String selectAllCommande1="SELECT * FROM Profs ";
        DatabaseManager.ProcessCommand(selectAllCommande1);
        // System.out.println("SELECT * FROM Profs");
        //DatabaseManager.ProcessCommand("SELECT * FROM Profs WHERE Age>20");

        




        // //createTable3.printTableInfo();
        //String insertDataCommande1="INSERT INTO Profs VALUES (ivan,cyber,23)";
        // // test the INSERT command with INSERT INTO Profs VALUES (Ileana,BDDA)
        //DatabaseManager.ProcessCommand(insertDataCommande1);

        //DatabaseManager.Finish(); //write everything into the database

    }
}



