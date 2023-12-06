package CoucheOperateursRelationnels;

import java.io.File;
import java.util.ArrayList;
import CoucheAcces_Fichier.ColInfo;
import CoucheAcces_Fichier.ColumnType;
import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import CoucheAcces_Fichier.TableInfo;
import GestionEspaceDisque_et_Buffer.DiskManager;
import GestionEspaceDisque_et_Buffer.PageID;

public class CreateTableCommand {
    private String tableName;
    private int nbCol;
    private ArrayList<ColInfo> colsInfo;
    private String userCommand; 

    // exemple of userCommand :
    // (col1:INT,col2:FLOAT,col3:STRING(10))
    // Constructeur de ColInfo(String nameCol, ColumnType typeCol, int lengthString)
    // pas de colSize

    public CreateTableCommand(String userCommand) {

        this.userCommand=userCommand;

        String[] tokens = this.userCommand.split(" ");
        this.tableName = tokens[2];
        String colsStr = this.userCommand.substring(this.userCommand.indexOf('(') + 1, this.userCommand.indexOf(')'));
        String[] cols = colsStr.split(",");
        this.nbCol = cols.length;
        this.colsInfo = new ArrayList<>();

        for (int i = 0; i < nbCol; i++) {
            String[] col = cols[i].split(":");
            String colName = col[0].trim(); // Supprimez les espaces autour du nom de la colonne
            ColumnType colType = null; // Initialisez à null

            if (col.length >= 2) {
                // Vérifiez le type de colonne
                String colTypeStr = col[1].trim();
                if (colTypeStr.equals("INT")) {
                    colType = ColumnType.INT;
                } else if (colTypeStr.equals("FLOAT")) {
                    colType = ColumnType.FLOAT;
                } else if (colTypeStr.startsWith("STRING")) {
                    colType = ColumnType.STRING;
                } else if (colTypeStr.startsWith("VARSTRING")) {
                    colType = ColumnType.VARSTRING;
                } else {
                    System.out.println("Invalid column type: " + colTypeStr);
                }
            }

            int lengthString = 0;
            if (colType != null && colType != ColumnType.INT && colType != ColumnType.FLOAT) {

                if (colType == ColumnType.STRING || colType == ColumnType.VARSTRING) { // par défaut 1 si longueur n'est pas spécifiée
                    lengthString = 1;
                }



                // Nettoyez la chaîne col[2] pour supprimer tout caractère non numérique
                String lengthStringStr = col[1].replaceAll("[^0-9]", "");
                try {
                    lengthString = Integer.parseInt(lengthStringStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid column size: " + col[1]);
                }

            }

            ColInfo colInfo = new ColInfo(colName, colType, lengthString);
            colsInfo.add(colInfo);

            // add the table to the databaseInfo
        }

       
    }
        
    public void printTableInfo() {
        System.out.println("Table Name: " + this.tableName);
        System.out.println("Number of Columns: " + this.nbCol);
        System.out.println("Column Information:");
        for (ColInfo col : this.colsInfo) {
            System.out.println("  Column Name: " + col.getNameCol());
            System.out.println("  Column Type: " + col.getTypeCol());
            if (col.getTypeCol() == ColumnType.STRING || col.getTypeCol() == ColumnType.VARSTRING) {
                System.out.println("  Column Size: " + col.getLengthString());
            }
        }
    }

    //add the table to the database
    public void Execute() {
        
        DatabaseInfo databaseInfo = DatabaseInfo.getInstance();
        FileManager fileManager = FileManager.getFileManager();
        PageID headerPageID =fileManager.createNewHeaderPage(); //crete a headerPage for the relation !!
        databaseInfo.AddTableInfo(new TableInfo(tableName, nbCol, colsInfo, headerPageID)); //add the table to the database
    }

}
