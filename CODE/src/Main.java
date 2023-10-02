//import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        DBParams.DBPath = "../../DB";
        DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 3;  //max number of file in db


        DiskManager diskManager=new DiskManager();
        //ArrayList <Integer> list =new ArrayList<Integer>();
        int nbPages = 5;
        //list.add(1);  //allocate 1 page   
        //list.add(4);  //allocate 4 pages  
        //list.add(7); //allocate 7 pages       
        //list.add(5);  //allocate 50 pages 

        //make sur when testing that the number of page allocated doesn't exceed 4*DMFFileCount we should handle this 

        //add the number of page you want to allocate so you can test it

        for(int i=0;i<nbPages;i++){
            for(int j=0;j<DBParams.DMFileCount;j++){
                diskManager.AllocPage();
            }            

  
        }

    }

}

