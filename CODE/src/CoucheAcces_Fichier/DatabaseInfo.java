package CoucheAcces_Fichier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import GestionEspaceDisque_et_Buffer.DBParams;
import GestionEspaceDisque_et_Buffer.PageID;


public class DatabaseInfo implements Serializable {
    // this class should have only and only a unique instance that will be initiallized in the later TPs
    private static DatabaseInfo instance=new DatabaseInfo();
    private String databaseInfoFile="DBInfo.save";
    private ArrayList<TableInfo> informationTable;
    private int counterRelations;

    // constructeur
    private DatabaseInfo() {
        informationTable = new ArrayList<>();
        counterRelations = 0;
    }

    // methods

    //create a new databaseInfo instance
    // public static DatabaseInfo createInstance() {
    //         instance = new DatabaseInfo();
    //         instance.Init();  //get all relation of the database
    //         System.out.println("New instance of DBInfo created");
        
    //     System.out.println("Instance of DBInfo returned");
    //     return instance;
    // }

    //get an existing databaseInfo instance
    public static DatabaseInfo getInstance() {
       return instance;
    }

    // get PageID of the header page of a relation
    public PageID getHeaderPageID(String relationName) {
        TableInfo tableInfo = GetTableInfo(relationName);
        return tableInfo.getHeaderPageId();
    }

    public void Init() {
        Load();
    } // initialize an instance

    public void Finish() {
        Save();
    } // for cleaning

    public void AddTableInfo(TableInfo tabInfo) {
        informationTable.add(tabInfo);
        System.out.println("TableInfo added to DBInfo");
        counterRelations++;
    }

    public TableInfo GetTableInfo(String nameRelation) { // gives us information (from TableInfo) about
        // a certain relation depending on a given name in argument
        for (TableInfo tabInfo : informationTable) { // a for each loop
            System.out.println("Nom de la relation :");
            System.out.println(tabInfo.getNameRelation());
            if ((tabInfo.getNameRelation()).equals(nameRelation)) {
                System.out.println("On a trouvé une relation avec ce nom ");
                return tabInfo;
            }
        }
        return null; // return null if none of them matches the given relation's name in the argument
    }

    public int GetCounterRelations() {
        return counterRelations;
    }
    // Method to save DBinfo informations in a file
    public void Save() {
        try {
            // Complete path to the directory DB (depend on how you run the code)
            String filePath = DBParams.DBPath + File.separator + databaseInfoFile; // Combine the directory with fileName

            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fileOut);
            oos.writeObject(this);
            oos.close();
            fileOut.close();

            System.out.println("Data has been written to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load DBinfo informations starting from a file
    public void Load() {
        try {
            String filePath = DBParams.DBPath + File.separator + databaseInfoFile;
            File file = new File(filePath);
    
            if (file.exists() && file.length() >0) {
                FileInputStream fileIn = new FileInputStream(filePath);
                ObjectInputStream ois = new ObjectInputStream(fileIn);
                DatabaseInfo dbInfo = (DatabaseInfo) ois.readObject();
                ois.close();
                fileIn.close();
                
                // Mettez à jour l'instance actuelle avec les nouvelles informations chargées
                instance = dbInfo;
                System.out.println("Data has been loaded to unique instance of DBinfo (updated)");
            } else {
                //if the file doesn't exist we create it
                file = new File(filePath);
                file.createNewFile();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    

    //check if the data of the database has been written in DBInfo.sava file
    public String toString(){
        String informationDatabase="";
        System.out.println("size of relation : "+informationTable.size());
        for(TableInfo table: informationTable ){
            informationDatabase+=table.toString();
            informationDatabase+="\n";
        }
        return informationDatabase;
    }

    /*« remettre tout à 0 » dans DataBaseinfo.*/
    public void resetDataBaseInfo(){
        informationTable = new ArrayList<>();
        counterRelations = 0;
        deleteDBInfoFile();
    }

    //delete the file DBInfo.save 
    private void deleteDBInfoFile(){
        String filePath = DBParams.DBPath + File.separator + databaseInfoFile;
        File file = new File(filePath);

        if (file.exists()) {
            
            file.delete();
        }
    }

}
