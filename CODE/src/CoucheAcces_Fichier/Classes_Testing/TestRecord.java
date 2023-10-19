package CoucheAcces_Fichier.Classes_Testing;

//We had a problem when we imported "CoucheAcces_Fichier.*;", so we had to do for each separatelly
import CoucheAcces_Fichier.Record;
import CoucheAcces_Fichier.ColInfo;
import CoucheAcces_Fichier.ColumnType;
import CoucheAcces_Fichier.TableInfo;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class TestRecord {
    public static void main(String[] args) {
        ArrayList<ColInfo> tableCols = new ArrayList<>();
        tableCols.add(new ColInfo("Column1", ColumnType.INT, 0)); // Spécifiez la longueur de chaîne 0 pour INT
        tableCols.add(new ColInfo("Column2", ColumnType.FLOAT, 0)); // Spécifiez la longueur de chaîne 0 pour FLOAT
        tableCols.add(new ColInfo("Column3", ColumnType.STRING, 19)); // Spécifiez une longueur de chaîne de 10 pour STRING
        tableCols.add(new ColInfo("Column4", ColumnType.VARSTRING, 50)); // Spécifiez une longueur de chaîne de 20 pour VARSTRING

        TableInfo tableInfo = new TableInfo("Table1", 4, tableCols);

        Record record = new Record(tableInfo);

        ArrayList<String> recValues = new ArrayList<>();
        recValues.add("1247"); // INT
        recValues.add("469.85"); // FLOAT
        recValues.add("HelloTest"); // STRING
        recValues.add("Licence2Wirdo"); // VARSTRING
        record.setRecValues(recValues);

        ByteBuffer buffer = ByteBuffer.allocate(100); 
        int bytesWritten = record.writeToBuffer(buffer, 0);
        System.out.println("Bytes Written: " + bytesWritten);

        

        Record newRecord = new Record(tableInfo);
        int bytesRead = newRecord.readFromBuffer(buffer, 0);
        System.out.println("Bytes Read: " + bytesRead);

        ArrayList<String> readValues = newRecord.getRecValues();
        for (int i = 0; i < readValues.size(); i++) {
            System.out.println("Column " + (i + 1) + ": " + readValues.get(i));
        }
    }
}

