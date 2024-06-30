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

package uk.co.conoregan.showrenamer.suggestion;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.conoregan.showrenamer.api.ShowResultProvider;
import uk.co.conoregan.showrenamer.config.preference.PreferenceService;
import uk.co.conoregan.showrenamer.config.preference.ShowRenamerPreference;
import uk.co.conoregan.showrenamer.util.ShowInfoMatcher;
import uk.co.conoregan.showrenamer.util.StringUtils;

/**
 * Class to generate an improved file.
 */
public class FileSuggestionProvider {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSuggestionProvider.class);

    /**
     * The preference service.
     */
    private static final PreferenceService PREFERENCE_SERVICE = new PreferenceService();

    /**
     * The show results provider.
     */
    private final ShowResultProvider showResultProvider;

    /**
     * Constructor.
     *
     * @param showResultProvider the show result provider to use.
     */
    public FileSuggestionProvider(final ShowResultProvider showResultProvider) {
        this.showResultProvider = showResultProvider;
    }

    /**
     * Gets an improved name for the file passed, creating a new file object.
     *
     * @param file the file.
     * @return the file with a new name.
     */
    public Optional<File> getImprovedName(final File file) {
        final String fileName;

        if (file.isFile()) {
            fileName = FilenameUtils.removeExtension(file.getName());
        }
        else {
            fileName = file.getName();
        }

        final Optional<String> matchedTitle = ShowInfoMatcher.matchTitle(fileName);
        final Optional<Integer> matchedYear = ShowInfoMatcher.matchYear(fileName);
        final Optional<Integer> matchedSeason = ShowInfoMatcher.matchSeason(fileName);
        final List<Integer> matchedEpisodes = ShowInfoMatcher.matchEpisodes(fileName);

        if (matchedTitle.isEmpty()) {
            LOGGER.info("No title match found for file name: {}", file.getName());
            return Optional.empty();
        }

        String newFileName;

        // episode
        if (matchedSeason.isPresent() && !matchedEpisodes.isEmpty()) {
            final Optional<ShowResultProvider.TvEpisodeResult> showResult =
                    showResultProvider.getTvEpisodeResult(
                            matchedTitle.get(), matchedSeason.get(), matchedEpisodes.getFirst(), matchedYear.orElse(null), null);

            if (showResult.isEmpty()) {
                LOGGER.info("No result found for title: {}, season: {}, episode: {}",
                    matchedTitle.get(), matchedSeason.get(), matchedEpisodes.getFirst());
                return Optional.empty();
            }

            newFileName = PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.RENAME_FORMAT_TV_SHOW_EPISODE)
                            .replace("{title}", showResult.get().title())
                            .replace("{year}", String.valueOf(showResult.get().date().getYear()))
                            .replace("{season}", String.format("%02d", showResult.get().seasonNumber()))
                            .replace("{episode}", String.format("%02d", showResult.get().episodeNumber()))
                            .replace("{episodeName}", showResult.get().episodeName());
        }
        // movie or tv show
        else {
            final Optional<ShowResultProvider.ShowResult> showResult =
                    showResultProvider.getShowResult(matchedTitle.get(), matchedYear.orElse(null), null);

            if (showResult.isEmpty()) {
                LOGGER.info("No result found for title: {}, year: {}", matchedTitle.get(), matchedYear.orElse(null));
                return Optional.empty();
            }

            final String renameFormat = showResult.get().type().equals(ShowResultProvider.ShowType.MOVIE) ?
                    PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.RENAME_FORMAT_MOVIE) :
                    PREFERENCE_SERVICE.getPreference(ShowRenamerPreference.RENAME_FORMAT_TV_SHOW);

            newFileName = renameFormat
                    .replace("{title}", showResult.get().title())
                    .replace("{year}", String.valueOf(showResult.get().date().getYear()));
        }

        if (SystemUtils.IS_OS_WINDOWS) {
            newFileName = StringUtils.removeForbiddenCharsWindows(newFileName);
        }
        else if (SystemUtils.IS_OS_UNIX) {
            newFileName = StringUtils.removeForbiddenCharsUnix(newFileName);
        }

        return Optional.of(new File(StringUtils.replaceLastOccurrence(file.getAbsolutePath(), fileName, newFileName)));
    }
}
