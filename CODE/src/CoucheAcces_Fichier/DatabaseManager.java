package CoucheAcces_Fichier;

import CoucheOperateursRelationnels.CreateTableCommand;
import CoucheOperateursRelationnels.DeleteRecordsCommand;
import CoucheOperateursRelationnels.ImportCommande;
import CoucheOperateursRelationnels.InsertCommand;
import CoucheOperateursRelationnels.ResetDBCommand;
import CoucheOperateursRelationnels.SelectCommand;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DiskManager;


/**
 * Class DatabaseManager that manages the database
 */
public class DatabaseManager {
    
    // ----------------   Methods   ----------------

    /**
     * Method Init which is called at the beginning of the program
     * @param : nothing
     * @return void : nothing
     */
    public static  void Init(){
        DiskManager diskManager=DiskManager.getDiskManager(); //create the files of the database 
        diskManager.createFiles();
        DatabaseInfo databaseInfo=DatabaseInfo.getInstance();

        databaseInfo.Init();
    }

    /**
     * Method Finish which is called at the end of the program
     * @param : nothing
     * @return void : nothing
     */
    public static void Finish(){ 
        
        DatabaseInfo.getInstance().Finish();
        BufferManager.getBufferManager().flushAll();

    }
    
    
    /**
     * Method ProcessCommand that processes the command entered by the user
     * @param : command : the command entered by the user
     * @return void : nothing
     */
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
        
            importCommand.Execute();
            
        }

        else if(command.startsWith("INSERT INTO")){ 
            InsertCommand insertCommand=new InsertCommand(command);
            insertCommand.Execute();

        }
        
        else if (command.startsWith("SELECT")) {    // create a class for selecting a record
            SelectCommand selectCommand = new SelectCommand(command);
            
            selectCommand.Execute();
            
        }

        else if(command.startsWith("DELETE")){
            DeleteRecordsCommand deleteRecordsCommand=new DeleteRecordsCommand(command);
            
            deleteRecordsCommand.Execute();
        }
    }

}
