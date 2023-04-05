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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final String viewPath = "view/rename.fxml";
        final URL startView = getClass().getClassLoader().getResource(viewPath);

        if (startView == null) {
            LOGGER.error(String.format("The resource: %s was not found.", viewPath));
            System.exit(0);
        }

        final Parent root = FXMLLoader.load(startView);
        final Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setTitle("Show Renamer");
        primaryStage.setScene(scene);
        //  primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("images/icon.png")));

        primaryStage.show();
        LOGGER.info("Show Renamer successfully started.");
    }
}


