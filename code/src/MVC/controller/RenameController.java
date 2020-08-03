package MVC.controller;

import MVC.model.Movie;
import MVC.model.Show;
import MVC.model.TVShow;
import MVC.utils.ShowInfoFromAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import uk.co.conoregan.ShowInfo;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RenameController implements Initializable
{
    // listView lists
    private final ObservableList<String> listRenameFrom = FXCollections.observableArrayList();
    private final ObservableList<String> listRenameFromFullPath = FXCollections.observableArrayList();
    private final ObservableList<String> listRenameTo = FXCollections.observableArrayList();
    private final ObservableList<String> listRenameToFullPath = FXCollections.observableArrayList();
    // Menu buttons
    @FXML
    private Button buttonMenuRename;
    @FXML
    private Button buttonMenuSearch;
    @FXML
    private Button buttonMenuSettings;

    // listViews
    @FXML
    private ListView listViewRenameFrom;
    @FXML
    private ListView listViewRenameTo;
    // buttons
    @FXML
    private Button buttonOpenFileDialog;
    @FXML
    private Button buttonRenameSelected;
    @FXML
    private Button buttonRenameAll;
    // textFields
    @FXML
    private TextField textFieldDirectory;

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

                ShowInfo showInfo;
                ArrayList<Show> shows;

                for (String s : listRenameFrom)
                {
                    // get title of tv show or movie from original file name
                    showInfo = new ShowInfo(s);

                    // get the first show matching title
                    shows = ShowInfoFromAPI.getShows(showInfo.getTitle());

                    int season;
                    int episode;
                    String episodeName;
                    Show lookedUpShow;

                    if (shows.size() > 0)
                    {
                        lookedUpShow = shows.get(0);

                        if (lookedUpShow instanceof TVShow)
                        {
                            // get the season and episode number from the original file
                            season = Integer.parseInt(showInfo.getSeason());
                            episode = Integer.parseInt(showInfo.getEpisode());

                            // get the episode name for that season and episode
                            episodeName = ((TVShow) lookedUpShow).getEpisodeName(season, episode);

                            listRenameTo.add(lookedUpShow.getTitle() + " - S" + showInfo.getSeason() + "E" + showInfo.getEpisode() + " - " + episodeName);
                        }
                        else if (lookedUpShow instanceof Movie)
                        {
                            // get the info
                            listRenameTo.add(lookedUpShow.getTitle() + " (" + ((Movie) lookedUpShow).getReleaseDate().substring(0, 4) + ")");
                        }
                    }
                    else
                    {
                        listRenameTo.add("<Unable to find match>");
                    }
                }
            }
        }
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