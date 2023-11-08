package GestionEspaceDisque_et_Buffer;
import java.util.ArrayList;
import java.nio.ByteBuffer;

public class BufferManager {

    private static BufferManager bufferManager = new BufferManager();
    private ArrayList<Frame> listFrames=new ArrayList<Frame>();

    private ArrayList<Frame> EmptyFrames = new ArrayList<Frame>();

    private BufferManager() {
        for(int i=0;i<DBParams.FrameCount;i++){
            listFrames.add(new Frame());
        }
    }

    public static BufferManager getBufferManager() {
        return bufferManager;
    }

    public ByteBuffer getPage(PageID pageId) {
        ByteBuffer Bf=ByteBuffer.allocate(DBParams.SGBDPageSize);
        
        //if the page already exist in a frame
        for(Frame frame:listFrames){   //increment the pincount of the frame if the frame containe the page
            if(frame.compareFrames(pageId)){
                frame.addPinCount();
                frame.setByteBuffer(DiskManager.ReadPage(pageId, Bf));
                return Bf;
                
                
            }
        }

        //if not we will check if there is an empty frame
        /*
        if(!EmptyFrames.isEmpty()){
            EmptyFrames.getLast().setPage(pageId);
            EmptyFrames.getLast().addPinCount();
            EmptyFrames.getLast().setByteBuffer(DiskManager.ReadPage(pageId, Bf));
            Bf = EmptyFrames.getLast().getByteBuffer();
            EmptyFrames.removeLast();
            return Bf;
        }
        */
        for(Frame frame:listFrames){
            if(frame.frameIsEmpty()){
                frame.setPage(pageId);
                frame.addPinCount();
                frame.setByteBuffer(DiskManager.ReadPage(pageId, Bf));
                Bf=frame.getByteBuffer();
                return Bf;

            }
        }

        //otherwise we will replace the page of the frame with the smallest pincount
        Frame minFrame=getFrameWithSmallestPinCount();
        minFrame.setPage(pageId);
        minFrame.setPinCount(1);
        minFrame.setByteBuffer(DiskManager.ReadPage(pageId, Bf));
        Bf=minFrame.getByteBuffer();
        
        //we need to check if the dirty flag of the page that we will replace is 1
        return Bf;
    }

    private Frame getFrameWithSmallestPinCount(){  //return the page with the smallest pincount
        Frame minFrame=listFrames.get(0);
        for(Frame frame:listFrames){
            if(frame.getPinCount()<minFrame.getPinCount()){
                minFrame=frame;
            }
        }
        return minFrame;
    }

    public void freePage(PageID pageId,boolean flagDirty){
        for(Frame frame : listFrames){                          //We search the frame of the pageId
            if(frame.compareFrames(pageId)) {                   //If we find

                frame.setPinCount(frame.getPinCount()-1);       //Decrement PinCount
                frame.setFlagDirty(flagDirty);                  //Set flagDirty

                if(frame.getPinCount() == 0){                   //If no one want the page
                    frame.cleanFrame();                        //Set an Empty PageId
                    EmptyFrames.add(frame);
                }


                return;                                         //Finish the function
            }
        }
    }

    public void flushAll(){

        DiskManager diskManager = DiskManager.getDiskManager();                     //Get diskManager

        for(Frame frame : listFrames){                                              //All the frame
            if(frame.getFlagDirty()){                                               //If we have the write
                diskManager.WritePage(frame.getPageId(), frame.getByteBuffer());
            }
            frame.cleanFrame();                                                     //Clean the frame;
            if(!EmptyFrames.contains(frame))  EmptyFrames.add(frame);
        }
    }

    

    public void displaySatetOfFrames(){      //just to check if everything work as expected
        for(int i=0;i<DBParams.FrameCount;i++){
            System.out.println("frame : "+i+" page : "+listFrames.get(i).getPageId().getFileIdx()+" "+listFrames.get(i).getPageId().getPageIdx()+" pinCount : "+listFrames.get(i).getPinCount());
            System.out.println(" content of the frame : " );
            System.out.println(DiskManager.readContentOfBuffer(listFrames.get(i).getByteBuffer()));
        }
    }
    

}
