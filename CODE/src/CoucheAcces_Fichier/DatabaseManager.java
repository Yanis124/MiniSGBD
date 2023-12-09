package CoucheAcces_Fichier;


import CoucheOperateursRelationnels.CreateTableCommand;
import GestionEspaceDisque_et_Buffer.BufferManager;
import CoucheOperateursRelationnels.ImportCommande;
import CoucheOperateursRelationnels.InsertCommand;
import CoucheOperateursRelationnels.ResetDBCommand;


public class DatabaseManager {
    
    public static  void Init(){
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

        else if(command.startsWith("INSERT INTO")){  //TODO : create a class for inserting a record 
            InsertCommand insertCommand=new InsertCommand(command);
            insertCommand.Execute();

        }
    }

}
