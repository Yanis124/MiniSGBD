import java.util.ArrayList;
import java.nio.*;

public class Main {
    public static void main(String[] args) {
        DBParams.DBPath = "../../DB";
        DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4; // max number of file in db

        DiskManager diskManager = new DiskManager();
        // ArrayList<Integer> list = new ArrayList<Integer>();
        // diskManager.AllocPage();
        // diskManager.AllocPage();
        // diskManager.AllocPage();
        // diskManager.AllocPage();
        diskManager.AllocPage();
        diskManager.AllocPage();
        diskManager.AllocPage();
        diskManager.AllocPage();
        PageID page = diskManager.AllocPage();

        String dataToAppend = "This data will be appended to the file " + page.getFileIdx() + " page : "
                + page.getPageIdx();
        byte[] dataBytes = dataToAppend.getBytes(); // convert string to a byte code
        ByteBuffer buffWrite = ByteBuffer.wrap(dataBytes);
        diskManager.WritePage(page, buffWrite);

        ByteBuffer buffRead = ByteBuffer.allocate(DBParams.SGBDPageSize);
        diskManager.ReadPage(page, buffRead);

    }

}
