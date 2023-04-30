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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.conoregan.showrenamer.suggestion.ShowSuggestionProvider;
import uk.co.conoregan.showrenamer.suggestion.TMDBSuggestionProvider;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * The JavaFX controller for the rename.fxml file.
 */
public class RenameController implements Initializable {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RenameController.class);

    /**
     * The movie database suggestion provider.
     */
    private ShowSuggestionProvider showSuggestionProvider;

    /**
     * The directory chooser.
     */
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    /**
     * File mapping. Current name --> Suggested name.
     */
    private final ObservableMap<File, File> fileRenameMapping = FXCollections.observableHashMap();

    /**
     * Whether to enable the current titles section.
     */
    private boolean enableSectionCurrentTitles = false;

    /**
     * Whether to enable the suggested titles section.
     */
    private boolean enableSectionSuggestedTitles = false;

    /**
     * List view to show original file names.
     */
    @FXML
    private ListView<File> listViewCurrentTitles;

    /**
     * List view to suggested file names.
     */
    @FXML
    private ListView<File> listViewSuggestedTitles;

    /**
     * Checkbox to include sub-folders.
     */
    @FXML
    private CheckBox checkboxIncludeSubFolder;

    /**
     * Checkbox to improve folder names.
     */
    @FXML
    private CheckBox checkboxImproveFolderNames;

    /**
     * Button to get suggested file names.
     */
    @FXML
    private Button buttonGetSuggestions;

    /**
     * Button to save suggested file names.
     */
    @FXML
    private Button buttonSaveAll;

    /**
     * VBox parent node for current titles section.
     */
    @FXML
    private VBox vboxCurrentTitles;

    /**
     * VBox parent node for suggested titles section.
     */
    @FXML
    private VBox vboxSuggestedTitles;

    /**
     * Event to handle a files drag over event.
     *
     * @param event the event.
     */
    @FXML
    private void handleDragOverFileUpload(final DragEvent event) {
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
    private void handleDragDroppedFileUpload(final DragEvent event) {
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

        if (dir != null) {
            addFile(dir);
        }
    }

    /**
     * Gets rename suggestions for files on left list.
     */
    @FXML
    private void getSuggestions() {
        for (Map.Entry<File, File> entry : fileRenameMapping.entrySet()) {
            final String fileNameWithoutExtension = getFileNameWithoutExtension(entry.getKey().getName());

            /*
            1. Get suggestion from movie database api
            2. Map suggestion to string, or null if no suggestion is present
            3. Update hashmap with suggestion (must use Platform.runLater to update UI for JavaFX
             */
            CompletableFuture.supplyAsync(() -> showSuggestionProvider.getSuggestion(fileNameWithoutExtension))
                    .thenApply(suggestion ->
                            suggestion.map(s ->
                                    new File(entry.getKey().getAbsolutePath().replace(fileNameWithoutExtension, s))).orElse(null))
                    .thenAccept(suggestion -> Platform.runLater(() -> entry.setValue(suggestion)));
        }

        buttonGetSuggestions.setDisable(true);
    }

    /**
     * Clears all the items from the fileRenameMapping, removing the items from the list views as a result.
     */
    @FXML
    private void clearAll() {
        fileRenameMapping.clear();

        buttonGetSuggestions.setDisable(false);
        buttonSaveAll.setDisable(false);
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

            final boolean successfulRename = entry.getKey().renameTo(entry.getValue());
            if (!successfulRename) {
                LOGGER.error(String.format("Cannot rename: %s, an unexpected error occurred.", entry.getKey().getName()));
            }
        }

        buttonSaveAll.setDisable(true);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeConstructor();

        // stops checkbox box resizing when clicking on and off other controls
        checkboxIncludeSubFolder.setFocusTraversable(false);
        checkboxImproveFolderNames.setFocusTraversable(false);

        setListViewCellFactorySettings(listViewCurrentTitles);
        setListViewCellFactorySettings(listViewSuggestedTitles);

        listViewCurrentTitles.setFocusTraversable(false);
        listViewSuggestedTitles.setFocusTraversable(false);

        // update list views based on fileRenameMapping.
        fileRenameMapping.addListener((MapChangeListener<File, File>) change -> {
            enableSectionCurrentTitles = !fileRenameMapping.keySet().isEmpty();
            enableSectionSuggestedTitles = !fileRenameMapping.values().isEmpty() && !fileRenameMapping.values().stream().allMatch(Objects::isNull);

            vboxCurrentTitles.setDisable(!enableSectionCurrentTitles);
            vboxSuggestedTitles.setDisable(!enableSectionSuggestedTitles);

            listViewCurrentTitles.getItems().removeAll(change.getKey());
            if (change.wasAdded()) {
                listViewCurrentTitles.getItems().add(change.getKey());
            }

            listViewSuggestedTitles.getItems().removeAll(change.getValueRemoved());
            if (change.wasAdded()) {
                listViewSuggestedTitles.getItems().add(change.getValueAdded());
            }
        });
    }

    /**
     * This function is treated as constructor for non-javafx related things.
     */
    private void initializeConstructor() {
        // load properties config
        final String apiKeysPath = "/properties/api_keys.properties";
        final InputStream res = TMDBSuggestionProvider.class.getResourceAsStream(apiKeysPath);

        final Properties properties = new Properties();
        try {
            properties.load(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // make tmdb api
        final String tmdbApiKeyPropertyName = "TMDB_API_KEY_V3";
        final String tmdbApiKey = properties.getProperty(tmdbApiKeyPropertyName);
        if (tmdbApiKey == null) {
            LOGGER.error(String.format("The property: '%s' was not found in the properties file: %s.",
                    tmdbApiKeyPropertyName, apiKeysPath));
            System.exit(0);
        }

        showSuggestionProvider = new TMDBSuggestionProvider(tmdbApiKey);
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
     * Recursive function to add files and sub folders.
     *
     * @param file file from selected folder in open file dialog
     */
    private void addFile(@Nonnull final File file) {
        if (file.isFile()) {
            fileRenameMapping.put(file, null);
        } else if (file.isDirectory() && checkboxIncludeSubFolder.isSelected()) {
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
