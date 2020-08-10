package uk.co.conoregan.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import uk.co.conoregan.model.Movie;
import uk.co.conoregan.model.Show;
import uk.co.conoregan.model.TVShow;
import uk.co.conoregan.utils.ShowInfo;
import uk.co.conoregan.utils.ShowInfoFromAPI;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RenameController implements Initializable
{
    // constants
    private static final String ERROR_MESSAGE = "<Unable to find match>";

    // listView lists
    private final ObservableList<String> listRenameFrom = FXCollections.observableArrayList();
    private final ObservableList<String> listRenameTo = FXCollections.observableArrayList();

    // files
    File[] files;

    // Menu buttons
    @FXML
    private Button buttonMenuRename;
    @FXML
    private Button buttonMenuSearch;
    @FXML
    private Button buttonMenuSettings;

    // listViews
    @FXML
    private ListView<String> listViewRenameFrom;
    @FXML
    private ListView<String> listViewRenameTo;

    // buttons
    @FXML
    private Button buttonOpenFileDialog;
    @FXML
    private Button buttonRenameSelected;
    @FXML
    private Button buttonRenameAll;

    @FXML
    private void openFileDialog() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException
    {
        // set dialog MVC.style to windows
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        // open the dialog
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);    // change to files too, but only allow video extensions
        int returnVal = chooser.showOpenDialog(null);

        // if the user selected a folder
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File dir = chooser.getSelectedFile();

            // get list of files
            files = dir.listFiles();

            // if the folder contains files
            if (files != null)
            {
                for (File item : files)
                {
                    if (item.isFile())
                    {
                        listRenameFrom.add(item.getName().substring(0, item.getName().lastIndexOf('.')));
                    }
                    else if (item.isDirectory()) // add and statement to check if checkbox is ticked
                    {
                        // TODO:
                        //  - Make recursive to check for sub folders and add items in those aswell if checkbox is ticked
                    }
                }
            }
        }
    }

    @FXML
    public void getSuggestedNames() throws IOException
    {
        listRenameTo.clear();

        ShowInfo showInfo;
        ArrayList<Show> shows;

        for (String s : listRenameFrom)
        {
            // get title of tv show or movie from original file name
            showInfo = new ShowInfo(s);

            // get the first show matching title
            shows = ShowInfoFromAPI.getShows(showInfo.getTitle());

            int season, episode;
            String episodeName;
            Show lookedUpShow;

            if (shows.size() > 0)
            {
                lookedUpShow = shows.get(0);

                if (lookedUpShow instanceof TVShow)
                {
                    //System.out.println(((TVShow) lookedUpShow).getAllEpisodeNames());

                    // get the season and episode number from the original file
                    season = Integer.parseInt(showInfo.getSeason());
                    episode = Integer.parseInt(showInfo.getEpisode());

                    // get the episode name for that season and episode
                    episodeName = ((TVShow) lookedUpShow).getEpisodeName(season, episode);

                    listRenameTo.add((lookedUpShow.getTitle() + " - S" + showInfo.getSeason() + "E" + showInfo.getEpisode() + " - " + episodeName).replaceAll(":", ""));
                }
                else if (lookedUpShow instanceof Movie)
                {
                    listRenameTo.add((lookedUpShow.getTitle() + " (" + ((Movie) lookedUpShow).getReleaseDate().substring(0, 4) + ")").replaceAll(":", ""));
                }
            }
            else
            {
                listRenameTo.add(RenameController.ERROR_MESSAGE);
            }
        }
    }

    @FXML
    public void renameAll() throws IOException
    {
        if (listRenameFrom.size() == listRenameTo.size() && listRenameFrom.size() > 0)
        {
            int j = 0;

            for (int i = 0; i < files.length; i++)
            {
                if (files[i].isDirectory())
                {
                    j += 1;
                }
                else if (!listRenameTo.get(i - j).equals(RenameController.ERROR_MESSAGE))
                {
                    // get name of file
                    String name = files[i].getName();
                    int lastIndex = name.lastIndexOf('.');
                    String ext = name.substring(lastIndex);

                    // rename
                    String newName = files[i].getCanonicalPath().replace(listRenameFrom.get(i - j), listRenameTo.get(i - j));

                    Path p = Paths.get(files[i].getCanonicalPath());
                    Files.move(p, p.resolveSibling(newName));
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        setListViewsWrapText();

        // set list views to observe these lists
        listViewRenameFrom.setItems(listRenameFrom);
        listViewRenameTo.setItems(listRenameTo);

        listViewRenameFrom.setPlaceholder(new Label("Click \"Add Sauce\" to add files!"));
        listViewRenameTo.setPlaceholder(new Label("Click \"Get Rename Suggestions\" to get renamed file suggestions!"));
    }

    /**
     * Function to set the styling of the ListViews to wrap the text if it's too long
     */
    private void setListViewsWrapText()
    {
        listViewRenameFrom.setCellFactory(param -> new ListCell<String>()
        {
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);

                if (empty || item == null)
                {
                    setGraphic(null);
                    setText(null);
                }
                else
                {
                    // set the width's
                    setMinWidth(param.getWidth() - 40);
                    setMaxWidth(param.getWidth() - 40);
                    setPrefWidth(param.getWidth() - 40);

                    // allow wrapping
                    setWrapText(true);

                    setText(item);
                }
            }
        });

        listViewRenameTo.setCellFactory(param -> new ListCell<String>()
        {
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);

                if (empty || item == null)
                {
                    setGraphic(null);
                    setText(null);
                }
                else
                {
                    // set the width's
                    setMinWidth(param.getWidth() - 40);
                    setMaxWidth(param.getWidth() - 40);
                    setPrefWidth(param.getWidth() - 40);

                    // allow wrapping
                    setWrapText(true);

                    setText(item);
                }
            }
        });
    }
}