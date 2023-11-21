package GestionEspaceDisque_et_Buffer;

import java.io.File;
import java.io.IOException;

public class DBParams {
    public static String DBPath ;
    public static int SGBDPageSize;
    public static int DMFileCount;
    public static int NumFichier;
    public static int FrameCount;
    public static int PageFull;

    public static int createFile() {
        NumFichier = 0;
        String filePatheName;
        while (true) {
            filePatheName = DBPath + "/F" + NumFichier + ".data";
            File file = new File(filePatheName);
            if (!file.exists()) {
                try {
                    if (file.createNewFile()) {
                        System.out.println("Le fichier a été créé" + filePatheName);
                    } else {
                        System.out.println("Erreur lors de la création du fichier !");
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                break;
            } else {
                NumFichier++;
            }

        }
        return NumFichier;
    }

}