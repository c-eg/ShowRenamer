package MVC.controller;

import MVC.ShowInfoFromAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
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
    private ObservableList<String> listRenameFromFullPath = FXCollections.observableArrayList();
    private ObservableList<String> listRenameTo = FXCollections.observableArrayList();
    private ObservableList<String> listRenameToFullPath = FXCollections.observableArrayList();

    @FXML
    private void openFileDialog() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException
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
                        listRenameFrom.add(item.getName().substring(0, item.getName().lastIndexOf('.')));
                        listRenameFromFullPath.add(item.toString());
                    }
                    else if (item.isDirectory()) // add and statement to check if checkbox is ticked
                    {
                        // TODO:
                        //  - Make recursive to check for sub folders and add items in those aswell if checkbox is ticked
                    }
                }
            }
        }

        for (String s : listRenameFrom)
        {
            listRenameTo.add(getSuggestedNames(s));
        }
    }

    private String getSuggestedNames(String name) throws IOException
    {
        return ShowInfoFromAPI.generateMovieFileName(ShowInfoFromAPI.getShowInformation(name));
    }

    private String getSuggestedNames(String name, int seasonNumber, int episodeNumber) throws IOException
    {
        JSONObject showInformation = ShowInfoFromAPI.getShowInformation(name);
        JSONObject tvShowInformation = ShowInfoFromAPI.getTVShowInformation((int) showInformation.get("id"), seasonNumber, episodeNumber);

        return ShowInfoFromAPI.generateTVFilename(tvShowInformation);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // set list views to observe these lists
        listViewRenameFrom.setItems(listRenameFrom);
        listViewRenameTo.setItems(listRenameTo);
    }
}
