package CoucheAcces_Fichier;

import CoucheOperateursRelationnels.CreateTableCommand;
import CoucheOperateursRelationnels.DeleteRecordsCommand;
import CoucheOperateursRelationnels.ImportCommande;
import CoucheOperateursRelationnels.InsertCommand;
import CoucheOperateursRelationnels.ResetDBCommand;
import CoucheOperateursRelationnels.SelectCommand;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DiskManager;


public class DatabaseManager {
    
    
    public static  void Init(){
        DiskManager.getDiskManager(); //create the files of the database 
        DatabaseInfo databaseInfo=DatabaseInfo.getInstance();

        databaseInfo.Init();
    }

    public static void Finish(){ 
        
        DatabaseInfo.getInstance().Finish();
        BufferManager.getBufferManager().flushAll();

    }
    
    
    
    public static void ProcessCommand(String command){
        if(command.equals("RESETDB")){
            System.out.print("yes"); 
           ResetDBCommand  resetDBCommande=new ResetDBCommand(command);
           resetDBCommande.Execute();
           

        }
        //create a realtion
        else if(command.startsWith("CREATE TABLE")){
            CreateTableCommand createTableCommand=new CreateTableCommand(command);

            createTableCommand.Execute();
        }
        //import a set of records from a file
        else if(command.startsWith("IMPORT INTO")){
            ImportCommande importCommand=new ImportCommande(command);
            importCommand.Execute();
        }

        else if(command.startsWith("INSERT INTO")){ 
            InsertCommand insertCommand=new InsertCommand(command);
            insertCommand.Execute();

        }
        
        else if (command.startsWith("SELECT")) {    // create a class for selecting a record
            SelectCommand selectCommand = new SelectCommand(command);
            
            
            System.out.print("yes");
            selectCommand.Execute();
            
        }

        else if(command.startsWith("DELETE")){
            DeleteRecordsCommand deleteRecordsCommand=new DeleteRecordsCommand(command);
            
            deleteRecordsCommand.Execute();
        }
    }

}
