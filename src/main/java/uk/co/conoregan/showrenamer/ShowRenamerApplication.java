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

package uk.co.conoregan.showrenamer;

import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The starting point of the ShowRenamer application.
 */
public class ShowRenamerApplication extends Application {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowRenamerApplication.class);

    /**
     * The screen width.
     */
    private static final int WIDTH = 1280;

    /**
     * The screen height.
     */
    private static final int HEIGHT = 720;

    /**
     * Main application function. Starts the JavaFX Application.
     *
     * @param args the ags.
     */
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setTitle("Show Renamer");

        final FXMLLoader fxmlLoader = new FXMLLoader(ShowRenamerApplication.class.getResource("/view/rename.fxml"));
        final Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);

        final InputStream appIconStream = ShowRenamerApplication.class.getResourceAsStream("/images/show-renamer-icon.png");
        if (appIconStream != null) {
            final Image appIcon = new Image(appIconStream);
            primaryStage.getIcons().add(appIcon);
        }

        primaryStage.show();
        LOGGER.info("Show Renamer successfully started.");
    }
}
