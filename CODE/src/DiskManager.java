import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.nio.ByteBuffer;

public class DiskManager {
    
    
    private Map<Integer, ArrayList<PageID>> filePageId = new HashMap<>();  // {file:[page]} 
    private PageID currentAllocatedPage = new PageID(0,0);
    private ArrayList <PageID> desalocatedPage = new ArrayList<PageID>(); // for desallocated page
    

    DiskManager(){
        for(int i=0;i<DBParams.DMFileCount;i++){                              // get the available files in the db
            String filePatheName = DBParams.DBPath + "/F" + i + ".data";    //assume that the db should always have DMFileCount

            File file = new File(filePatheName);
            if(!file.exists()){
                DBParams.createFile();
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
 
        PageID currentSmallestPage=getSmallestPage();
        int currentSmallestFileId=currentSmallestPage.getFileIdx();
        int currentSmallestPageId=currentSmallestPage.getPageIdx();

        currentAllocatedPage.setFileIdx(currentSmallestFileId);
        currentAllocatedPage.setPageIdx(currentSmallestPageId);
        ArrayList<PageID> filePages = filePageId.getOrDefault(currentSmallestFileId, new ArrayList<>());
        filePages.add(currentAllocatedPage);
        filePageId.put(currentSmallestFileId, filePages);
        
        System.out.println("current file:   "+currentAllocatedPage.getFileIdx()+"/ page "+ currentAllocatedPage.getPageIdx()+"\t");
    
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


    public void ReadPage(PageID pageId, ByteBuffer buff) {
        int fileIdx = pageId.getFileIdx();
        int pageIdx = pageId.getPageIdx();
    
        if (filePageId.containsKey(fileIdx) && pageIdx >= 0) { //pageIdx >= 0, pour l'instant c'est pour éviter l'erreur
            ArrayList<PageID> currentFilePagesTab = filePageId.get(fileIdx);  // on va chercher la tableau correspondant à la clé fileIdx
            int numPages = currentFilePagesTab.size();
    
            if (pageIdx < numPages) {
                if (buff.capacity() >= DBParams.SGBDPageSize) { 
                    //Nous allons devoir copier le contenu de la page dans le buff, mais on connait pas encore le contenue de la page
                    buff.clear(); // pour s'assurer que le buff est vide avant de le remplir et réinitialisé oson curseur
                    //buff.put(PageContent à définir plus tard); 
                    buff.flip(); 
                } else {
                    System.out.println("Ce message dit qu'il faut gérer une exception à cet endroit ! ");
                }
            } else {
                System.out.println("Ce message dit qu'il faut gérer une exception à cet endroit ! ");
            }
        } else {
            System.out.println("Ce message dit qu'il faut gérer une exception à cet endroit ! ");
        }
    }

    public void DeallocPage(PageID pageId) {

        int fileIdx = pageId.getFileIdx();
        int pageIdx = pageId.getPageIdx();
    
        if (filePageId.containsKey(fileIdx)) { // pour vérifier que le fichier correspondant à la page existe dans la map
            ArrayList<PageID> currentFilePagesTab = filePageId.get(fileIdx); // un tableau temporaire qui va mettre à jour à la fin la map
    
            if (pageIdx >= 0 && pageIdx < currentFilePagesTab.size()) { //vérifier que la page existe dans le fichier 'mieux avec try catch)
                
                PageID deallocatedPage = currentFilePagesTab.get(pageIdx);
    
                desalocatedPage.add(deallocatedPage); // ajout de la page qu'on va désallouer au tableau des pages à désallouer
    
                currentFilePagesTab.remove(pageIdx); 
    
                filePageId.put(fileIdx, currentFilePagesTab); // mettre à jour la map avec le nouveau tableau sans la page qui a été désallouée
    
                System.out.println("Voici la page désallouée : Fichier n°" + fileIdx + ", Page n°" + pageIdx);

            } else {
                System.out.println("Ce message dit qu'il faut gérer une exception à cet endroit !");
            }
        } else {
            System.out.println("Ce message dit qu'il faut gérer une exception à cet endroit !");
        }
    }
    
    
    public int GetCurrentCountAllocPages() {
        int currentAllocatedPages = 0;
        for (ArrayList<PageID> pages : filePageId.values()) { // pour chaque tableau des map.values
            currentAllocatedPages += pages.size();
        }
        return currentAllocatedPages;
    }
    
    
}
        





