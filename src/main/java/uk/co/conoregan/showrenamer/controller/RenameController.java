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
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
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
     * The movie database suggestion provider.
     */
    private final ShowSuggestionProvider showSuggestionProvider = new TMDBSuggestionProvider();

    /**
     * The directory chooser.
     */
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    /**
     * File mapping. Current name --> Suggested name.
     */
    private final ObservableMap<File, File> fileRenameMapping = FXCollections.observableHashMap();

    @FXML
    private ListView<File> listViewRenameFrom;
    @FXML
    private ListView<File> listViewRenameTo;

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
        for (Map.Entry<File, File> entry : fileRenameMapping.entrySet()) {
            renameSuggestionTask(entry);
        }
    }

    /**
     * Function to rename all files with api updated info.
     */
    @FXML
    public void saveAll() {
        for (Map.Entry<File, File> entry : fileRenameMapping.entrySet()) {
            if (entry.getValue() == null) {
                LOGGER.info(String.format("Cannot rename: %s, no suggestion found.", entry.getKey().getName()));
                continue;
            }

            if (entry.getValue().exists()) {
                LOGGER.warn(String.format("Cannot rename: %s, %s already exists.", entry.getKey().getName(), entry.getValue().getName()));
                continue;
            }

            entry.getKey().renameTo(entry.getValue());
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

        fileRenameMapping.addListener((MapChangeListener<File, File>) change -> {
            listViewRenameFrom.getItems().removeAll(change.getKey());
            if (change.wasAdded()) {
                listViewRenameFrom.getItems().add(change.getKey());
            }

            listViewRenameTo.getItems().removeAll(change.getValueRemoved());
            if (change.wasAdded()) {
                listViewRenameTo.getItems().add(change.getValueAdded());
            }
        });
    }

    /**
     * Sets the list view cell factory settings.
     *
     * @param listView the listview.
     */
    private void setListViewCellFactorySettings(@Nonnull final ListView<File> listView) {
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // prevent horizontal scroll bar
                    setMinWidth(param.getWidth() - 20);
                    setMaxWidth(param.getWidth() - 20);
                    setPrefWidth(param.getWidth() - 20);
                    setText(getFileNameWithoutExtension(item.getName()));
                }
            }
        });
    }

    /**
     * Rename suggestion task for threaded api calls.
     *
     * @param entry a fileRenameMapping entry.
     */
    private void renameSuggestionTask(@Nonnull final Map.Entry<File, File> entry) {
        final Task<File> task = new Task<>() {
            @Override
            protected File call() {
                final String fileNameWithoutExtension = getFileNameWithoutExtension(entry.getKey().getName());
                final Optional<String> newFileNameWithoutExtension = showSuggestionProvider.getSuggestion(fileNameWithoutExtension);

                if (newFileNameWithoutExtension.isPresent()) {
                    return new File(entry.getKey().getAbsolutePath().replace(fileNameWithoutExtension, newFileNameWithoutExtension.get()));
                }

                return null;
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            Platform.runLater(() -> {
                entry.setValue(task.getValue());
            });
        });

        task.setOnFailed(workerStateEvent -> {
            Platform.runLater(() -> {
                entry.setValue(null);
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
        if (file.isFile()) {
            fileRenameMapping.put(file, null);
        }
        else if (file.isDirectory() && checkboxIncludeSubFolder.isSelected()) {
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
