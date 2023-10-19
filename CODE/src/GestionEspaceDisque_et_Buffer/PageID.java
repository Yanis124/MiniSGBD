package GestionEspaceDisque_et_Buffer;

import java.nio.ByteBuffer;

public class PageID {

   private int FileIdx;
   private int PageIdx;

   public PageID(int FileIdx, int PageIdx) {
      this.FileIdx = FileIdx;
      this.PageIdx = PageIdx;

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
   public PageID() {
      this.FileIdx = -1;
      this.PageIdx = -1;
   }

   public void setFileIdx(int FileIdx) {
      this.FileIdx = FileIdx;
   }

   public void setPageIdx(int PageIdx) {
      this.PageIdx = PageIdx;
   }

   public int getFileIdx() {
      return this.FileIdx;
   }

   public int getPageIdx() {
      return this.PageIdx;
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
