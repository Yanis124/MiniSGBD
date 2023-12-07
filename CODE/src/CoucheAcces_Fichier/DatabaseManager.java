package CoucheAcces_Fichier;
import CoucheOperateursRelationnels.CreateTableCommand;
import CoucheOperateursRelationnels.Import;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DiskManager;

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
        if(command.equals("RESETDB")){  //TODO : create a class for resetDataBase a record
            
            // delete all the files in the DB folder
            FileManager.getFileManager().resetFileDB();

            // flush all the frames in the buffer
            BufferManager.getBufferManager().flushAll();
            
            // reset the database info
            DatabaseInfo.getInstance().resetDataBaseInfo();

            // reset the disk manager
            DiskManager.getDiskManager().resetDiskManager();
        }
        //create a realtion
        else if(command.startsWith("CREATE TABLE")){
            CreateTableCommand createTableCommand=new CreateTableCommand(command);
            createTableCommand.Execute();
        }
        //import a set of records from a file
        else if(command.startsWith("IMPORT INTO")){
            Import importCommand=new Import(command);
            importCommand.printTableInfo();
            importCommand.Execute();
        }

        else if(command.startsWith("INSERT INTO")){  //TODO : create a class for inserting a record 
            String[] commandSplit = command.split(" ");
            String relationName = commandSplit[2];
            
            String values = commandSplit[4];
            String[] valuesSplit = values.split(",");

            
            TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(relationName);
            System.out.println("Pour vérifier le tableInfo");
            System.out.println(tableInfo);
            Record record = new Record(tableInfo);
            for(int i = 0; i < valuesSplit.length; i++){
                record.getRecValues().add(valuesSplit[i]);
            }
            
            FileManager.getFileManager().InsertRecordIntoTable(record);
        }
    }

    //check if a relation exist in the database
    public boolean relationExists(String relationName){
        return true;
    }

    


}
