import java.util.ArrayList;

public class DatabaseInfo {
    //this class should have only and only a unique instance that will be initiallized in the later TPs

    private ArrayList<TableInfo> informationTable;
    private int counterRelations;

    // constructeur


    //methods
    public void Init() {} // initialize an instance
    public void Finish() {} // for cleaning

    public void AddTableInfo(TableInfo tabInfo) {
        informationTable.add(tabInfo);
        counterRelations++;
    }

    public TableInfo GetTableInfo(String nameRelation) { // gives us information (from TableInfo) about 
        //a certain relation depending on a given name in argument

        for (TableInfo tabInfo : informationTable) { // a for each loop 
            if ((tabInfo.getNameRelation()).equals(nameRelation)) {
                return tabInfo;
            }
        }
        return null; // retuen null if none of them matches the given relation's name in the argument
    }
    

    public int GetCounterRelations() {
        return counterRelations;
    } 


}
