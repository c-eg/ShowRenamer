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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.conoregan.showrenamer.suggestion.ShowSuggestionProvider;
import uk.co.conoregan.showrenamer.suggestion.TMDBSuggestionProvider;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Class to control the renaming of files that are about shows (movie or tv).
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
     * The movie database suggestion provider.
     */
    private final ShowSuggestionProvider showSuggestionProvider = new TMDBSuggestionProvider();

    /**
     * The directory chooser.
     */
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    /**
     * File mapping. File --> Suggested name
     */
    private final Map<File, String> fileRenameMapping = new HashMap<>();

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
        } else {
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
    }

    /**
     * Open file dialog to select files to be renamed.
     */
    @FXML
    private void openFileDialog() {
        final Window window = checkboxIncludeSubFolder.getScene().getWindow();
        final File dir = directoryChooser.showDialog(window);
        addFile(dir);
    }

    /**
     * Function is called by user from GUI. Gets rename suggestions for files
     * on left list.
     */
    @FXML
    private void getSuggestions() {
        for (Map.Entry<File, String> entry : fileRenameMapping.entrySet()) {
            renameSuggestionTask(entry);
        }
    }

    /**
     * Gets rename suggestion.
     *
     * @param entry a fileRenameMapping entry.
     * @return the renamed suggestion
     */
    @Nonnull
    private String getRenameSuggestion(@Nonnull final Map.Entry<File, String> entry) {
        final Optional<String> suggestedName = showSuggestionProvider.getSuggestion(entry.getKey().getName());
        return suggestedName.orElse(RenameController.ERROR_MESSAGE);
    }

    /**
     * Function to rename all files with api updated info.
     */
    @FXML
    public void saveAll() {
        for (Map.Entry<File, String> entry : fileRenameMapping.entrySet()) {
            final File file = entry.getKey();
            final String fileNameWithoutExtension = getFileNameWithoutExtension(file.getName());
            final String newFileName = entry.getValue();

            if (newFileName.equals(ERROR_MESSAGE)) {
                continue;
            }

            final File newName = new File(file.getAbsolutePath().replace(fileNameWithoutExtension, newFileName));
            file.renameTo(newName);
        }
    }

//    @FXML
//    public void removeSelected() {
//        removeItem(listViewRenameFrom);
//    }

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

        setListViewCellFactorySettings(listViewRenameFrom);
        setListViewCellFactorySettings(listViewRenameTo);

        listViewRenameFrom.setFocusTraversable(false);
        listViewRenameTo.setFocusTraversable(false);
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

    /**
     * Rename suggestion task for threaded api calls.
     *
     * @param entry a fileRenameMapping entry.
     */
    private void renameSuggestionTask(@Nonnull final Map.Entry<File, String> entry) {
        final Task<String> task = new Task<>() {
            @Override
            protected String call() {
                return getRenameSuggestion(entry);
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            Platform.runLater(() -> {
                entry.setValue(task.getValue());
            });
        });

        task.setOnFailed(workerStateEvent -> {
            Platform.runLater(() -> {
                entry.setValue(RenameController.ERROR_MESSAGE);
            });
        });

        new Thread(task).start();
    }

    /**
     * Recursive function to add files and sub folders.
     *
     * @param file file from selected folder in open file dialog
     */
    private void addFile(@Nonnull final File file) {
        fileRenameMapping.put(file, null);

        if (file.isDirectory() && checkboxIncludeSubFolder.isSelected()) {
            final File[] dirFiles = file.listFiles();

            if (dirFiles == null) {
                return;
            }

            for (final File f : dirFiles) {
                addFile(f);
            }
        }
    }

    /**
     * Returns the filename without an extension. If no extension, return original file name.
     *
     * @param fileName the file name.
     * @return filename without extension.
     */
    @Nonnull
    private String getFileNameWithoutExtension(@Nonnull final String fileName) {
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }
}
