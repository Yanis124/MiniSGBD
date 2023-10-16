package CoucheAcces_Fichier.Classes_Testing;

import GestionEspaceDisque_et_Buffer.*;
import CoucheAcces_Fichier.*;
import java.util.ArrayList;

public class TestDatabaseInfo {
    public static void main(String[] args) {

        DBParams.DBPath = "../../DB";

        DatabaseInfo databaseInfo = DatabaseInfo.getInstance(); // unique instance of DatabaseInfo

       // creating instances of the different classes in "CoucheAcces_Ficher" to test them
        ArrayList<ColInfo> tableCols = new ArrayList<>();
        tableCols.add(new ColInfo("Column1", ColumnType.INT, 0)); // Calling the constructor to instantiate the class with the specified arguments
        tableCols.add(new ColInfo("Column2", ColumnType.FLOAT, 0)); 

        // Same thing, we are calling the constructor to instantiate the class
        TableInfo tableInfo1 = new TableInfo("Table1", 2, tableCols);
        TableInfo tableInfo2 = new TableInfo("Table2", 2, tableCols);

        databaseInfo.AddTableInfo(tableInfo1);
        databaseInfo.AddTableInfo(tableInfo2);

        // Sauvegarder les informations dans un fichier
        databaseInfo.Finish();

        // Charger les informations depuis le fichier
         databaseInfo.Init();

        //check if we get the right information
         System.out.println(databaseInfo.toString());
    }
}
