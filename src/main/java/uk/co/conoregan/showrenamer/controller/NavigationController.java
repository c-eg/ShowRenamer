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

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;

/**
 * The navigation controller, to be extended by JavaFX controllers to navigate to other scenes (pages).
 */
public class NavigationController {
    /**
     * The path to the .fxml files.
     */
    private static final String VIEW_PATH = "/view/";

    /**
     * The scene mapping cache.
     */
    private static final HashMap<String, Scene> SCENE_MAP = new HashMap<>();

    /**
     * Changes the scene on the stage passed.
     *
     * @param fxml the fxml file to load, e.g. "rename".
     * @param stage the stage to set the scene on.
     * @throws IOException thrown if fxml file is not found.
     */
    public static void changeScene(@Nonnull final String fxml, @Nonnull final Stage stage) throws IOException {
        Scene scene = SCENE_MAP.get(fxml);

        // load if not in map
        if (scene == null) {
            final Parent root = FXMLLoader.load(NavigationController.class.getResource(VIEW_PATH + fxml + ".fxml"));
            scene = new Scene(root);
            SCENE_MAP.put(fxml, scene);
        }

        // keep height and width from current scene.
        final double currentHeight = stage.getHeight();
        final double currentWidth = stage.getWidth();

        stage.setScene(scene);
        stage.setHeight(currentHeight);
        stage.setWidth(currentWidth);
    }
}
