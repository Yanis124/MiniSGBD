package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.ColInfo;
import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.Record;
import CoucheAcces_Fichier.TableInfo;
import java.util.ArrayList;
import java.util.List;




public class SelectCondition {
    private String operator; // =, <, >, <=, >=, !=
    private String value; // the value to compare with
    private String firstColumnName;
    private String secondColumnName;
    private String firstRelationName;
    private String secondRelationName;
    private Boolean isJointCondition=false;

    public SelectCondition(String columnName, String operator, String value,String relationName) {
        this.firstColumnName = columnName;
        this.firstRelationName=relationName;
        this.operator = operator;
        this.value = value;
        this.secondColumnName=null;
        this.secondRelationName=null;
    }

    //if the condition is a joint condition
    public SelectCondition(String firstRelationName,String firstColumnName,String secondRelationName,String secondColumnName,String operator){
        this.firstColumnName=firstColumnName;
        this.firstRelationName=firstRelationName;
        this.secondColumnName=secondColumnName;
        this.secondRelationName=secondRelationName;
        this.operator=operator;
        this.value=null;
        this.isJointCondition=true;
    }

    // Constructor for the case where there is no condition
    public SelectCondition() {
        this.firstColumnName = null;
        this.secondColumnName=null;
        this.operator = null;
        this.value = null;
        this.secondRelationName=null;
        this.firstRelationName=null;
    }

    // Getters for the attributes
    public String getFirstColumnName() {
        return this.firstColumnName;
    }

    public String getSecondColumnName(){
        return this.secondColumnName;
    }
    
    public String getOperator() {
        return this.operator;
    }

    public String getValue() {
        return this.value;
    }

    public String getFirstRelationName(){
        return this.firstRelationName;
    }

    public String getSecondRelationName(){
        return this.secondRelationName;
    }

    public Boolean getTypeCondition(){
        return this.isJointCondition;
    }

    // Method to verify if a record satisfies this condition
    public boolean isSatisfiedBy(Record record) {
        // Get the value of the column in the record
        TableInfo tableInfo =record.getTableInfo();

        int columnIndex = getColumnIndex(firstColumnName, tableInfo);

        String columnValue = record.getRecValues().get(columnIndex);
        if (columnValue == null) {
            // Handle the case where the column value is null
            System.out.println(
                    "The column " + firstColumnName + " has a null value in the record because of a certain problem");
            return false;
        }

        // Compare the value with the condition
        switch (operator) {
            case "=":
                return columnValue.equals(value);
            case "<":
                return columnValue.compareTo(value) < 0;
            case ">":
                return columnValue.compareTo(value) > 0;
            case "<=":
                return columnValue.compareTo(value) <= 0;
            case ">=":
                return columnValue.compareTo(value) >= 0;
            case "<>":
                return !columnValue.equals(value);
            default:
                return false;
        }
    }

    public static ArrayList<ArrayList<Record>> joinRelations(SelectCondition selectCondition,ArrayList<Record> selectedRecordsFirstRelation, ArrayList<Record> selectedRecordsSecondRelation){
        

        String columnNameFirstRelation=selectCondition.getFirstColumnName(); //get the column name of the condition joint
        String columnNameSecondRelarion=selectCondition.getSecondColumnName(); //get the column name of the condition joint

        String operator=selectCondition.getOperator();  //get the oparator of the condition joint
        String firstRelationName=selectCondition.getFirstRelationName(); //get the name of the first relation
        String secondRelationName=selectCondition.getSecondRelationName(); //get the name of the second relation

        TableInfo firstTableInfo= DatabaseInfo.getInstance().GetTableInfo(firstRelationName);
        TableInfo secondTableInfo=DatabaseInfo.getInstance().GetTableInfo(secondRelationName); //get the table info of the second relation

        int columnIndexFirstRelation=selectCondition.getColumnIndex(columnNameFirstRelation,firstTableInfo); //get the index of the column in the first relation
        int columnIndexSecondRelation=selectCondition.getColumnIndex(columnNameSecondRelarion,secondTableInfo); //get the index of the column in the second relation

       
        return joinRecords(selectedRecordsFirstRelation,selectedRecordsSecondRelation,columnIndexFirstRelation,columnIndexSecondRelation,operator);
    }

    public static ArrayList<ArrayList<Record>> joinRecords(ArrayList<Record> selectedRecordsFirstRelation, ArrayList<Record> selectedRecordsSecondRelation, int columnIndexFirstRelation, int columnIndexSecondRelation, String operator) {
       
        ArrayList<ArrayList<Record>> joinedRecords=new ArrayList<ArrayList<Record>>();

        for(int i=0;i<selectedRecordsFirstRelation.size();i++){
            Record recordFirstRelation=selectedRecordsFirstRelation.get(i); //select a record from the first relation
           

            
            String columnValueFirstRelation=recordFirstRelation.getRecValues().get(columnIndexFirstRelation); //get the value of the column in the first relation

            for(int j=0;j<selectedRecordsSecondRelation.size();j++){
                 ArrayList<Record> newRecord=new ArrayList<Record>(); //it a that containe [record1,record2]
                newRecord.add(recordFirstRelation);

                Record recordSecondRelation=selectedRecordsSecondRelation.get(j); //Select a record from the second relation
                String columnValueSecondRelation=recordSecondRelation.getRecValues().get(columnIndexSecondRelation); //get the value of the column in the second relation

                switch(operator){

                    case "=":
                        if(columnValueFirstRelation.equals(columnValueSecondRelation)){
                            System.out.println("first value :"+columnValueFirstRelation);
                            System.out.println("second value :"+columnValueSecondRelation);
                            
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                            
                            for(Record record : newRecord) {
                                record.displayRecord();
                            }
                            
                        }
                        break;
                    case "<":
                        if(columnValueFirstRelation.compareTo(columnValueSecondRelation)<0){
                            System.out.println("first value :"+columnValueFirstRelation);
                            System.out.println("second value :"+columnValueSecondRelation);
                            

                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                             for(Record record : newRecord) {
                                record.displayRecord();
                            }
                            
                        }
                        break;
                    case ">":
                        if(columnValueFirstRelation.compareTo(columnValueSecondRelation)>0){
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                             
                        }
                        break;
                    case "<=":
                        if(columnValueFirstRelation.compareTo(columnValueSecondRelation)<=0){
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                            for(Record record : newRecord) {
                                record.displayRecord();
                            }
                             
                        }
                        break;
                    case ">=":
                        if(columnValueFirstRelation.compareTo(columnValueSecondRelation)>=0){
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                            for(Record record : newRecord) {
                                record.displayRecord();
                            }
                             
                        }
                        break;
                    case "<>":
                        if(!columnValueFirstRelation.equals(columnValueSecondRelation)){
                            newRecord.add(recordSecondRelation);
                            joinedRecords.add(newRecord);
                             
                        }
                        break;
                    default:
                        break;
                }

                
            }
        }
        System.out.println(" terminate : "+operator);

        return joinedRecords;
    }

    //get the index of a column in a relation
    private int getColumnIndex(String columnName, TableInfo tableInfo) {

        int columnIndex = -1;
        List<ColInfo> tableCols = tableInfo.getTableCols();

        for (int i = 0; i < tableCols.size(); i++) {
            if (tableCols.get(i).getNameCol().equals(columnName)) {
                columnIndex = i;
                break;
            }
        }
        return columnIndex;
    }

    @Override
    public String toString() {
        return firstRelationName + " " +firstColumnName+" "+secondRelationName+" "+secondColumnName+" "+ operator + " " + value;
    }

}

