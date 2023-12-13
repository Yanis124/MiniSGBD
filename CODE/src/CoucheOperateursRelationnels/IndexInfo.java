package CoucheOperateursRelationnels;

public class IndexInfo {
    private String relationName;
    private int columnIndex;
    private String order;
    //private ArrayList<IndexEntry> entries;

    public IndexInfo(String relationName, int columnIndex, String order) {
        this.relationName = relationName;
        this.columnIndex = columnIndex;
        this.order = order;
        //this.entries = new ArrayList<>();
    }

    // Méthode pour ajouter une entrée à l'index
    

}
