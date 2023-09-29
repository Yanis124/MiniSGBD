import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        DBParams.DBPath = "../../DB";
        DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;  //max number of file in db


        DiskManager diskManager=new DiskManager();
        ArrayList <Integer> list =new ArrayList<Integer>();
        list.add(1);  //allocate 1 page   
        list.add(4);  //allocate 4 pages  //it should create 1 file
        //list.add(7); //allocate 7 pages   // it should cretae 2 files    
        //list.add(16);  //allocate 16 pages //it should create 4 files

        //make sur when testing that the number of page allocated doesn't exceed 4*DMFFileCount we should handle this 

        //add the number of page you want to allocate so you can test it

        for(int i=0;i<list.size();i++){
            for(int j=0;j<list.get(i);j++){
                diskManager.AllocPage();
            }            

  
        }

        

    }

}

