package CoucheAcces_Fichier;

import GestionEspaceDisque_et_Buffer.PageID;

public class RecordId {

    private PageID pageId;
    private int slotIdx;

    public RecordId (PageID pageId, int slotIdx){
        this.pageId = pageId;
        this.slotIdx = slotIdx;
    }

}
