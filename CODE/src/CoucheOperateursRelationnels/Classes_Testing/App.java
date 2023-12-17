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

        //create a table with CREATE TABLE Profs (Nom:STRING(10),Matiere:STRING(10))
        //String createTableCommand3 = "CREATE TABLE Personne (Nom:STRING 10, Prenom:VARSTRING 13, Age:INT, Salaire:STRING 10)";
        //DatabaseManager.ProcessCommand(createTableCommand3);

         //String createTableCommand4 = "CREATE TABLE Employe (Nom:STRING 10, Prenom:VARSTRING 13, Age:INT)";
        //DatabaseManager.ProcessCommand(createTableCommand4);

        
        //String importDataFromFileCommande1="IMPORT INTO Profs ../../DB/file.csv";
        //DatabaseManager.ProcessCommand(importDataFromFileCommande1);

        // // test the SELECT command with SELECT * FROM Profs

        String selectAllCommande1="SELECT * FROM Personne,Employe WHERE Personne.Nom=Employe.Nom AND Personne.Prenom=Employe.Prenom AND Personne.Age=Employe.Age"; 
        DatabaseManager.ProcessCommand(selectAllCommande1);
         //System.out.println("SELECT * FROM Profs");
        //DatabaseManager.ProcessCommand("DELETE * FROM Profs WHERE Profs.Age>40");

        




        // //createTable3.printTableInfo();
        // String insertDataCommande1="INSERT INTO Personne VALUES (lomeni,HAMMACI,55,10000)";
        // String insertDataCommande2="INSERT INTO Personne VALUES (soto,ZERIOUL,64,12000)";

        // String insertDataCommande3="INSERT INTO Employe VALUES (lomeni,HAMMACI,55)";
        // String insertDataCommande4="INSERT INTO Employe VALUES (soto,ZERIOUL,64)";
        // // // // // test the INSERT command with INSERT INTO Profs VALUES (Ileana,BDDA)
        // DatabaseManager.ProcessCommand(insertDataCommande1);
        // DatabaseManager.ProcessCommand(insertDataCommande2);
        // DatabaseManager.ProcessCommand(insertDataCommande3);
        // DatabaseManager.ProcessCommand(insertDataCommande4);

        //DatabaseManager.Finish(); //write everything into the database

    }
}



