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
    private static DatabaseInfo instance; 
    private String databaseInfoFile="DBInfo.save";
    private ArrayList<TableInfo> informationTable;
    private int counterRelations;

    // constructeur
    private DatabaseInfo() {
        informationTable = new ArrayList<>();
        counterRelations = 0;
    }

    // methods

    public static DatabaseInfo getInstance() {
        if (instance == null) {
            instance = new DatabaseInfo();
            instance.Load();
            System.out.println("New instance of DBInfo created");
        }
        System.out.println("Instance of DBInfo returned");
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
        System.out.println("Valeur de informationTable :");
        System.out.println(informationTable);
        for (TableInfo tabInfo : informationTable) { // a for each loop
            System.out.println("Nom de la relation :");
            System.out.println(tabInfo.getNameRelation());
            if ((tabInfo.getNameRelation()).equals(nameRelation)) {
                return tabInfo;
            }
        }
        System.out.println("On return null car on a pas trouvé de relation avec ce nom ");
        return null; // return null if none of them matches the given relation's name in the argument
    }

    public int GetCounterRelations() {
        return counterRelations;
    }

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
    
            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(filePath);
                ObjectInputStream ois = new ObjectInputStream(fileIn);
                DatabaseInfo dbInfo = (DatabaseInfo) ois.readObject();
                ois.close();
                fileIn.close();
                
                // Mettez à jour l'instance actuelle avec les nouvelles informations chargées
                instance = dbInfo;
                System.out.println("Data has been loaded to unique instance of DBinfo (updated)");
            } else {
                System.out.println("DBInfo.save file does not exist or is empty.");
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
    }

}
