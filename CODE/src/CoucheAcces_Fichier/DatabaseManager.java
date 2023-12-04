package CoucheAcces_Fichier;
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
        else if(command.startsWith("INSERT INTO")){
            String[] commandSplit = command.split(" ");
            String relationName = commandSplit[2];
            System.out.println("Pour vérifier le nom de la relation");
            System.out.println(relationName);
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

    


}
