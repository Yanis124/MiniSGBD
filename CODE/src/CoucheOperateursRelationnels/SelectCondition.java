package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.Record;
import java.util.List;
import CoucheAcces_Fichier.ColInfo;

public class SelectCondition {
    private String columnName; // the name of the column to compare with
    private String operator; // =, <, >, <=, >=, !=
    private String value; // the value to compare with

    public SelectCondition(String columnName, String operator, String value) {
        this.columnName = columnName;
        this.operator = operator;
        this.value = value;
    }

    // Constructor for the case where there is no condition
    public SelectCondition() {
        this.columnName = null;
        this.operator = null;
        this.value = null;
    }

    // Getters for the attributes
    public String getColumnName() {
        return columnName;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    // Method to verify if a record satisfies this condition
    public boolean isSatisfiedBy(Record record) {
        // Get the value of the column in the record
        int columnIndex = -1;
        List<ColInfo> tableCols = record.getTableInfo().getTableCols();
        for (int i = 0; i < tableCols.size(); i++) {
            if (tableCols.get(i).getNameCol().equals(columnName)) {
                columnIndex = i;
                break;
            }
        }

        if (columnIndex == -1) {
            // Handle the case where the column does not exist in the record
            System.out.println("The column " + columnName + " does not exist in the record");
            return false;
        }

        String columnValue = record.getRecValues().get(columnIndex);
        if (columnValue == null) {
            // Handle the case where the column value is null
            System.out.println(
                    "The column " + columnName + " has a null value in the record because of a certain problem");
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
            case "!=":
                return !columnValue.equals(value);
            default:
                return false;
        }
    }

}

