package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.FileManager;
import CoucheAcces_Fichier.Record;
import CoucheAcces_Fichier.TableInfo;
import java.util.ArrayList;


public class SelectCommand {

    private String userCommand;
    private String relationName;
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

        this.relationName = commandSplit[3]; // the relation name is the 4th word in the command
        
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
        
        // parse the condition
        String[] parts = conditionStr.split("=|<|>|<=|>=|<>"); // split the condition based on the operator

        String [] parseColumnName = parts[0].split("\\.");
        
        String columnName = parseColumnName[1].trim(); // the first part is the column name
        System.out.println(columnName);
        String value = parts[1].trim(); // the second part is the value (it's technically the third part and the
                                        // operator is the second part)
        String operator = conditionStr.substring(relationName.length()+columnName.length()+1, conditionStr.length() - value.length()).trim();
        // the operator is the remaining part of the condition after removing the column
        // name and the value

        System.out.println(operator);

        return new SelectCondition(columnName, operator, value);
    }

    // Method to execute the SelectCommand
    public void Execute() {
        // get the table info
        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.relationName);

        // get all the records from the table
        
        ArrayList<Record> records = FileManager.getFileManager().getAllRecords(tableInfo);

        // filter the records based on the conditions
        ArrayList<Record> selectedRecords = new ArrayList<>();

        //filter records if there is a condition
        if(this.condition){
            
            for (Record record : records) {
                if(record.isDeleted()==false){      //if the record is deleted it will not be selected
                    if (satisfiesConditions(record) ) {
                        selectedRecords.add(record);
                    }
                }
            }
        }

        //select all the records
        else{  
            
            for (Record record : records) {
                
                if(record.isDeleted()==false){
                    selectedRecords.add(record);
                }
                
            }
        }

        // print the selected records
        printSelectedRecords(selectedRecords);
    }

    // Method to verify if a record satisfies all the conditions
    private boolean satisfiesConditions(Record record) {
        for (SelectCondition condition : conditions) {
            if (!condition.isSatisfiedBy(record)) {
                return false; // if one condition is not satisfied, the record is not selected
            }
        }

        return true; // all conditions are satisfied
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

}
