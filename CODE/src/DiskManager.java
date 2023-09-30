import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;

public class DiskManager {
    
    
    private Map<Integer, ArrayList<PageID>> filePageId = new HashMap<>();  // {file:[page]}
    private PageID nextAllocatedPage=new PageID(0,0);   
    private PageID currentAllocatedPage=new PageID(0,0);
    private ArrayList <PageID> desalocatedPage=new ArrayList<PageID>(); // for desallocated page
    

    DiskManager(){
        for(int i=0;i<DBParams.DMFileCount;i++){                              // get the available files in the db
            String filePatheName = DBParams.DBPath + "/F" + i + ".data";    //assume that the db should always have DMFileCount

            File file = new File(filePatheName);
            if(!file.exists()){
                DBParams.createFile();
            }
            else{
            }
            filePageId.put(i, new ArrayList<PageID>());
        }
    }
    
    

    public PageID AllocPage(){

        if(desalocatedPage.size()!=0){     //if a page was desalocated
            PageID Page=desalocatedPage.get(desalocatedPage.size()-1);
            desalocatedPage.remove(desalocatedPage.size()-1);
            return Page;
        }

        int currentFileId=nextAllocatedPage.getFileIdx();
        int currentPageId=nextAllocatedPage.getPageIdx();
        
         
        PageID currentSmallestPage=getSmallestPage();
        int currentSmallestFileId=currentSmallestPage.getFileIdx();
        int currentSmallestPageId=currentSmallestPage.getPageIdx();

        currentAllocatedPage.setFileIdx(currentSmallestFileId);
        currentAllocatedPage.setPageIdx(currentSmallestPageId);
        ArrayList<PageID> filePages = filePageId.getOrDefault(currentSmallestFileId, new ArrayList<>());
        filePages.add(currentAllocatedPage);
        filePageId.put(currentSmallestFileId, filePages);
        

        PageID nextSmallestPage=getSmallestPage();
        int nextSmallestFileId=nextSmallestPage.getFileIdx();
        int nextSmallestPageId=nextSmallestPage.getPageIdx();
        
        nextAllocatedPage.setFileIdx(nextSmallestFileId);
        nextAllocatedPage.setPageIdx(nextSmallestPageId);
 
        System.out.println("current :   "+currentFileId+"/"+ currentPageId+"\t");
        System.out.println("next :   "+nextAllocatedPage.getFileIdx()+"/"+ nextAllocatedPage.getPageIdx()+"\t");
       
        return currentAllocatedPage;
    }

    private PageID getSmallestPage(){    //get the last page of the file with the smallest allocated page 
        int smallestFileSize=Integer.MAX_VALUE;
        int fileIdxWithSmallestSize = -1;

        for (int fileIdx : filePageId.keySet()) {     //get the file with the less number of pages
            int fileSize = filePageId.get(fileIdx).size();
            if (fileSize < smallestFileSize) {
                smallestFileSize = fileSize;
                fileIdxWithSmallestSize = fileIdx;
            }
        }
        return new PageID(fileIdxWithSmallestSize,smallestFileSize);
    }
}
        





