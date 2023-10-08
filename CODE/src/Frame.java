import java.nio.ByteBuffer;

public class Frame {

    private ByteBuffer byteBuff;
    private PageID pageId;
    private int pinCount = 0;
    private boolean flagDirty;

    public Frame(PageID pageId, int pinCount, boolean flagDirty) {
        this.byteBuff = ByteBuffer.allocate(DBParams.SGBDPageSize);
        this.pageId = pageId;
        this.pinCount = 1;
        this.flagDirty = flagDirty;

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

    public PageID getPage() {
        return this.pageId;
    }

    public int getPinCount() {
        return this.pinCount;
    }

    public boolean getFlagDirty() {
        return this.flagDirty;
    }

    public void addSetCount() {
        this.pinCount++;
    }

}
