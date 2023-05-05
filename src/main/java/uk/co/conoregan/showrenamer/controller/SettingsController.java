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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The JavaFX controller for the settings.fxml file.
 */
public class SettingsController extends NavigationController implements Initializable {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsController.class);

    private static final String SETTINGS_NAV_ACTIVE_CSS = "-fx-border-color: #3298e3;";

    @FXML
    private VBox vboxRenameFormat;

    @FXML
    private VBox vboxAbout;

    @FXML
    private Button buttonSettingsNavRenameFormat;

    @FXML
    private Button buttonSettingsNavAbout;

    private Button buttonActiveSettingsNav;

    @FXML
    private void navigateToRenamePage(final ActionEvent event) throws IOException {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        changeScene("rename", stage);
    }

    @FXML
    private void navigateToSettingsPage(final ActionEvent event) throws IOException {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        changeScene("settings", stage);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        buttonSettingsNavRenameFormat.setStyle(SETTINGS_NAV_ACTIVE_CSS);
        buttonActiveSettingsNav = buttonSettingsNavRenameFormat;
    }

    @FXML
    private void handleSettingsNavClick(final ActionEvent event) {
        buttonActiveSettingsNav.setStyle(null);

        buttonActiveSettingsNav = (Button) event.getSource();
        buttonActiveSettingsNav.setStyle(SETTINGS_NAV_ACTIVE_CSS);

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
}