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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.co.conoregan.showrenamer.config.preference.PreferenceService;
import uk.co.conoregan.showrenamer.config.preference.ShowRenamerPreference;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The JavaFX controller for the settings.fxml file.
 */
public class SettingsController extends NavigationController implements Initializable {
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
     * Navigate to rename page.
     *
     * @param event the button event.
     * @throws IOException if fxml file not found.
     */
    @FXML
    private void navigateToRenamePage(final ActionEvent event) throws IOException {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        changeScene("rename", stage);
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

        if (buttonActiveSettingsNav.equals(buttonSettingsNavRenameFormat)) {
            vboxRenameFormat.toFront();
            vboxRenameFormat.setVisible(true);
            vboxAbout.setVisible(false);
        } else if (buttonActiveSettingsNav.equals(buttonSettingsNavAbout)) {
            vboxAbout.toFront();
            vboxAbout.setVisible(true);
            vboxRenameFormat.setVisible(false);
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
    }

    /**
     * @inheritDoc
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        buttonActiveSettingsNav = buttonSettingsNavRenameFormat;

        textFieldMovieRenameFormat.setText(PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.RENAME_FORMAT_MOVIE));
        textFieldTvShowRenameFormat.setText(PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.RENAME_FORMAT_TV_SHOW));
    }
}
