
public class BufferManager {

    private static BufferManager bufferManager = new BufferManager();
    private Frame[] listFrames;

    private BufferManager() {
        listFrames = new Frame[DBParams.FrameCount];

    }

    public static BufferManager getBufferManager() {
        return bufferManager;
    }

    public void GetPage(PageID pageId, DiskManager diskManager) {

        // int vide = -1;

        // for (int i = 0; i < listFrames.length; i++) {

        //     if (listFrames[i] != null) {
        //         if ((listFrames[i].getPage().getFileIdx() == pageId.getFileIdx())
        //                 && (listFrames[i].getPage().getPageIdx() == pageId.getPageIdx())) {
        //             listFrames[i].addSetCount();
        //             // return listFrames[i].getByteBuffer();
        //         }

        //     } else {
        //         vide = i;
        //     }

        // }
        // if (vide >= 0) {
        //     listFrames[vide] = new Frame(pageId, vide, false);

        // }
        // ByteBuffer Bf = listFrames[vide].getByteBuffer();
        // diskManager.AllocPage();
        // diskManager.ReadPage(pageId, Bf);
        // System.out.println(new String(Bf.array()));

    }

}
