package IDE_PROJET_DJUROVIC_ALASHOUR_ARBANE_HAMMACI.CODE.src;
public class PageID {
    private int FileIdx;
    private int PageIdx;


    public PageID(int f, int p){
        FileIdx = f;
        PageIdx= p ;
    }

    public int getFileIdx(){
        return FileIdx;
    }

    public int getPageIdx(){
        return PageIdx;
    }

}
