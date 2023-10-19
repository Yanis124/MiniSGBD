package GestionEspaceDisque_et_Buffer;

import java.nio.ByteBuffer;

public class PageID {

   private int fileIdx;
   private int pageIdx;


   public PageID(int fileIdx, int pageIdx) {
      this.fileIdx = fileIdx;
      this.pageIdx = pageIdx;

   }
   public static PageID readFromBuffer(ByteBuffer byteBuffer, int pos){
      byteBuffer.position(pos);
      return new PageID(byteBuffer.getInt(), byteBuffer.getInt());
   }

   public static void writeFromBuffer(ByteBuffer byteBuffer, int pos,PageID pageId){

      byteBuffer.position(pos);

      byteBuffer.putInt(pageId.getFileIdx());
      byteBuffer.putInt(pageId.getPageIdx());

   }
   public PageID(){
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

   public boolean factice(){
      if(fileIdx == -1 && pageIdx == -1)
         return true;
      return false;
   }

   public boolean compareTo(PageID pageId){

      if(this.pageIdx == pageId.getPageIdx() && this.fileIdx == pageId.getPageIdx())
         return true;
      return false;
   }


}
