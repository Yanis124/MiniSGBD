package GestionEspaceDisque_et_Buffer;
public class PageID {

   private int fileIdx;
   private int pageIdx;

   public PageID(int fileIdx, int pageIdx) {
      this.fileIdx = fileIdx;
      this.pageIdx = pageIdx;

   }

   public PageID() {
      this.fileIdx = -1;
      this.pageIdx = -1;
   }

   public void setFileIdx(int FileIdx) {
      this.fileIdx = FileIdx;
   }

   public void setPageIdx(int PageIdx) {
      this.pageIdx = PageIdx;
   }

   public int getFileIdx() {
      return this.fileIdx;
   }

   public int getPageIdx() {
      return this.pageIdx;
   }

   public String toString(){
      return "("+fileIdx+","+pageIdx+")"+",";
   }

}
