package GestionEspaceDisque_et_Buffer;

import java.nio.ByteBuffer;

public class PageID {

   private int fileIdx;
   private int pageIdx;

<<<<<<< HEAD
   public PageID(int FileIdx, int PageIdx) {
      this.FileIdx = FileIdx;
      this.PageIdx = PageIdx;
=======
   public PageID(int fileIdx, int pageIdx) {
      this.fileIdx = fileIdx;
      this.pageIdx = pageIdx;
>>>>>>> 9ccc4e6e2c0078c36202f4e4918512808cddefd5

   }
   public static PageID readFromBuffer(ByteBuffer byteBuffer, int pos){
      byteBuffer.position(pos);
      return new PageID(byteBuffer.getInt(), byteBuffer.getInt());
   }

<<<<<<< HEAD
   public static void writeFromBuffer(ByteBuffer byteBuffer, int pos,PageID pageId){

      byteBuffer.position(pos);

      byteBuffer.putInt(pageId.getFileIdx());
      byteBuffer.putInt(pageId.getPageIdx());

   }
   public PageID() {
      this.FileIdx = -1;
      this.PageIdx = -1;
=======
   public PageID() {
      this.fileIdx = -1;
      this.pageIdx = -1;
>>>>>>> 9ccc4e6e2c0078c36202f4e4918512808cddefd5
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
      if(FileIdx == -1 && PageIdx == -1)
         return true;
      return false;
   }

   public boolean compareTo(PageID pageId){

      if(this.PageIdx == pageId.getPageIdx() && this.FileIdx == pageId.getPageIdx())
         return true;
      return false;
   }


}
