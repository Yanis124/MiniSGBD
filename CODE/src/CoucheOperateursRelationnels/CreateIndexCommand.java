package CoucheOperateursRelationnels;

import CoucheAcces_Fichier.DatabaseInfo;
import CoucheAcces_Fichier.TableInfo;

public class CreateIndexCommand {
    private String userCommand;
    private String relationName;
    private String columnName;
    private String order;

    //Constructor for the CreateIndexCommand class using the parsing of the userCommand
    public CreateIndexCommand(String userCommand) {
        this.userCommand = userCommand;

        // Split the user command to extract relevant information
        String[] commandSplit = userCommand.split(" ");
        this.relationName = commandSplit[2]; // Relation name is the 3rd word in the command

        this.columnName = commandSplit[3].split("=")[1]; // Extracting the column name from KEY=nomColonne

        this.order = commandSplit[4].split("=")[1]; // Extracting the order from ORDER=ordre
    }

    // Method to execute the CreateIndexCommand
    public void Execute() {
        // Get the table info
        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(this.relationName);

        // Get the column index
        int columnIndex = tableInfo.getTableCols().indexOf(this.columnName);

        // Create the index
        IndexInfo indexInfo = new IndexInfo(this.relationName, columnIndex, this.order);
    }
}
