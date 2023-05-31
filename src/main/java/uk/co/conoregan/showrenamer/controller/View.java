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

/**
 * Enum to represent a view for fxml.
 */
public enum View {
    RENAME("rename"),
    SETTINGS("settings");

    /**
     * The path to the .fxml files.
     */
    private static final String VIEW_PATH = "/view/";

    /**
     * The fxml file extension.
     */
    private static final String FXML_FILE_EXTENSION = ".fxml";

    /**
     * The path for to load the fxml file from.
     */
    final String path;

    /**
     * The file name, without an extension.
     *
     * @param fileName the file name.
     */
    View(final String fileName) {
        this.path = VIEW_PATH + fileName + FXML_FILE_EXTENSION;
    }

    /**
     * Gets the path.
     *
     * @return the path.
     */
    public String getPath() {
        return path;
    }
}
