package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import CoucheAcces_Fichier.Record;
import CoucheAcces_Fichier.TableInfo;
import java.util.ArrayList;



public class SelectCommand {

    private String userCommand;
    private ArrayList<String> listRelationNames;
    private ArrayList<SelectCondition> conditions;
    private boolean condition=false;
    
    
    

    // Constructor for the SelectCommand class using the parsing of the userCommand
    public SelectCommand(String userCommand) {
        this.userCommand = userCommand;

        // Extraction of the relation name
        String[] commandSplit = userCommand.split(" ");

        for(String elements:commandSplit){
            if(elements.equals("WHERE")){
                this.condition=true;
                break;

            }
        }

        String relationNames = commandSplit[3]; // get the raltions in the command
        this.listRelationNames = parseRelationNames(relationNames); // get the relations 

        //verifie if the select have a condition 

        if(this.condition==true){
            
            String conditionsStr = ""; // parse the conditions
            int whereIndex = userCommand.indexOf("WHERE");
            
            if (whereIndex != -1) {
                conditionsStr = userCommand.substring(whereIndex + 6).trim(); // 6 because "WHERE" has 5 characters and we
                                                                            // want to skip the space after it
            }

            this.conditions = parseConditions(conditionsStr);
            // conditions now contains the parsed conditions (only the String part after the
            // WHERE)
        }
    }

    // this method to analyze the conditions and return an ArrayList of
    // SelectCondition
    // used inside the constructor

    //get the relationNames in the commande
    private ArrayList<String> parseRelationNames(String relationNames){
        ArrayList<String> parsedRelationNames = new ArrayList<>();

        if(!relationNames.isEmpty()){
            String[] relationNamesSplit = relationNames.split(","); // split the relations based on the comma
            for(String relationName:relationNamesSplit){
                parsedRelationNames.add(relationName.trim()); // add the relation name to the ArrayList
            }
        }
        else{
            parsedRelationNames.add(relationNames.trim()); // add the relation name to the ArrayList
        }
        
        return parsedRelationNames;
    }



    private ArrayList<SelectCondition> parseConditions(String conditionsStr) {
        ArrayList<SelectCondition> parsedConditions = new ArrayList<>();

        // check if there are conditions
        if (!conditionsStr.isEmpty()) {
            // parse the conditions and add them to the ArrayList
            String[] conditionsSplit = conditionsStr.split(" AND "); // spaces included in the AND
            for (String conditionStr : conditionsSplit) {
                // parse the condition
                parsedConditions.add(parseEachCondition(conditionStr.trim()));
            }
        } else {
            // No conditions specified in the command
            parsedConditions.add(new SelectCondition()); // add an empty condition
        }

        return parsedConditions;
    }

    // this method to analyze each condition and return a SelectCondition
    // used inside the parseConditions method
    private SelectCondition parseEachCondition(String conditionStr) {

        String [] parseColumnName = conditionStr.split("\\.");
    
        if(parseColumnName.length>2){
            return parseConditionJoint(conditionStr);
        }

        else{
            return parseConditionValue(conditionStr);
        }        
    }


    private SelectCondition parseConditionValue(String conditionStr) {
        // parse the condition

         // parse the condition
        String[] parts = conditionStr.split("=|<|>|<=|>=|<>"); // split the condition based on the operator

        String [] parseColumnName = parts[0].split("\\.");
        
        String relationName=parseColumnName[0].trim(); // the first part is the relation name
        String columnName = parseColumnName[1].trim(); // the second part is the column name
        
        String value = parts[1].trim(); // the second part is the value (it's technically the third part and the
                                        // operator is the second part)
        String operator = conditionStr.substring(relationName.length()+columnName.length()+1, conditionStr.length() - value.length()).trim();
        // the operator is the remaining part of the condition after removing the column
        // name and the value

        return new SelectCondition(columnName, operator, value, relationName);
    }

    //parse a condition of type R.col=S.col
    private SelectCondition parseConditionJoint(String joinConditionStr){

        String [] parts = joinConditionStr.split("=|<|>|<=|>=|<>"); // split the condition based on the operator
        
        String [] firstPartCondition=parts[0].split("\\.");
        String [] secondPartCondition=parts[1].split("\\.");

        String firstRelationName=firstPartCondition[0].trim();
        String firstColumnName=firstPartCondition[1].trim();
        String secondRelationName=secondPartCondition[0].trim();
        String secondColumnName=secondPartCondition[1].trim();

        String operator = joinConditionStr.substring(firstRelationName.length() + firstColumnName.length() + 1,joinConditionStr.length() - (secondRelationName.length() + secondColumnName.length() + 1)).trim();

        return new SelectCondition(firstRelationName,firstColumnName,secondRelationName,secondColumnName,operator);

    }

