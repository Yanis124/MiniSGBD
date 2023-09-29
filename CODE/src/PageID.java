public class PageID {
    private int FileIdx;
    private int PageIdx;

    PageID(int FileIdx,int PageIdx){
        this.FileIdx=FileIdx;
        this.PageIdx=PageIdx;
    }

    PageID(){
        this.FileIdx=-1;
        this.PageIdx=-1;
    }

    public void setFileIdx(int FileIdx){
       this.FileIdx=FileIdx ;
    }

    public void setPageIdx(int PageIdx){
        this.PageIdx=PageIdx ;
     }

     public int getFileIdx(){
        return this.FileIdx;
     }

     public int getPageIdx(){
        return this.PageIdx;
     }

    }

