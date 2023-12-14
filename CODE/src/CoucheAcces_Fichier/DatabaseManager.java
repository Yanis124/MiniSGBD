package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.DiskManager;
import CoucheOperateursRelationnels.CreateTableCommand;
import GestionEspaceDisque_et_Buffer.BufferManager;
import CoucheOperateursRelationnels.ImportCommande;
import CoucheOperateursRelationnels.InsertCommand;
import CoucheOperateursRelationnels.SelectCommand;
import CoucheOperateursRelationnels.ResetDBCommand;


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
            
            importCommand.printTableInfo();
            importCommand.Execute();
        }

        else if(command.startsWith("INSERT INTO")){ 
            InsertCommand insertCommand=new InsertCommand(command);
            insertCommand.Execute();

        }
        
        else if (command.startsWith("SELECT")) {    // create a class for selecting a record
            SelectCommand selectCommand = new SelectCommand(command);
            System.out.println("rueghiurehgiuheri");
            selectCommand.Execute();
            
        }
    }

}
