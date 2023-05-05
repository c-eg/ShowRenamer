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

/**
 * This is a class to call the main function from a class that doesn't extend Application. It is needed in order to run the app without
 * module-info. I don't want to add module info until all dependencies have it too, in order to generate a runtime image with jlink.
 * For now, I will create a 'fat' jar and use jpackage to generate an installer.
 */
public class ShowRenamerLauncher {
    /**
     * Start function.
     *
     * @param args the args.
     */
    public static void main(final String[] args) {
        ShowRenamerApplication.main(args);
    }
}