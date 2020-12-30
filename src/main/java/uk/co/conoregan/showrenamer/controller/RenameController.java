/*
 * This file is part of ShowRenamer.
 *
 * ShowRenamer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ShowRenamer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ShowRenamer.  If not, see <https://www.gnu.org/licenses/>.
 */

package uk.co.conoregan.showrenamer.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.co.conoregan.showrenamer.model.show.Episode;
import uk.co.conoregan.showrenamer.model.show.Movie;
import uk.co.conoregan.showrenamer.model.show.Show;
import uk.co.conoregan.showrenamer.util.api.TheMovieDB;
import uk.co.conoregan.showrenamer.util.show.ShowInfoMatcher;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Class to control the renaming of files that are about shows (movie or tv)
 *
 * @author c -eg
 */
public class RenameController implements Initializable {
    // constants
    public static final String ERROR_MESSAGE = "<Unable to find match>";

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

    /**
     * Creates a show object from a file.
     *
     * @param name name of file
     * @return Show with missing values
     */
    private Show createShow(String name) {
        Show show;
        ShowInfoMatcher showInfo = new ShowInfoMatcher(name);

        // check if tv show or movie
        if (showInfo.getSeason() == null && showInfo.getEpisode() == null) {

            // if year is not in file name
            if (showInfo.getYear() != null) {
                show = new Movie(showInfo.getTitle(), showInfo.getYear());
            }
            // if year is in file name
            else {
                show = new Movie(showInfo.getTitle());
            }
        }
        else {
            show = new Episode(showInfo.getTitle(),
                    Integer.parseInt(showInfo.getSeason()),
                    Integer.parseInt(showInfo.getEpisode()));
        }

        return show;
    }

    /**
     * Open file dialog to select files to be renamed.
     */
    @FXML
    private void openFileDialog() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // set dialog MVC.style to windows
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        // open the dialog
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);    // change to files too, but only allow video extensions
        int returnVal = chooser.showOpenDialog(null);

        // if the user selected a folder
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();

            // get list of files
            File[] temp = dir.listFiles();

            // add files to file list
            if (temp != null && temp.length > 0) {
                for (File item : Objects.requireNonNull(dir.listFiles())) {
                    addFile(item);
                }
            }

            // add shows to listViews
            for (File file : files) {
                listRenameFrom.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
                listRenameTo.add("");
            }
        }
    }

    /**
     * Function is called by user from GUI. Gets rename suggestions for files
     * on left list.
     */
    @FXML
    private void getRenameSuggestions() {
        for (int i = 0; i < listRenameFrom.size(); i++) {
            renameSuggestionTask(i);
        }
    }

    /**
     * Gets rename suggestion.
     *
     * @param index the index
     * @return the rename suggestion
     * @throws IOException the io exception
     */
    private String getRenameSuggestion(int index) throws IOException {
        String newName = RenameController.ERROR_MESSAGE;
        String name = listRenameFrom.get(index);

        // create initial show object from file name
        Show show = createShow(name);

        if (show instanceof Movie) {
            Movie m = (Movie) show;

            // get results from api
            JSONArray results = TheMovieDB.getMovieResults(m.getTitle(), m.getReleaseDate(), "en-US", true);

            if (results != null) {
                JSONObject result = (JSONObject) results.get(0);

                // pull info wanted from results
                String title = result.get("title").toString();
                String id = result.get("id").toString();
                String releaseDate = result.get("release_date").toString().substring(0, 4);

                // set movie object to info retrieved
                m.setTitle(title);
                m.setId(id);
                m.setReleaseDate(releaseDate);

                newName = m.toString();
            }
        }
        else if (show instanceof Episode) {
            Episode e = (Episode) show;

            JSONObject result = TheMovieDB.getTVId(e.getTitle());

            if (result != null) {
                JSONArray results = result.getJSONArray("results");

                if (results.length() > 0) {
                    JSONObject tv = (JSONObject) results.get(0);
                    String title = tv.get("name").toString();
                    String id = tv.get("id").toString();

                    JSONObject episodeResult = TheMovieDB.getEpisodeResults(id, e.getSeasonNumber(), e.getEpisodeNumber());

                    String episodeName = episodeResult.get("name").toString();

                    e.setId(id);
                    e.setTitle(title);
                    e.setEpisodeName(episodeName);

                    newName = e.toString();
                }
            }

        }

        return newName;
    }

    /**
     * Rename suggestion task for threaded api calls.
     *
     * @param index the index
     */
    private void renameSuggestionTask(int index) {
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return getRenameSuggestion(index);
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            Platform.runLater(() -> {
                listRenameTo.set(index, task.getValue());
            });
        });

        task.setOnFailed(workerStateEvent -> {
            Platform.runLater(() -> {
                listRenameTo.set(index, RenameController.ERROR_MESSAGE);
            });
        });

        new Thread(task).start();
    }

    /**
     * Recursive function to add files and sub folders.
     *
     * @param item file from selected folder in open file dialog
     */
    private void addFile(File item) {
        if (item.isFile()) {
            files.add(item);
        }
        else if (item.isDirectory() && checkboxSubFolder.isSelected()) {
            for (File f : Objects.requireNonNull(item.listFiles())) {
                addFile(f);
            }
        }
    }


    /**
     * Function is run when object is initialized.
     *
     * @param url            the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // stops checkbox box resizing when clicking on and off other controls
        checkboxSubFolder.setFocusTraversable(false);

        // creates an arraylist of files
        files = new ArrayList<>();

        // set list views to observe these lists
        listViewRenameFrom.setItems(listRenameFrom);
        listViewRenameTo.setItems(listRenameTo);

        // placeholder text for ListViews
        listViewRenameFrom.setPlaceholder(new Label("Click \"Add Source\" to add files!"));
        listViewRenameTo.setPlaceholder(new Label("Click \"Get Rename Suggestions\" to get renamed file suggestions!"));
    }
}
