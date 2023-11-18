package GestionEspaceDisque_et_Buffer;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BufferManager {

    private static BufferManager bufferManager = new BufferManager();
    private ArrayList<Frame> listFrames=new ArrayList<Frame>();

    private BufferManager() {
        for(int i=0;i<DBParams.FrameCount;i++){
            listFrames.add(new Frame());
        }
    }

    public static BufferManager getBufferManager() {
        return bufferManager;
    }

    
    public ByteBuffer getPage(PageID pageId) {

        if(!pageId.isValid()){ //if the page is (-1,-1) we shouldn't read its content
            return null;
        }

        ByteBuffer Bf=ByteBuffer.allocate(DBParams.SGBDPageSize).order(ByteOrder.BIG_ENDIAN);
        
        //if the page already exist in a frame
        for(Frame frame:listFrames){   //increment the pincount of the frame if the frame containe the page
            if(frame.compareFrames(pageId)){
                frame.addPinCount();
                frame.setByteBuffer(DiskManager.ReadPage(pageId, Bf));  //get the content of the page
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
        for(Frame frame:listFrames){    //add the page to an empty frame
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

    // get the page with the smallest pinCount
    private Frame getFrameWithSmallestPinCount(){  
        Frame minFrame=listFrames.get(0);
        for(Frame frame:listFrames){
            if(frame.getPinCount()<minFrame.getPinCount()){
                minFrame=frame;
            }
        }
        if(minFrame.getFlagDirty()){   //if the frame was modified and we should free it its content should be written ???
            DiskManager diskManager=DiskManager.getDiskManager();
            diskManager.WritePage(minFrame.getPageId(),minFrame.getByteBuffer() );
        }

        return minFrame;
    }

    //decrement the pin count of the frame and set a flagDirty
    public void freePage(PageID pageId,boolean flagDirty){
        for(Frame frame : listFrames){                          //We search the frame of the pageId
            if(frame.compareFrames(pageId)) {                   //If we find

                frame.setPinCount(frame.getPinCount()-1);       //Decrement PinCount
                frame.setFlagDirty(flagDirty);                  //Set flagDirty
                
                return;                                         //Finish the function
            }
        }
    }

    //reset all the frames and write the content ot thr frames whose flagdirty is true
    public void flushAll(){

        DiskManager diskManager = DiskManager.getDiskManager();                    //Get diskManager
                                                                                 
        for(Frame frame : listFrames){                                              //All the frame
            if(frame.getFlagDirty()){      
                diskManager.WritePage(frame.getPageId(), frame.getByteBuffer());    //write the buffer
            }
            frame.cleanFrame();                                                     //Clean the frame;
           
        }
    }

    //get the frame associated with  the given pageId
    public Frame getFrame(PageID pageId){
        for(Frame frame : listFrames){
            if(frame.getPageId().equals(pageId)){
                return frame;
            }
        }
        return null;
    }

    //display the content of the frame
    public void displaySatetOfFrames(){      //just to check if everything work as expected
        for(int i=0;i<DBParams.FrameCount;i++){
            if(listFrames.get(i).getPageId().isValid()){
                System.out.println("frame : "+i+" page : "+listFrames.get(i).getPageId().getFileIdx()+" "+listFrames.get(i).getPageId().getPageIdx()+" pinCount : "+listFrames.get(i).getPinCount());
                System.out.print(" content of the frame : " );
                System.out.println(DiskManager.readContentOfBuffer(listFrames.get(i).getByteBuffer()));
            }
        }
    }
    

}
