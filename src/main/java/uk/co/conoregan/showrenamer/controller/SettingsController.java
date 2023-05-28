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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.conoregan.showrenamer.config.preference.PreferenceService;
import uk.co.conoregan.showrenamer.config.preference.ShowRenamerPreference;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The JavaFX controller for the settings.fxml file.
 */
public class SettingsController extends NavigationController implements Initializable {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsController.class);

    /**
     * The preference service.
     */
    private static final PreferenceService PREFERENCE_SERVICE = new PreferenceService();

    /**
     * The class to mark the card as active.
     */
    private static final String CSS_CLASS_CARD_ACTIVE = "card-active";

    /**
     * VBox node containing the settings navigation button for rename format.
     */
    @FXML
    private VBox vboxRenameFormat;

    /**
     * VBox node containing the settings navigation button for allowed files types.
     */
    @FXML
    private VBox vboxAllowedFileTypes;

    /**
     * VBox node containing the settings navigation button for about.
     */
    @FXML
    private VBox vboxAbout;

    /**
     * Button for settings navigation to change to rename format section.
     */
    @FXML
    private Button buttonSettingsNavRenameFormat;

    /**
     * Button for settings navigation to change to allowed files types section.
     */
    @FXML
    private Button buttonSettingsNavFileTypeFilter;

    /**
     * Button for settings navigation to change to about section.
     */
    @FXML
    private Button buttonSettingsNavAbout;

    /**
     * The settings navigation button currently active.
     */
    private Button buttonActiveSettingsNav;

    /**
     * Text field for movie rename format
     */
    @FXML
    private TextField textFieldMovieRenameFormat;

    /**
     * Text field for tv show rename format.
     */
    @FXML
    private TextField textFieldTvShowRenameFormat;

    /**
     * Text field for tv show episode rename format.
     */
    @FXML
    private TextField textFieldTvShowEpisodeRenameFormat;

    /**
     * Text field for adding allowed file types.
     */
    @FXML
    private TextField textFieldAddAllowedFileType;

    /**
     * HBox containing allowed file types.
     */
    @FXML
    private HBox hboxAllowedFileTypes;

    /**
     * Navigate to rename page.
     *
     * @param event the button event.
     * @throws IOException if fxml file not found.
     */
    @FXML
    private void navigateToRenamePage(final ActionEvent event) throws IOException {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        changeScene(View.RENAME, stage);
    }

    /**
     * Handles the event when a settings navigation button is clicked.
     *
     * @param event the button event.
     */
    @FXML
    private void handleSettingsNavClick(final ActionEvent event) {
        buttonActiveSettingsNav.getStyleClass().remove(CSS_CLASS_CARD_ACTIVE);

        buttonActiveSettingsNav = (Button) event.getSource();
        buttonActiveSettingsNav.getStyleClass().add(CSS_CLASS_CARD_ACTIVE);

        vboxRenameFormat.setVisible(false);
        vboxAllowedFileTypes.setVisible(false);
        vboxAbout.setVisible(false);

        if (buttonActiveSettingsNav.equals(buttonSettingsNavRenameFormat)) {
            vboxRenameFormat.toFront();
            vboxRenameFormat.setVisible(true);
        } else if (buttonActiveSettingsNav.equals(buttonSettingsNavFileTypeFilter)) {
            vboxAllowedFileTypes.toFront();
            vboxAllowedFileTypes.setVisible(true);
        } else if (buttonActiveSettingsNav.equals(buttonSettingsNavAbout)) {
            vboxAbout.toFront();
            vboxAbout.setVisible(true);
        }
    }

    /**
     * Resets the movie rename format to default.
     */
    @FXML
    private void resetMovieRenameFormat() {
        resetFormat(ShowRenamerPreference.RENAME_FORMAT_MOVIE, textFieldMovieRenameFormat);
    }

    /**
     * Resets the tv show rename format to default.
     */
    @FXML
    private void resetTvShowRenameFormat() {
        resetFormat(ShowRenamerPreference.RENAME_FORMAT_TV_SHOW, textFieldTvShowRenameFormat);
    }

    /**
     * Resets the tv show episode rename format to default.
     */
    @FXML
    private void resetTvShowEpisodeRenameFormat() {
        resetFormat(ShowRenamerPreference.RENAME_FORMAT_TV_SHOW_EPISODE, textFieldTvShowEpisodeRenameFormat);
    }

    /**
     * Resets rename format.
     *
     * @param renameFormat the rename format to reset.
     * @param textField the text field to change back to default.
     */
    private void resetFormat(@Nonnull final ShowRenamerPreference renameFormat, @Nonnull final TextField textField) {
        textField.setText(renameFormat.getDefaultValue());
    }

    /**
     * Saves all rename formats.
     */
    @FXML
    private void saveRenameFormats() {
        PREFERENCE_SERVICE.setPreference(ShowRenamerPreference.RENAME_FORMAT_MOVIE, textFieldMovieRenameFormat.getText());
        PREFERENCE_SERVICE.setPreference(ShowRenamerPreference.RENAME_FORMAT_TV_SHOW, textFieldTvShowRenameFormat.getText());
        PREFERENCE_SERVICE.setPreference(ShowRenamerPreference.RENAME_FORMAT_TV_SHOW_EPISODE, textFieldTvShowEpisodeRenameFormat.getText());
    }

    /**
     * Adds the file type to the allowed files types.
     */
    @FXML
    private void addAllowedFileType() {
        final String fileType = textFieldAddAllowedFileType.getText().trim().strip().toLowerCase();

        if (fileType.isEmpty()) {
            LOGGER.info(String.format("Cannot add file type: '%s', it is empty.", fileType));
            return;
        }

        addAllowedFileType(fileType);
        textFieldAddAllowedFileType.setText("");
        Platform.runLater(() -> textFieldAddAllowedFileType.requestFocus());
    }

    /**
     * Saves the allowed file types.
     */
    @FXML
    private void saveAllowedFileTypes() {
        final Set<String> allowedFileTypes = new HashSet<>();

        for (final Node node : hboxAllowedFileTypes.getChildren()) {
            final Label label = ((Label) node);
            allowedFileTypes.add(label.getText());
        }

        PREFERENCE_SERVICE.setPreference(ShowRenamerPreference.ALLOWED_FILE_TYPES, String.join(",", allowedFileTypes));
    }

    /**
     * @inheritDoc
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        buttonActiveSettingsNav = buttonSettingsNavRenameFormat;

        textFieldMovieRenameFormat.setText(PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.RENAME_FORMAT_MOVIE));
        textFieldTvShowRenameFormat.setText(PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.RENAME_FORMAT_TV_SHOW));
        textFieldTvShowEpisodeRenameFormat.setText(PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.RENAME_FORMAT_TV_SHOW_EPISODE));

        final List<String> allowedFileTypes =
                Stream.of(PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.ALLOWED_FILE_TYPES).split(",")).sorted().toList();
        for (final String type : allowedFileTypes) {
            addAllowedFileType(type);
        }
    }

    /**
     * Add file type to allowed.
     *
     * @param type the file type.
     */
    private void addAllowedFileType(@Nonnull final String type) {
        final Set<String> existingTypes = hboxAllowedFileTypes.getChildren().stream().map(node ->
                ((Label) node).getText()).collect(Collectors.toSet());

        if (existingTypes.contains(type)) {
            LOGGER.info(String.format("The file type: '%s' is already present.", type));
            return;
        }

        final Button closeButton = new Button("x");
        closeButton.setAlignment(Pos.TOP_CENTER);
        closeButton.getStyleClass().add("button-close");

        final Label label = new Label(type);
        label.setGraphic(closeButton);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setContentDisplay(ContentDisplay.RIGHT);
        label.getStyleClass().add("file-type");

        closeButton.setOnAction(event -> hboxAllowedFileTypes.getChildren().remove(label));

        hboxAllowedFileTypes.getChildren().add(label);
    }
}
