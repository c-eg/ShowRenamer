package MVC.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RenameController implements Initializable
{
    // Menu buttons
    @FXML private Button buttonMenuRename;
    @FXML private Button buttonMenuSearch;
    @FXML private Button buttonMenuSettings;

    // buttons
    @FXML private Button buttonOpenFileDialog;
    @FXML private Button buttonRenameSelected;
    @FXML private Button buttonRenameAll;

    // textFields
    @FXML private TextField textFieldDirectory;

    // listViews
    @FXML private ListView listViewRenameFrom;
    @FXML private ListView listViewRenameTo;

    // listView lists
    private ObservableList<String> listRenameFrom = FXCollections.observableArrayList();
    private ObservableList<String> listRenameTo = FXCollections.observableArrayList();

    @FXML
    private void openFileDialog() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException
    {
        // set dialog style to windows
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        // open the dialog
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(null);

        // if the user selected a folder
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            textFieldDirectory.setText(chooser.getSelectedFile().toString());

            File dir = chooser.getSelectedFile();

            // get list of files
            File[] files = dir.listFiles();

            // if the folder contains files
            if (files != null)
            {
                for (File item : files)
                {
                    if (item.isFile())
                    {
                        listRenameFrom.add(item.toString());
                    }
                    else if (item.isDirectory())
                    {
                        // TODO:
                        //  - Make recursive to check for sub folders and add items in those aswell if checkbox is ticked
                    }
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // set list views to observe these lists
        listViewRenameFrom.setItems(listRenameFrom);
        listViewRenameTo.setItems(listRenameTo);
    }
}
