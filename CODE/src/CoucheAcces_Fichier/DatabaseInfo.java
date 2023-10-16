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
        }
        return instance;
    }

    public void Init() {
        Load();
    } // initialize an instance

    public void Finish() {
        Save();
    } // for cleaning

    public void AddTableInfo(TableInfo tabInfo) {
        informationTable.add(tabInfo);
        counterRelations++;
    }

    public TableInfo GetTableInfo(String nameRelation) { // gives us information (from TableInfo) about
        // a certain relation depending on a given name in argument

        for (TableInfo tabInfo : informationTable) { // a for each loop
            if ((tabInfo.getNameRelation()).equals(nameRelation)) {
                return tabInfo;
            }
        }
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
            String filePath = DBParams.DBPath + File.separator +databaseInfoFile ; 
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fileIn);
            DatabaseInfo dbInfo = (DatabaseInfo) ois.readObject();
            ois.close();
            fileIn.close();
            // Update the actual instance with new loaded information
            instance = dbInfo;
            System.out.println("Data has been loaded to unique instance of DBinfo (updated)");
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //check if the data of the database has been written in DBInfo.sava file
    public String toString(){
        String informationDatabase="";
        for(TableInfo table: informationTable ){
            informationDatabase+=table.toString();
            informationDatabase+="\n";
        }
        return informationDatabase;
    }

}
