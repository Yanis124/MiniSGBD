package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.PageID;

import java.io.Serializable;
import java.util.ArrayList;

public class TableInfo implements Serializable{ // a set of usefull information about a relation
    private String nameRelation;
    private int numberCols;
    private ArrayList<ColInfo> tableCols; // name and type of columns are regrouped in a separated class

    private PageID headerPageId;

    //constructeur
    public TableInfo(String nameRelation, int numberCols, ArrayList<ColInfo> tableCols, PageID headerPageId) {
        this.nameRelation = nameRelation;
        this.numberCols = numberCols;
        this.tableCols = tableCols;
        this.headerPageId = headerPageId;
    }

    public String getNameRelation() {
        return nameRelation;
    }

    public int getNumberCols() {
        return numberCols;
    }

    public ArrayList<ColInfo> getTableCols() {
        return tableCols;
    }

    public PageID getHeaderPageId(){
        return headerPageId;
    }


    public String toString(){
        String TableInfo="|"+nameRelation+"|";
        for(ColInfo col:tableCols){
            TableInfo+=col.toString();
        }
        TableInfo+="| number of colomn : "+numberCols+"|";
        return TableInfo;
    }




}
