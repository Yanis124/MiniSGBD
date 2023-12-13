package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import GestionEspaceDisque_et_Buffer.BufferManager;
import GestionEspaceDisque_et_Buffer.DiskManager;

public class ResetDBCommand {
    
    private String userCommand;

    public ResetDBCommand(String userCommand) {
        this.userCommand = userCommand;
    }

    public void Execute(){

        
         // delete all the files in the DB folder
        FileManager.getFileManager().resetFileDB();

        // flush all the frames in the buffer
        BufferManager.getBufferManager().flushAll();
        
        // reset the database info
        DatabaseInfo.getInstance().resetDataBaseInfo();

        // reset the disk manager
        DiskManager.getDiskManager().resetDiskManager();
    }
}