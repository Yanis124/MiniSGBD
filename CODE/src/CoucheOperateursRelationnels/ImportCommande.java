package CoucheOperateursRelationnels;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import CoucheAcces_Fichier.ColInfo;
import CoucheAcces_Fichier.ColumnType;
import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import CoucheAcces_Fichier.TableInfo;
import CoucheAcces_Fichier.Record;

public class ImportCommande {
    private String relationName;
    private ArrayList<ColInfo> colsInfo=new ArrayList<>();
    private String filePath;
    private String userCommand; 

    //constructor
    public ImportCommande(String userCommand){
        
        this.userCommand=userCommand; 

        String[] tokens = this.userCommand.split(" ");
        this.relationName = tokens[2]; //get the name of the relation

        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.relationName); //get the relation 

        ArrayList<ColInfo> colsInfos = tableInfo.getTableCols();
        
        for(ColInfo colInfo : colsInfos){ //get the colInfo of the relation
            this.colsInfo.add(colInfo);
        }

        this.filePath = tokens[3]; //get the path of the file
    }

    public void printTableInfo() {
        System.out.println("Table Name: " + this.relationName);
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

        ArrayList<Record> recordList=createListRecords(); //get the list of records

        // Insert the record into the table
        for(Record record : recordList){
            FileManager.getFileManager().InsertRecordIntoTable(record);
        }
    }

    public ArrayList<Record> createListRecords(){

        ArrayList<Record> recordList= new ArrayList<>();//list of records
        
        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.relationName); // Get the table info

        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {  //read the file
            String line;
            while ((line = br.readLine()) != null) {
                Record record = new Record(tableInfo); //create a record
                String[] valuesSplit = line.split(",");
                
                for(String value:valuesSplit){
                    record.getRecValues().add(value); //add the values to the record
                }
                recordList.add(record); //add the record to the list of records

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return recordList;
    }
}