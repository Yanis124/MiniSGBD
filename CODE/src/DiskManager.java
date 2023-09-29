import java.util.ArrayList;
import java.io.File;

public class DiskManager {
    
    private ArrayList<PageID> listAllocatedPages=new ArrayList<PageID>();  //list of allocated page
    private ArrayList<Integer> listFiles=new ArrayList<Integer>();   //list of available file 
    private PageID nextAllocatedPage=new PageID(0,0);
    private PageID currentAllocatedPage=new PageID(0,0);
    private PageID desalocatedPage=null; 
    

    DiskManager(){
        for(int i=0;i<DBParams.DMFileCount;i++){                              // get the available files in the data base
            String filePatheName = DBParams.DBPath + "/F" + i + ".data";
            File file = new File(filePatheName);
            if(file.exists()){
                listFiles.add(i);
            }
        }
    }
    
    

    public PageID AllocPage(){

        if(desalocatedPage!=null){     //if a page was desalocated
            PageID Page=desalocatedPage;
            desalocatedPage=null;
            return Page;
        }
        
       int  nextFile=nextAllocatedPage.getFileIdx();
       int nextPage=nextAllocatedPage.getPageIdx();

       

        //set the current allocate page

        if(listFiles.size()==0 || nextFile > listFiles.size()-1){  //if all pages of all files are allocated we create a new file
            if(nextFile<DBParams.DMFileCount){
            
            int numFile=DBParams.createFile();
            listFiles.add(numFile);
            currentAllocatedPage.setFileIdx(numFile);
            currentAllocatedPage.setPageIdx(0);
            }
            else{
                System.out.println("not enough file");  //if we exceed the limit of file 
                return new PageID();
            }
            
        }
        else{
            
            currentAllocatedPage.setFileIdx(nextFile);
            currentAllocatedPage.setPageIdx(nextPage); 
        }

        listAllocatedPages.add(currentAllocatedPage);  
        int currentFileId=currentAllocatedPage.getFileIdx();
        int currentPageId=currentAllocatedPage.getPageIdx();

        //set the nextAllocatPage
        
        
        
        if(((currentPageId+1)%DBParams.DMFileCount)==0){    //if all pages of the current file are allocated we switch to another file
            nextAllocatedPage.setFileIdx(currentFileId+1);
            nextAllocatedPage.setPageIdx(0);
        }
        else{
            nextAllocatedPage.setFileIdx(currentFileId);
            nextAllocatedPage.setPageIdx(currentPageId+1);
        }
        System.out.print("current :   "+currentFileId+"/"+ currentPageId+"\t");
        System.out.println("next :    "+nextAllocatedPage.getFileIdx()+"/"+nextAllocatedPage.getPageIdx()+"\t");


        

        return currentAllocatedPage;
        


    }
}
