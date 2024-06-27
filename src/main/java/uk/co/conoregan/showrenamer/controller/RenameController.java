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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.conoregan.showrenamer.api.TMDBResultProvider;
import uk.co.conoregan.showrenamer.config.preference.PreferenceService;
import uk.co.conoregan.showrenamer.config.preference.ShowRenamerPreference;
import uk.co.conoregan.showrenamer.config.property.PropertyService;
import uk.co.conoregan.showrenamer.config.property.ShowRenamerProperty;
import uk.co.conoregan.showrenamer.suggestion.FileSuggestionProvider;

/**
 * The JavaFX controller for the rename.fxml file.
 */
public class RenameController extends NavigationController implements Initializable {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RenameController.class);

    /**
     * The Properties service.
     */
    private static final PropertyService PROPERTY_SERVICE = new PropertyService();

    /**
     * The preference service.
     */
    private static final PreferenceService PREFERENCE_SERVICE = new PreferenceService();

    /**
     * The directory chooser.
     */
    private static final DirectoryChooser DIRECTORY_CHOOSER = new DirectoryChooser();

    /**
     * File mapping. Current name --> Suggested name.
     */
    private final TreeMap<File, File> fileRenameMapping = new TreeMap<>(Comparator.comparing(File::getName));

    /**
     * The file renamer.
     */
    private FileSuggestionProvider fileSuggestionProvider;

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
     * Checkbox to filter file types.
     */
    @FXML
    private CheckBox checkboxFilterFileTypes;

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
     * Navigate to settings page.
     *
     * @param event the button event.
     * @throws IOException if fxml file not found.
     */
    @FXML
    private void navigateToSettingsPage(final ActionEvent event) throws IOException {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        changeScene(View.SETTINGS, stage);
    }

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
        }
        else {
            event.consume();
        }

        updateUserInterface();
    }

    /**
     * Event to handle files being dropped.
     *
     * @param event the event.
     */
    @FXML
    private void handleDragDroppedFileUpload(final DragEvent event) {
        final List<File> dragboardFiles = event.getDragboard().getFiles();

        for (final File item : dragboardFiles) {
            addFile(item);
        }

        updateUserInterface();
    }

    /**
     * Open file dialog to select files to be renamed.
     */
    @FXML
    private void openFileDialog(final ActionEvent event) {
        final Window window = ((Button) event.getSource()).getScene().getWindow();
        final File dir = DIRECTORY_CHOOSER.showDialog(window);

        if (dir != null) {
            addFile(dir);
        }

        updateUserInterface();
    }

    /**
     * Gets rename suggestions for files on left list.
     */
    @FXML
    private void getSuggestions() {
        updateButtonsUserInterface();

        CompletableFuture.allOf(
                fileRenameMapping.keySet()
                        .stream()
                        .map(file -> CompletableFuture.supplyAsync(() -> fileSuggestionProvider.getImprovedName(file))
                                .thenApply(suggestion -> suggestion.orElse(null))
                                .thenAccept(suggestion -> fileRenameMapping.replace(file, suggestion))
                        ).toArray(CompletableFuture[]::new)
        ).thenAccept(unused -> updateUserInterface());
    }

    /**
     * Clears all the items from the fileRenameMapping, removing the items from the list views as a result.
     */
    @FXML
    private void clearAll() {
        fileRenameMapping.clear();

        updateUserInterface();
    }

    /**
     * Function to rename all files with suggested replacements.
     */
    @FXML
    public void saveAll() {
        fileRenameMapping
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .sorted((entry1, entry2) -> Integer.compare(getDepth(entry2.getKey()), getDepth(entry1.getKey())))
                .forEach(entry -> {
                    final File sourceFile = entry.getKey();
                    final File destinationFile = entry.getValue();

                    final String originalName = sourceFile.getName();
                    final String newName = destinationFile.getName();

                    if (destinationFile.exists()) {
                        LOGGER.warn("Cannot rename: {}, the file already exists.", sourceFile.getName());
                        return;
                    }

                    final boolean successfulRename = sourceFile.renameTo(destinationFile);
                    if (successfulRename) {
                        LOGGER.info("Successfully renamed: {} --> {}", originalName, newName);
                    }
                    else {
                        LOGGER.error("Cannot rename: {}, an unexpected error occurred.", sourceFile.getName());
                    }
                });

        clearAll();
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        fileSuggestionProvider = new FileSuggestionProvider(
                new TMDBResultProvider(PROPERTY_SERVICE.getProperty(ShowRenamerProperty.TMDB_API_KEY)));

        // stops checkbox box resizing when clicking on and off other controls
        checkboxIncludeSubFolder.setFocusTraversable(false);
        checkboxFilterFileTypes.setFocusTraversable(false);

        // save selection value to preferences
        checkboxIncludeSubFolder.selectedProperty().addListener((observable, oldValue, newValue) ->
                PREFERENCE_SERVICE.setPreference(ShowRenamerPreference.CHECKBOX_CHECKED_INCLUDE_SUB_FOLDERS, String.valueOf(newValue)));
        checkboxFilterFileTypes.selectedProperty().addListener((observable, oldValue, newValue) ->
                PREFERENCE_SERVICE.setPreference(ShowRenamerPreference.CHECKBOX_CHECKED_FILTER_FILE_TYPES, String.valueOf(newValue)));

        // set checkboxes to preference values
        checkboxIncludeSubFolder.selectedProperty().set(
                Boolean.parseBoolean(PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.CHECKBOX_CHECKED_INCLUDE_SUB_FOLDERS)));
        checkboxFilterFileTypes.selectedProperty().set(
                Boolean.parseBoolean(PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.CHECKBOX_CHECKED_FILTER_FILE_TYPES)));

        setListViewCellFactorySettings(listViewCurrentTitles);
        setListViewCellFactorySettings(listViewSuggestedTitles);

        listViewCurrentTitles.setFocusTraversable(false);
        listViewSuggestedTitles.setFocusTraversable(false);
    }

    /**
     * Update all nodes present on the UI depending on the state of the application.
     */
    private void updateUserInterface() {
        updateButtonsUserInterface();
        updateListViewsUserInterface();
        updateVboxUserInterface();
    }

    /**
     * Update the buttons depending on the state of the application.
     */
    private void updateButtonsUserInterface() {
        Platform.runLater(() -> {
            buttonGetSuggestions.setDisable(
                    fileRenameMapping.keySet().isEmpty() || !fileRenameMapping.values().stream().allMatch(Objects::isNull));
            buttonSaveAll.setDisable(
                    fileRenameMapping.values().isEmpty() || fileRenameMapping.values().stream().allMatch(Objects::isNull));
        });
    }

    /**
     * Update the list views depending on the state of the application.
     */
    private void updateListViewsUserInterface() {
        Platform.runLater(() -> {
            listViewCurrentTitles.getItems().setAll(fileRenameMapping.keySet());
            listViewSuggestedTitles.getItems().setAll(fileRenameMapping.values());
        });
    }

    /**
     * Update the vbox depending on the state of the application.
     */
    private void updateVboxUserInterface() {
        Platform.runLater(() -> {
            vboxCurrentTitles.setDisable(fileRenameMapping.keySet().isEmpty());
            vboxSuggestedTitles.setDisable(
                    fileRenameMapping.values().isEmpty() || fileRenameMapping.values().stream().allMatch(Objects::isNull));
        });
    }

    /**
     * Sets the list view cell factory settings.
     *
     * @param listView the listview.
     */
    private void setListViewCellFactorySettings(final ListView<File> listView) {
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                }
                else {
                    // prevent horizontal scroll bar
                    setMinWidth(param.getWidth() - 20);
                    setMaxWidth(param.getWidth() - 20);
                    setPrefWidth(param.getWidth() - 20);

                    if (item.isDirectory()) {
                        setText(item.getName());
                    }
                    else {
                        setText(FilenameUtils.removeExtension(item.getName()));
                    }
                }
            }
        });
    }

    /**
     * Recursive function to add files and sub folders.
     *
     * @param file file from selected folder in open file dialog
     */
    private void addFile(final File file) {
        if (allowedToAddFile(file)) {
            fileRenameMapping.put(file, null);
        }

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
     * Determines whether a file is allowed to be added.
     *
     * @param file the file.
     * @return true if allowed, false if not.
     */
    private boolean allowedToAddFile(final File file) {
        if (file.isDirectory()) {
            return true;
        }

        if (!checkboxFilterFileTypes.isSelected()) {
            return true;
        }
        else {
            final List<String> allowedTypes =
                    List.of(PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.ALLOWED_FILE_TYPES).split(","));
            return allowedTypes.contains(FilenameUtils.getExtension(file.getName()));
        }
    }

    /**
     * Gets the depth of a file.
     *
     * @param file the file.
     * @return the depth.
     */
    private int getDepth(final File file) {
        final String fileSeparator = String.valueOf(File.separatorChar);
        return file.getAbsolutePath().split(Pattern.quote(fileSeparator)).length;
    }
}
