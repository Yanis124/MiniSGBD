package IDE_PROJET_DJUROVIC_ALASHOUR_ARBANE_HAMMACI.CODE.src;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DiskManager {
    
    public static ArrayList<PageID> PidLibres;
    public static PageID LastPid;

    public static void createFile(){
        DBParams.NumFichier = 0;
        String filePatheName;
        while (true){
            filePatheName = DBParams.DBPath + "/F" + DBParams.NumFichier + ".data";
            File file = new File(filePatheName);
            if (!file.exists()){
                try{
                    if (file.createNewFile()){
                        System.out.println("Le fichier a été créé" + filePatheName);
                    }else{
                        System.out.println("Erreur lors de la création du fichier !");
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                break;
            }else{
                DBParams.NumFichier++;
            }

        }
    }

    PageID AllocPage(){
        
        if(PageID.PidLibres.length())
    }
}
