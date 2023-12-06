package GestionEspaceDisque_et_Buffer;

import java.nio.ByteBuffer;
import java.io.Serializable;

public class PageID implements Serializable {

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

   public boolean isValid(){  //check if the page is a valid page
      if(this.fileIdx<0 || this.pageIdx<0 ){
         return false;
      }
      return true;
   }

   public boolean equals(PageID pageId){   //check if two pages are equal
      if(this.fileIdx==pageId.getFileIdx() && this.pageIdx==pageId.getPageIdx()){
         return true;
      }
      return false;
   }

   @Override
   public String toString(){
      return "("+fileIdx+","+pageIdx+")"+",";
   }

}
