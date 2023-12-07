package CoucheOperateursRelationnels;

import java.util.ArrayList;

import CoucheAcces_Fichier.ColInfo;
import CoucheAcces_Fichier.ColumnType;
import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.TableInfo;

public class ImportCommande {
    private String tableName;
    private ArrayList<ColInfo> colsInfo=new ArrayList<>();
    private String filePath;
    private String userCommand; 

    //constructor
    public ImportCommande(String userCommand){
        
        this.userCommand=userCommand; 

        String[] tokens = this.userCommand.split(" ");
        this.tableName = tokens[2]; //get the name of the relation

        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.tableName); //get the relation 

        ArrayList<ColInfo> colsInfos = tableInfo.getTableCols();
        System.out.println("size :   "+colsInfos.size());
        
        for(ColInfo colInfo : colsInfos){ //get the colInfo of the relation
            this.colsInfo.add(colInfo);
        }

        this.filePath = tokens[3]; //get the path of the file
    }

    public void printTableInfo() {
        System.out.println("Table Name: " + this.tableName);
        System.out.println("name of the file : "+this.filePath);
        System.out.println("Column Information:");

        for (ColInfo col : this.colsInfo) {
            System.out.println("  Column Name: " + col.getNameCol());
            System.out.println("  Column Type: " + col.getTypeCol());
            if (col.getTypeCol() == ColumnType.STRING || col.getTypeCol() == ColumnType.VARSTRING) {
                System.out.println("  Column Size: " + col.getLengthString());
            }
        }
    }

    //add records to the relation
    public void Execute(){

    }
}