    // Method to execute the SelectCommand
    public void Execute() {
        // get the table info
        TableInfo firsttableInfo = DatabaseInfo.getInstance().GetTableInfo(this.listRelationNames.get(0));
        ArrayList<Record> recordsFirstRelation = FileManager.getFileManager().getAllRecords(firsttableInfo); //get record of the first relation
        TableInfo secondtableInfo = null;
        ArrayList<Record> recordsSecondRelation=null;

        if(listRelationNames.size()>1){  //if there is more than one relation
            secondtableInfo = DatabaseInfo.getInstance().GetTableInfo(this.listRelationNames.get(1));
            recordsSecondRelation= FileManager.getFileManager().getAllRecords(secondtableInfo); //get records of the second relation
        }

        // filter the records based on the conditions
        ArrayList<Record> selectedRecordsFirstRelation = new ArrayList<>();
        ArrayList<Record> selectedRecordsSecondRelation = new ArrayList<>();
        ArrayList<ArrayList<Record>> selectedRecordsJoin =null;

        //filter records if there is a condition
        if(this.condition){
            getSelectedRecordsCondition(recordsFirstRelation, recordsSecondRelation, selectedRecordsFirstRelation, selectedRecordsSecondRelation);
            if(secondtableInfo!=null){
                selectedRecordsJoin=selectedRecordsJoin(selectedRecordsFirstRelation, selectedRecordsSecondRelation);
                printSelectedRecordsJoin(selectedRecordsJoin);
            }
            else{
                printSelectedRecords(selectedRecordsFirstRelation);
            }
        }

        //no condition
        else{
            getSelectedRecords(recordsFirstRelation, recordsSecondRelation, selectedRecordsFirstRelation, selectedRecordsSecondRelation);
            System.out.println("size: "+selectedRecordsFirstRelation.size());
            System.out.println("size : "+selectedRecordsSecondRelation.size());
            if(secondtableInfo!=null){
                selectedRecordsJoin=combinateRecords(selectedRecordsFirstRelation, selectedRecordsSecondRelation);
                printSelectedRecordsJoin(selectedRecordsJoin);
            }
            else{
                printSelectedRecords(selectedRecordsFirstRelation);
            }
        }
    }

    //selecte all records with conditions
    private void getSelectedRecordsCondition(ArrayList<Record> recordsFirstRelation,ArrayList<Record> recordsSecondRelation,ArrayList<Record> selectedRecordsFirstRelation,ArrayList<Record> selectedRecordsSecondRelation) {

        for (Record record : recordsFirstRelation) {
            if(record.isDeleted()==false){      //if the record is deleted it will not be selected
                if (satisfiesConditions(record) ) {
                    selectedRecordsFirstRelation.add(record);
                }
            }
        }

        if(recordsSecondRelation!=null){
            for (Record record : recordsSecondRelation) {
                if(record.isDeleted()==false){      //if the record is deleted it will not be selected
                    if (satisfiesConditions(record) ) {
                        selectedRecordsSecondRelation.add(record);
                    }
                }
            }
        }
    }

    //select all records without conditions
    private void getSelectedRecords(ArrayList<Record> recordsFirstRelation,ArrayList<Record> recordsSecondRelation,ArrayList<Record> selectedRecordsFirstRelation,ArrayList<Record> selectedRecordsSecondRelation){            
        for (Record record : recordsFirstRelation) {
            
            if(record.isDeleted()==false){
                selectedRecordsFirstRelation.add(record);
            }
            
        }

        for (Record record : recordsSecondRelation) {
            
            if(record.isDeleted()==false){
                selectedRecordsSecondRelation.add(record);
            }
        }
    }

    // verifie if a recod satisfies all condition except if it a join condition
    private boolean satisfiesConditions(Record record) {
        for (SelectCondition condition : conditions) {
            if(condition.getFirstRelationName().equals(record.getTableInfo().getNameRelation()) && !condition.getTypeCondition()){ //if the condition is applied to the record
                if (!condition.isSatisfiedBy(record)) {
                    return false; // if one condition is not satisfied, the record is not selected
                }
            }
        }

        return true; // all conditions are satisfied
    }

    // verifie if a recod satisfies all condition if it a join condition
    private ArrayList<ArrayList<Record>> selectedRecordsJoin(ArrayList<Record> selectedRecordsFirstRelation,ArrayList<Record> selectedRecordsSecondRelation){
        
        ArrayList<ArrayList<Record>> selectedRecordsJoin=new ArrayList<>();
        boolean joinCondition=false;

        for(SelectCondition selectedCondition : this.conditions){
            if(selectedCondition.getTypeCondition()){ //if the condition of type join
                joinCondition=true;
                selectedRecordsJoin= SelectCondition.joinRelations(selectedCondition,selectedRecordsFirstRelation,selectedRecordsSecondRelation);
            }
        }
        if(joinCondition==false){
            selectedRecordsJoin=combinateRecords(selectedRecordsFirstRelation, selectedRecordsSecondRelation);
        }

        return selectedRecordsJoin;
    }

    private ArrayList<ArrayList<Record>> combinateRecords(ArrayList<Record> selectedRecordsFirstRelation,ArrayList<Record> selectedRecordsSecondRelation){
        ArrayList<ArrayList<Record>> combinateRecords=new ArrayList<>();

        for(int i=0;i<selectedRecordsFirstRelation.size();i++){
            Record recordFirstRelation=selectedRecordsFirstRelation.get(i); //select a record from the first relation
            

            for(int j=0;j<selectedRecordsSecondRelation.size();j++){
                Record recordSecondRelation=selectedRecordsSecondRelation.get(j); //Select a record from the second relation
                ArrayList<Record> newRecord=new ArrayList<Record>(); //it a that containe [record1,record2]
                newRecord.add(recordFirstRelation);
                newRecord.add(recordSecondRelation);
                combinateRecords.add(newRecord);
            }
            
            
        }

        return combinateRecords;
    }

    //Method to print the selected records
    private void printSelectedRecords(ArrayList<Record> records) {
        System.out.println("        <<<<Selected records:>>>>       ");
        for (Record record : records) {
            // print the record values separated by " ; " and end with a dot
            System.out.println(String.join(" ; ", record.getRecValues()));
            
        }

        //print the total number of records
        System.out.println("Total records=" + records.size());
    }

    private void printSelectedRecordsJoin(ArrayList<ArrayList<Record>> records) {
        System.out.println("        <<<<Selected records:>>>>       ");

        System.out.println("size : :: "+records.size());
        
        for(ArrayList<Record> record : records){
            Record record1=record.get(0);
            Record record2=record.get(1);
            System.out.println(String.join(" ; ", record1.getRecValues())+" ; "+String.join(" ; ", record2.getRecValues()));
        }

    }

}
