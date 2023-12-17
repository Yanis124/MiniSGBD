package CoucheAcces_Fichier;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {
    private TableInfo tabInfo;
    private ArrayList<String> recValues;
    private boolean deletedRecord=false;

    // The constructor
    public Record(TableInfo tableInfo) {
        this.tabInfo = tableInfo;
        this.recValues = new ArrayList<>();
    }

    // method to update the recValues
    public void setRecValues(ArrayList<String> recVals) {
        recValues = recVals;
    }

    //set the deletedRecord to true
    public void setAsDeleted(){
        this.deletedRecord=true;
    }

    //mark if a recrod is deleted
    public boolean isDeleted(){
        return this.deletedRecord;
    }

    // method to retrieve the recValues
    public ArrayList<String> getRecValues() {
        return recValues;
    }

    public int writeToBuffer(ByteBuffer buff, int pos) {
        try {
            buff.position(pos);
            int totalSize = 0;
            for (int i = 0; i < tabInfo.getNumberCols(); i++) {
                String value = recValues.get(i);

                switch (tabInfo.getTableCols().get(i).getTypeCol()) {
                    case INT:
                        int intValue = Integer.parseInt(value);
                        buff.putInt(intValue);
                        totalSize += 4; // 4 bytes for an int
                        break;
                    case FLOAT:
                        // Store the FLOAT value as binary (no conversion, we've made it this way to avoid on certain problems)
                        float floatValue = Float.parseFloat(value);
                        buff.putFloat(floatValue);
                        totalSize += 4;  // 4 bytes for a float
                        break;
                    case STRING:
                        // Write a fixed-length STRING
                        String stringValue = value;
                        int stringLength = tabInfo.getTableCols().get(i).getLengthString();
                        byte[] stringBytes = new byte[stringLength];
                        byte[] valueBytes = stringValue.getBytes();

                        // Copy the needed part of the value string into the STRING column
                        System.arraycopy(valueBytes, 0, stringBytes, 0, Math.min(valueBytes.length, stringLength));
                        
                        buff.put(stringBytes);
                        totalSize += stringLength;
                        break;
                    case VARSTRING:
                        // Store the VARSTRING value as binary with its length
                        int varStringLength = value.length();
                        buff.putInt(varStringLength);
                        byte[] varStringBytes = value.getBytes();
                        buff.put(varStringBytes);
                        totalSize += 4 + varStringLength;
                        break;
                    default:
                        System.out.println("Column type not supported");
                        break;
                }
            }
            return totalSize;
        } catch (Exception e) {
            System.err.println("Error in writeToBuffer: " + e.getMessage());
            return -1; // Or another error value as needed
        }
    }

    public int sizeRecord(){
         int totalSize = 0;
            for (int i = 0; i < tabInfo.getNumberCols(); i++) {
                
                String value = recValues.get(i);

                switch (tabInfo.getTableCols().get(i).getTypeCol()) {
                    case INT:
                        
                        //buff.putInt(intValue);
                        totalSize += 4;
                        break;
                    case FLOAT:
                        // Store the FLOAT value as binary (no conversion, we've made it this way to avoid on certain problems)
                        
                        //buff.putFloat(floatValue);
                        totalSize += 4;
                        break;
                    case STRING:
                        // Write a fixed-length STRING
                        
                        int stringLength = tabInfo.getTableCols().get(i).getLengthString();
                        // byte[] stringBytes = new byte[stringLength];
                        // byte[] valueBytes = stringValue.getBytes();

                        // // Copy the needed part of the value string into the STRING column
                        // System.arraycopy(valueBytes, 0, stringBytes, 0, Math.min(valueBytes.length, stringLength));
                        
                        // buff.put(stringBytes);
                        totalSize += stringLength;
                        break;
                    case VARSTRING:
                        // Store the VARSTRING value as binary with its length
                        int varStringLength = value.length();
                        //buff.putInt(varStringLength);
                        //byte[] varStringBytes = value.getBytes();
                        //buff.put(varStringBytes);
                        totalSize += 4 + varStringLength;
                        break;
                    default:
                        System.out.println("Column type not supported");
                        break;
                }
            }
            return totalSize;
    }

    public int readFromBuffer(ByteBuffer buff, int pos) {
        try {
            recValues.clear();
            buff.position(pos);
            int totalSize = 0;

            for (int i = 0; i < tabInfo.getNumberCols(); i++) {
                switch (tabInfo.getTableCols().get(i).getTypeCol()) {
                    case INT:
                        int intValue = buff.getInt(); 
                        recValues.add(Integer.toString(intValue));
                        totalSize += 4;
                        break;
                    case FLOAT:
                        float floatValue = buff.getFloat();
                        recValues.add(Float.toString(floatValue));
                        totalSize += 4;
                        break;
                    case STRING:
                        int stringLength = tabInfo.getTableCols().get(i).getLengthString();
                        byte[] stringBytes = new byte[stringLength];
                        buff.get(stringBytes);
                        String stringValue = new String(stringBytes).trim(); // remove spaces or extra-padding
                        recValues.add(stringValue);
                        totalSize += stringLength;
                        break;
                    case VARSTRING:
                        int varStringLength = buff.getInt();
                        byte[] varStringBytes = new byte[varStringLength];
                        buff.get(varStringBytes);
                        String varStringValue = new String(varStringBytes);
                        recValues.add(varStringValue);
                        totalSize += 4 + varStringLength;
                        break;
                    default:
                        System.out.println("Column type not supported");
                        break;
                }
            }
            return totalSize;
        } catch (Exception e) {
            System.err.println("Error in readFromBuffer: " + e.getMessage());
            return -1; // Or another error value as needed
        }
    }

    //get the tabInfo of a record
    public TableInfo getTableInfo(){
        return this.tabInfo;
    }

    //display the content of a record
    public void displayRecord(){
        for(int i=0;i<recValues.size();i++){
            System.out.print(recValues.get(i));
        }
    }

    //compare two records
    public boolean compare(Record record){

        System.out.println("list ");
        if(record.getRecValues().isEmpty()|| this.recValues.isEmpty()){
            System.out.println("list empty");
            return false;
        }
        
        for(int i=0;i<recValues.size();i++){
            if(!recValues.get(i).equals(record.getRecValues().get(i))){
                return false;
            }
        }
        return true;
    }

}
