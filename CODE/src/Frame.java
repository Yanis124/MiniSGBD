import java.nio.ByteBuffer;

public class Frame {

    private ByteBuffer byteBuff;
    private PageID pageId;
    private int pinCount ;
    private boolean flagDirty;

    public Frame() {
        this.byteBuff = ByteBuffer.allocate(DBParams.SGBDPageSize);
        this.pageId = new PageID();
        this.pinCount = 0;
        this.flagDirty = false;

    }

    public void setByteBuffer(ByteBuffer byteBuff) {
        this.byteBuff = byteBuff;
    }

    public void setPage(PageID page) {
        this.pageId = page;
    }

    public void setPinCount(int pinCount) {
        this.pinCount = pinCount;
    }

    public void setFlagDirty(boolean flagDirty) {
        this.flagDirty = flagDirty;
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuff;
    }

    public PageID getPageId() {
        return this.pageId;
    }

    public int getPinCount() {
        return this.pinCount;
    }

    public boolean getFlagDirty() {
        return this.flagDirty;
    }

    public void addPinCount() {
        this.pinCount++;
    }

    public boolean compareFrames(PageID page){ 
        if(this.pageId.getFileIdx()==page.getFileIdx() && this.pageId.getPageIdx()==page.getPageIdx()){
            return true;
        }
        return false;
    }

    public boolean frameIsEmpty(){
        if(this.pageId.getFileIdx()==-1 && this.pageId.getPageIdx()==-1){
            return true;
        }
        return false;
    }

}
