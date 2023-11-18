package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.PageID;

public class RecordId {

    private PageID pageId;
    private int slotIdx;

    public RecordId (PageID pageId, int slotIdx){
        this.pageId = pageId;
        this.slotIdx = slotIdx;
    }

    public RecordId(){
        this.pageId=new PageID();
        this.slotIdx=-1;
    }

    //check if a recordId is valid
    public boolean isValid(){
        if(this.pageId.isValid()){
            return true;
        }
        return false;
    }

}
