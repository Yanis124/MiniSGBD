package CoucheAcces_Fichier;
import CoucheOperateursRelationnels.CreateTableCommand;
import CoucheOperateursRelationnels.InsertCommand;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DiskManager;

public class DatabaseManager {
    
    public void Init(){
        DatabaseInfo.getInstance().Init();
    }

    public void Finish(){ 
        
        DatabaseInfo.getInstance().Finish();
        BufferManager.getBufferManager().flushAll();

    }
    
    
    
    public void ProcessCommand(String command){
        if(command.equals("RESETDB")){
            
            // delete all the files in the DB folder
            FileManager.getFileManager().resetFileDB();

            // flush all the frames in the buffer
            BufferManager.getBufferManager().flushAll();
            
            // reset the database info
            DatabaseInfo.getInstance().resetDataBaseInfo();

            // reset the disk manager
            DiskManager.getDiskManager().resetDiskManager();
        }
        //ajouter les differentes commandes 
        else if(command.startsWith("CREATE TABLE")){
            CreateTableCommand createTableCommand=new CreateTableCommand(command);
            createTableCommand.Execute();
        }

        else if(command.startsWith("INSERT INTO")){  // create a class for inserting a record 
            InsertCommand insertCommand=new InsertCommand(command);
            insertCommand.Execute();
        }

        
    }
    


}
