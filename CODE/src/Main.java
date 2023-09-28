package IDE_PROJET_DJUROVIC_ALASHOUR_ARBANE_HAMMACI.CODE.src;

public class Main {
    public static void main(String[] args){
        DBParams.DBPath = "../../DB";
        DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;

        DBParams.createFile();
    }
}

