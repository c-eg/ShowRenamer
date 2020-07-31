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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            for (String s : listRenameFrom)
            {
                // TODO:
                //  - fix this
                //listRenameTo.add(ShowInfoFromAPI.getShows(s).get(0));


                // get title of tv show or movie from original file name
                System.out.println("original file: " + s);

                String title = getTitleFromFile(s);
                System.out.println("new file: " + title + "\n");

                // get the first show matching title

                // if tv show

                // get the season and episode number from the original file

                // get the episode name for that season and episode

                // if movie

                // get the info

                // rename
            }
        }
    }

    private String getTitleFromFile(String fileName)
    {
        Pattern pattern = Pattern.compile("^(.+).(\\d{4}p)");           // CHANGE REGEX FOR BETTER TITLE MATCH
        Matcher m = pattern.matcher(fileName);

        if (m.find())
        {
            return m.group(1);
        }
        else
        {
            return null;
        }


//        String[] data = fileName.split("\\.");
//
//        if (data.length == 0)
//        {
//            data = fileName.split("\\s+");  // space character
//        }
//        else if (data.length == 0)
//        {
//            data = fileName.split("_");
//        }
//
//        StringBuilder title = new StringBuilder();
//
//        int i = 0;
//        // while the split file string doesn't contain 4 numbers in a row
//        while (data.length > i && !data[i].matches("^(.+).(\\d{4}p)"))
//        {
//            title.append(data[i]).append(" ");
//            i++;
//        }
//
//        return title.toString().trim();
    }

    @FXML
    public void renameAll() throws IOException
    {
        // TODO:
        //  - Add functionality to rename files
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // set list views to observe these lists
        listViewRenameFrom.setItems(listRenameFrom);
        listViewRenameTo.setItems(listRenameTo);
    }
}