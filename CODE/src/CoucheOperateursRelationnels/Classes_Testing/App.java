package CoucheOperateursRelationnels.Classes_Testing;

import CoucheAcces_Fichier.DatabaseManager;
import GestionEspaceDisque_et_Buffer.DBParams;
import java.io.File;
import java.util.Scanner;

/*
 * Classe App qui teste les tables de la base de données
 */
public class App {
    /*
     * ----------------   Main   ----------------
     * @param : args : arguments
     */
    public static void main(String[] args) { 

        // Configuration de DBParams
        
        DBParams.DBPath = ".." + File.separator + ".." + File.separator + "DB";
        DBParams.DMFileCount = 4;
        DBParams.SGBDPageSize = 4096;
        DBParams.FrameCount = 10;
        DBParams.PageFull = 100;
        DBParams.nbPageFile = 50;

        DatabaseManager.Init();
        Scanner scanner = new Scanner(System.in);

        System.out.println("--------------------------------------------------");
        System.out.println("Bienvenue dans l'application de test de base de donnees !");
        System.out.println("Instructions :");
        System.out.println("1. Creer une table");
        System.out.println("   Exemple : CREATE TABLE table1 (col1:INT, col2:FLOAT, col3:STRING(15))");
        System.out.println("");
        System.out.println("2. Pour reinitialiser la base de donnees");
        System.out.println("   Exemple : RESETDB");
        System.out.println("");
        System.out.println("3. Inserer dans une table");
        System.out.println("   Exemple : INSERT INTO table1 VALUES (1, 3.14, 'exemple')");
        System.out.println("");
        System.out.println("4. Selectionner dans une table");
        System.out.println("   Exemple : SELECT * FROM table1, table2 WHERE table1.col1 = table2.col2");
        System.out.println("");
        System.out.println("5. Supprimer dans une table");
        System.out.println("   Exemple : DELETE FROM table1 WHERE col1 = 2");
        System.out.println("Tapez 'EXIT' pour quitter.");
        System.out.println("--------------------------------------------------");
        System.out.println("");

        while (true) {
           
            System.out.print("Entrez une commande, veuillez a bien respecter l'ecrire des commandes, sinon il faut relancer le programme: ");
            String userInput = scanner.nextLine().trim();
            

            if (userInput.equalsIgnoreCase("EXIT")) {
                break;
            }


                DatabaseManager.Init();
                DatabaseManager.ProcessCommand(userInput);
                

                if(!userInput.startsWith("RESETDB") && !userInput.startsWith("SELECT")){
                    DatabaseManager.Finish();    
                }

            

            
        }

        
        scanner.close();
    }
}

