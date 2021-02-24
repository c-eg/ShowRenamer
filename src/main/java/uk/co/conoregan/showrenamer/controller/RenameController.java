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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.co.conoregan.showrenamer.model.RenamedStuff;
import uk.co.conoregan.showrenamer.model.show.Episode;
import uk.co.conoregan.showrenamer.model.show.Movie;
import uk.co.conoregan.showrenamer.model.show.Show;
import uk.co.conoregan.showrenamer.util.api.TheMovieDB;
import uk.co.conoregan.showrenamer.util.show.ShowInfoMatcher;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Class to control the renaming of files that are about shows (movie or tv)
 *
 * @author c-eg
 */
public class RenameController implements Initializable {
    // constants
    public static final String ERROR_MESSAGE = "<Unable to find match>";

    // listView lists
    private final ObservableList<String> listRenameFrom = FXCollections.observableArrayList();
    private final ObservableList<String> listRenameTo = FXCollections.observableArrayList();

    // files
    private ObservableList<File> files = FXCollections.observableArrayList();

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

    // treeViews
    @FXML
    private TreeView<String> treeViewRenameFrom;
    @FXML
    private TreeView<String> treeViewRenameTo;

    // treemap
    TreeMap<File, RenamedStuff> fileMapToNames;

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

    private void initListenerObservableArray() {
        files.addListener((ListChangeListener<File>) change -> {
            System.out.println("Changed on " + change);

            if (change.next())
                addTreeNode(treeViewRenameFrom.getRoot(), files.get(change.getFrom()));
        });
    }

    private void addTreeNode(TreeItem<String> rootToAddTo, File dir) {
        TreeItem<String> currentRoot = new TreeItem<>(dir.getName());
        rootToAddTo.getChildren().add(currentRoot);

        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if (f.isFile()) {
                    TreeItem<String> item = new TreeItem<>(f.getName());
                    currentRoot.getChildren().add(item);
                    RenamedStuff rs = new RenamedStuff(f.getName());
                    fileMapToNames.put(f, rs);
                }
                else if (checkboxSubFolder.isSelected())
                    addTreeNode(currentRoot, f);
            }
        }
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
            files.add(dir);
        }
    }

    /**
     * Function is called by user from GUI. Gets rename suggestions for files
     * on left list.
     */
    @FXML
    private void getRenameTitles() {
        TreeItem<String> rootCopy = deepCopyTreeItem(treeViewRenameFrom.getRoot());
        treeViewRenameTo.setRoot(rootCopy);

        getRenameTitle(rootCopy);
    }

    private void getRenameTitle(TreeItem<String> item) {
        if (item.getChildren().size() == 0) {
            renameSuggestionTask(item);
        }
        else {
            for (TreeItem<String> child : item.getChildren()) {
                getRenameTitle(child);
            }
        }
    }

    /**
     * Deep copy of TreeItem.
     *
     * @param item to deep copy
     * @return new object copy of item passed.
     */
    private <T> TreeItem<T> deepCopyTreeItem(TreeItem<T> item) {
        TreeItem<T> copy = new TreeItem<>(item.getValue());

        for (TreeItem<T> child : item.getChildren()) {
            copy.getChildren().add(deepCopyTreeItem(child));
        }

        return copy;
    }

    /**
     * Rename suggestion task for threaded api calls.
     *
     * @param title of show
     */
    private void renameSuggestionTask(TreeItem<String> item) {
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return getRenameSuggestion(item.getValue());
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            Platform.runLater(() -> {
                item.setValue(task.getValue());
            });
        });

        task.setOnFailed(workerStateEvent -> {
            Platform.runLater(() -> {
                item.setValue(RenameController.ERROR_MESSAGE);
            });
        });

        new Thread(task).start();
    }

    /**
     * Gets rename suggestion.
     *
     * @param fileName of the file
     * @return the rename suggestion
     * @throws IOException the io exception
     */
    private String getRenameSuggestion(String fileName) throws IOException {
        String newName = RenameController.ERROR_MESSAGE;

        // create initial show object from file name
        Show show = createShow(fileName);

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

        if (newName.equals(RenameController.ERROR_MESSAGE))
            return newName;
        else
            return filterForIlegalChars(newName);
    }

    /**
     * Function to filter any chars not allowed on file system.
     * <p>
     * e.g. TODO write example here
     * </p>
     *
     * @param name name to filter
     * @return filtered string of name
     */
    private String filterForIlegalChars(final String name) {
        // TODO implement this to replace chars not allowed by windows here.
        return name;
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
     * Recursive function to rename a file passed, or if directory call function again on each file.
     *
     * @param f file to be renamed
     */
    private void renameFile(File f) throws IOException {
        if (f.isFile()) {
            for (int i = 0; i < listRenameFrom.size(); i++) {
                // check if current file name contains name from listRenameFrom
                if (f.getCanonicalPath().contains(listRenameFrom.get(i))) {
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
            for (File temp : Objects.requireNonNull(f.listFiles())) {
                renameFile(temp);
            }
        }
    }

    /**
     * Function to rename all files with api updated info.
     */
    @FXML
    public void renameAll() throws IOException {
        if (listRenameFrom.size() == listRenameTo.size() && listRenameFrom.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                if (!listRenameTo.get(i).equals(RenameController.ERROR_MESSAGE)) {
                    renameFile(files.get(i));
                }
            }
        }
    }

    /**
     * Function to remove selected item from listview
     *
     * @param list List for an item to be removed from
     * @param <T>  Type to of object in list
     */
    private <T> void removeItem(ListView<T> list) {
        if (list.getItems().size() > 0) {
            int index = list.getSelectionModel().getSelectedIndex();

            if (listRenameFrom.size() == listRenameTo.size()) {
                listRenameTo.remove(index);
            }

            listRenameFrom.remove(index);
            files.remove(index);
        }
    }

    @FXML
    public void removeSelected() {
        removeItem(listViewRenameFrom);
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
        //checkboxSubFolder.setFocusTraversable(false);

        // create tree map to map files to old and new name
        fileMapToNames = new TreeMap<>();

        initListenerObservableArray();

        initTree(treeViewRenameFrom);
        initTree(treeViewRenameTo);
    }

    private void initTree(TreeView<String> treeView) {
        TreeItem<String> item = new TreeItem<>("Root");
        treeView.setRoot(item);
        treeView.setShowRoot(false);

//        treeView.setCellFactory(param -> new TreeCell<File>() {
//            @Override
//            public void updateItem(File item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty) {
//                    setText("");
//                    setGraphic(null);
//                }
//                else {
//                    setText(item.getName());
//                }
//            }
//        });
    }

//    /**
//     * Function to set the styling of the ListView to wrap the text if it's too long
//     */
//    private void setListViewsWrapText(ListView<String> listView) {
//        listView.setCellFactory(param -> new ListCell<String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty || item == null) {
//                    setGraphic(null);
//                    setText(null);
//                }
//                else {
//                    // set the width's
//                    setMinWidth(param.getWidth() - 40);
//                    setMaxWidth(param.getWidth() - 40);
//                    setPrefWidth(param.getWidth() - 40);
//
//                    // allow wrapping
//                    setWrapText(true);
//
//                    setText(item);
//                }
//            }
//        });
//    }
}
