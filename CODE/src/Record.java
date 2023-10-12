import java.util.ArrayList;

public class Record { // record corresponds to line in a table (a tuple in a relation)
    private TableInfo tabInfo;
    private ArrayList<String> recValues;

    //constructeur
    public Record (TableInfo tableInfo) {
        this.tabInfo = tableInfo;
        this.recValues = new ArrayList<String>();
    }

}
