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

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.conoregan.showrenamer.util.ShowInfoMatcher;
import uk.co.conoregan.showrenamer.util.ResultValidator;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class to control the renaming of files that are about shows (movie or tv)
 */
public class RenameController implements Initializable {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RenameController.class);

    /**
     * The error message displayed if no result is found from TMDB_API.
     */
    private static final String ERROR_MESSAGE = "<Unable to find match>";

    /**
     * The movie database api wrapper object.
     */
    private static final TmdbApi TMDB_API;

    static {
        // load properties config
        final String apiKeysPath = "properties/api_keys.properties";
        final URL res = RenameController.class.getClassLoader().getResource(apiKeysPath);
        if (res == null) {
            throw new UncheckedIOException(new FileNotFoundException(apiKeysPath));
        }

        final URI uri;
        try {
            uri = res.toURI();
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }

        final Properties properties = new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(uri))) {
            properties.load(is);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load resource", e);
        }

        // make tmdb api
        final String tmdbApiKeyPropertyName = "TMDB_API_KEY_V3";
        final String tmdbApiKey = properties.getProperty(tmdbApiKeyPropertyName);
        if (tmdbApiKey == null) {
            LOGGER.error(String.format("The property: '%s' was not found in the properties file: %s.",
                    tmdbApiKeyPropertyName, apiKeysPath));
            System.exit(0);
        }
        TMDB_API = new TmdbApi(tmdbApiKey);
    }

    private final ObservableList<String> listRenameFrom = FXCollections.observableArrayList();
    private final ObservableList<String> listRenameTo = FXCollections.observableArrayList();
    private List<File> files;

    @FXML
    private ListView<String> listViewRenameFrom;
    @FXML
    private ListView<String> listViewRenameTo;

    @FXML
    private CheckBox checkboxIncludeSubFolder;
    @FXML
    private CheckBox checkboxImproveFolderNames;

    /**
     * Event to handle a drag over event.
     *
     * @param event the event.
     */
    @FXML
    private void handleDragOverFileUpload(DragEvent event) {
        final Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            event.acceptTransferModes(TransferMode.LINK);
        }
        else {
            event.consume();
        }
    }

    /**
     * Event to handle files being dropped.
     *
     * @param event the event.
     */
    @FXML
    private void handleDragDroppedFileUpload(DragEvent event) {
        final Dragboard dragboard = event.getDragboard();
        final List<File> dragboardFiles = dragboard.getFiles();
        for (final File item : dragboardFiles) {
            addFile(item);
        }

        for (final File file : files) {
            listRenameFrom.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
            listRenameTo.add("");
        }
    }

    /**
     * Open file dialog to select files to be renamed.
     */
    @FXML
    private void openFileDialog() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException,
            IllegalAccessException {
        // set dialog MVC.style to windows
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        // open the dialog
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a directory");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        final int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File dir = chooser.getSelectedFile();
            final File[] temp = dir.listFiles();

            if (temp != null && temp.length > 0) {
                for (final File item : Objects.requireNonNull(dir.listFiles())) {
                    addFile(item);
                }
            }

            for (final File file : files) {
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
    private void getSuggestions() {
        for (int i = 0; i < listRenameFrom.size(); i++) {
            renameSuggestionTask(i);
        }
    }

    /**
     * Gets rename suggestion.
     *
     * @param index the index
     * @return the renamed suggestion
     */
    @Nonnull
    private String getRenameSuggestion(int index) {
        final String fileName = listRenameFrom.get(index);
        final String matchedTitle = ShowInfoMatcher.matchTitle(fileName);

        final List<Multi> results = TMDB_API.getSearch().searchMulti(matchedTitle, "en-US", 1).getResults();
        if (!ResultValidator.isGenericListValid(results)) {
            LOGGER.info(String.format("No result found for title: %s", matchedTitle));
            return RenameController.ERROR_MESSAGE;
        }

        final Multi result = results.get(0);
        final Multi.MediaType mediaType = result.getMediaType();

        if (mediaType == Multi.MediaType.MOVIE) {
            final MovieDb movie = (MovieDb) result;
            final String title = movie.getTitle();
            final String releaseDate = movie.getReleaseDate();

            if (ResultValidator.isStringVarargsValid(title, releaseDate)) {
                return String.format("%s (%s)", title, releaseDate);
            }
        }
        else if (mediaType == Multi.MediaType.TV_SERIES) {
            final Integer matchedSeason = ShowInfoMatcher.matchSeason(fileName);
            final List<Integer> matchedEpisodes = ShowInfoMatcher.matchEpisodes(fileName);
            if (!ResultValidator.isIntegerValid(matchedSeason) || !ResultValidator.isIntegerListValid(matchedEpisodes)) {
                LOGGER.info(String.format("No season or episode could be matched from the file name: %s", fileName));
                return RenameController.ERROR_MESSAGE;
            }

            final TvSeries tvSeries = (TvSeries) result;
            final TvEpisode episode = TMDB_API.getTvEpisodes().getEpisode(
                    tvSeries.getId(), matchedSeason, matchedEpisodes.get(0), "en-US");
            final String seriesName = tvSeries.getName();
            final String episodeName = episode.getName();
            final int seasonNumber = episode.getSeasonNumber();
            final int episodeNumber = episode.getEpisodeNumber();

            if (ResultValidator.isIntegerVarargsValid(seasonNumber, episodeNumber)
                    && ResultValidator.isStringVarargsValid(seriesName, episodeName)) {
                return String.format("%s - S%02dE%02d - %s", seriesName, seasonNumber, episodeNumber, episodeName);
            }
        }

        return RenameController.ERROR_MESSAGE;
    }

    /**
     * Rename suggestion task for threaded api calls.
     *
     * @param index the index
     */
    private void renameSuggestionTask(final int index) {
        final Task<String> task = new Task<>() {
            @Override
            protected String call() {
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
    private void addFile(@Nonnull final File item) {
        if (item.isFile()) {
            files.add(item);
        }
        else if (item.isDirectory() && checkboxIncludeSubFolder.isSelected()) {
            for (final File f : Objects.requireNonNull(item.listFiles())) {
                addFile(f);
            }
        }
    }

    /**
     * Recursive function to rename a file passed, or if directory call function again on each file.
     *
     * @param f file to be renamed
     */
    private void renameFile(@Nonnull final File f) throws IOException {
        if (f.isFile()) {
            for (int i = 0; i < listRenameFrom.size(); i++) {
                // check if current file name contains name from listRenameFrom
                if (f.getCanonicalPath().contains(listRenameFrom.get(i))) {
                    // get path
                    final String path = files.get(i).getCanonicalPath();

                    // get last occurrence of file to be renamed
                    final StringBuilder sb = new StringBuilder();
                    final int lastOccurrence = path.lastIndexOf(listRenameFrom.get(i));

                    // generate new path string with replaced file name
                    sb.append(path, 0, lastOccurrence);
                    sb.append(listRenameTo.get(i));
                    sb.append(path.substring(lastOccurrence + listRenameFrom.get(i).length()));

                    // rename
                    final Path p = Paths.get(path);
                    Files.move(p, p.resolveSibling(sb.toString()));
                }
            }
        }
        else if (f.isDirectory()) {
            // for each file in the directory, call the recursive function
            for (final File temp : Objects.requireNonNull(f.listFiles())) {
                renameFile(temp);
            }
        }
    }

    /**
     * Function to rename all files with api updated info.
     */
    @FXML
    public void saveAll() throws IOException {
        if (listRenameFrom.size() == listRenameTo.size() && listRenameFrom.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                if (!listRenameTo.get(i).equals(RenameController.ERROR_MESSAGE)) {
                    renameFile(files.get(i));
                }
            }
        }
    }

    @FXML
    public void removeSelected() {
        removeItem(listViewRenameFrom);
    }

    /**
     * Function to remove selected item from listview
     *
     * @param list List for an item to be removed from
     * @param <T>  Type to of object in list
     */
    private <T> void removeItem(@Nonnull final ListView<T> list) {
        if (list.getItems().size() > 0) {
            int index = list.getSelectionModel().getSelectedIndex();

            if (listRenameFrom.size() == listRenameTo.size()) {
                listRenameTo.remove(index);
            }

            listRenameFrom.remove(index);
            files.remove(index);
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
        checkboxIncludeSubFolder.setFocusTraversable(false);
        checkboxImproveFolderNames.setFocusTraversable(false);

        files = new ArrayList<>();

        setListViewCellFactorySettings(listViewRenameFrom);
        setListViewCellFactorySettings(listViewRenameTo);

        listViewRenameFrom.setFocusTraversable(false);
        listViewRenameTo.setFocusTraversable(false);

        listViewRenameFrom.setItems(listRenameFrom);
        listViewRenameTo.setItems(listRenameTo);
    }

    /**
     * Sets the list view cell factory settings.
     *
     * @param listView the listview.
     */
    private void setListViewCellFactorySettings(@Nonnull final ListView<String> listView) {
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // prevent horizontal scroll bar
                    setMinWidth(param.getWidth() - 20);
                    setMaxWidth(param.getWidth() - 20);
                    setPrefWidth(param.getWidth() - 20);
                    setText(item);
                }
            }
        });
    }
}
