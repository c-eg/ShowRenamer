package uk.co.conoregan.showrenamer.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import uk.co.conoregan.showrenamer.model.Movie;
import uk.co.conoregan.showrenamer.model.Show;
import uk.co.conoregan.showrenamer.model.TVShow;
import uk.co.conoregan.showrenamer.utils.ShowInfo;
import uk.co.conoregan.showrenamer.utils.ShowInfoFromAPI;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class RenameController implements Initializable
{
    // constants
    private static final String ERROR_MESSAGE = "<Unable to find match>";

    // listView lists
    private final ObservableList<String> listRenameFrom = FXCollections.observableArrayList();
    private final ObservableList<String> listRenameTo = FXCollections.observableArrayList();

    // files
    private ArrayList<File> files;

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

    // checkbox
    @FXML
    private CheckBox checkboxSubFolder;

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
            File[] temp = dir.listFiles();

            // if the folder contains files
            if (temp != null && temp.length > 0)
            {
                for (File item : Objects.requireNonNull(dir.listFiles()))
                {
                    addContentsToRenameFrom(item);
                }
            }
        }
    }

    /**
     * Recursive function to add files and subfolders to RenameFrom list
     *
     * @param item file from selected folder in open file dialog
     */
    private void addContentsToRenameFrom(File item)
    {
        if (item.isFile())
        {
            listRenameFrom.add(item.getName().substring(0, item.getName().lastIndexOf('.')));
            files.add(item);
        }
        else if (item.isDirectory() && checkboxSubFolder.isSelected())
        {
            for (File f : Objects.requireNonNull(item.listFiles()))
            {
                addContentsToRenameFrom(f);
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


            String episodeName;
            Show lookedUpShow;

            if (shows.size() > 0)
            {
                lookedUpShow = shows.get(1);

                if (lookedUpShow instanceof TVShow)
                {
                    int season = Integer.parseInt(showInfo.getSeason());
                    int episode = Integer.parseInt(showInfo.getEpisode());

                    // get the episode name for that season and episode
                    episodeName = ShowInfoFromAPI.getEpisodeName(lookedUpShow.getId(), season, episode);

                    // TODO:
                    //  - Replace '*'  with '' in episode name

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

    private void renameFile(File f) throws IOException
    {
        if (f.isFile())
        {
            for (int i = 0; i < listRenameFrom.size(); i++)
            {
                // check if current file name contains name from listRenameFrom
                if (f.getCanonicalPath().contains(listRenameFrom.get(i)))
                {
                    // get path
                    String path = files.get(i).getCanonicalPath();

                    // get last occurance of file to be renamed
                    StringBuilder sb = new StringBuilder();
                    int lastOccurance = path.lastIndexOf(listRenameFrom.get(i));

                    // generate new path string with replaced file name
                    sb.append(path, 0, lastOccurance);
                    sb.append(listRenameTo.get(i));
                    sb.append(path.substring(lastOccurance + listRenameFrom.get(i).length()));

                    // rename
                    Path p = Paths.get(path);
                    Files.move(p, p.resolveSibling(sb.toString()));
                }
            }
        }
        else if (f.isDirectory())   // recursion
        {
            // for each file in the directory, call the recursive function
            for (File temp : Objects.requireNonNull(f.listFiles()))
            {
                renameFile(temp);
            }
        }
    }

    @FXML
    public void renameAll() throws IOException
    {
        if (listRenameFrom.size() == listRenameTo.size() && listRenameFrom.size() > 0)
        {
            for (int i = 0; i < files.size(); i++)
            {
                if (!listRenameTo.get(i).equals(RenameController.ERROR_MESSAGE))
                {
                    renameFile(files.get(i));
                }
            }
        }
    }

    @FXML
    public void renameSelected()
    {
//        if (listRenameFrom.size() > 0)
//        {
//            String toRename = listViewRenameFrom.getSelectionModel().getSelectedItem();
//
//            listViewRenameFrom.getItems().remove(toRemove);
//            listRenameFrom.remove(toRemove);
//        }
    }

    @FXML
    public void removeSelected()
    {
        removeItem(listViewRenameFrom);
    }

    /**
     * Function to remove selected item from listview
     *
     * @param list List for an item to be removed from
     * @param <T>  Type to of object in list
     */
    private <T> void removeItem(ListView<T> list)
    {
        if (list.getItems().size() > 0)
        {
            int index = list.getSelectionModel().getSelectedIndex();
            listRenameFrom.remove(index);
            files.remove(index);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // stops checkbox box resizing when clicking on and off other controls
        checkboxSubFolder.setFocusTraversable(false);

        // creates an arraylist of files
        files = new ArrayList<>();

        // set ListCells inside ListView to wrap text and adjust max width
        setListViewsWrapText(listViewRenameFrom);
        setListViewsWrapText(listViewRenameTo);

        // set list views to observe these lists
        listViewRenameFrom.setItems(listRenameFrom);
        listViewRenameTo.setItems(listRenameTo);

        // placeholder text for ListViews
        listViewRenameFrom.setPlaceholder(new Label("Click \"Add Sauce\" to add files!"));
        listViewRenameTo.setPlaceholder(new Label("Click \"Get Rename Suggestions\" to get renamed file suggestions!"));
    }

    /**
     * Function to set the styling of the ListView to wrap the text if it's too long
     */
    private void setListViewsWrapText(ListView<String> listView)
    {
        listView.setCellFactory(param -> new ListCell<String>()
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