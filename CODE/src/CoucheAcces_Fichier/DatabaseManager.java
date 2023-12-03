package CoucheAcces_Fichier;
import GestionEspaceDisque_et_Buffer.BufferManager;



public class DatabaseManager {
    
    public void Init(){
        DatabaseInfo.getInstance().Init();
    }

    public void Finish(){ 
        
        DatabaseInfo.getInstance().Finish();
        BufferManager.getBufferManager().flushAll();

    }
    

    /*Rajoutez, dans votre application, la gestion de la commande RESETDB.
    Cette commande doit « faire un ménage général », plus particulièrement :
    • supprimer tous les fichiers du dossier DB
    • « remettre tout à 0 » dans le BufferManager et la DatabaseInfo, ainsi que potentiellement 
    dans le DiskManager.
    Pour cela, il faut vider les listes, remettre tous les compteurs et les flags à 0, etc.
    Vous pouvez faire ces « remises à 0 » dans des méthodes spécifiques, à créer sur chaque 
    classe concernée.
    */
    public void ProcessCommand(String command){
        if(command.equals("RESETDB")){
            // delete all the files in the DB folder
            FileManager.getFileManager().resetFileDB();
            // set all the flags to 0
            BufferManager.getBufferManager().flushAll();
            DatabaseInfo.getInstance().Finish();
            DatabaseInfo.getInstance().Init();
        }

    }

    


}
