package CoucheAcces_Fichier;

import java.io.Serializable;

public class ColInfo implements Serializable{ // we've separated each column's info as advised in the TP
    private String nameCol;
    private ColumnType typeCol;
    private int lengthString;

    public ColInfo (String nameCol, ColumnType typeCol, int lengthString) {
        this.nameCol = nameCol;
        this.typeCol = typeCol;
        this.lengthString = lengthString;
    }


    public String getNameCol() {
        return nameCol;
    }

    public ColumnType getTypeCol() {
        return typeCol;
    }

    public int getLengthString() {
        return lengthString;
    }
}