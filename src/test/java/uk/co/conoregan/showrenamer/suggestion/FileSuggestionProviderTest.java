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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.conoregan.showrenamer.api.ShowResultProvider;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link FileSuggestionProvider}.
 */
public class FileSuggestionProviderTest {
    /**
     * The show result provider.
     */
    final ShowResultProvider showResultProvider = mock(ShowResultProvider.class);

    /**
     * The file suggestion provider.
     */
    final FileSuggestionProvider fileSuggestionProvider = new FileSuggestionProvider(showResultProvider);

    /**
     * Tests {@link FileSuggestionProvider#getImprovedName(File)} when the title of the file name cannot be matched.
     */
    @Test
    public void testGetImprovedNameFileTitleNotFound() {
        final File file = mock(File.class);
        when(file.getName()).thenReturn("2010.txt");
        when(file.isFile()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("2010.txt");

        final Optional<File> newFile = fileSuggestionProvider.getImprovedName(file);
        Assertions.assertTrue(newFile.isEmpty());
    }

    /**
     * Tests {@link FileSuggestionProvider#getImprovedName(File)} when the file is a file, and it is based on a movie.
     */
    @Test
    public void testGetImprovedNameFileMovie() {
        final File file = mock(File.class);
        when(file.getName()).thenReturn("Movie title 2010.txt");
        when(file.isFile()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("Movie title 2010.txt");

        final LocalDate localDate = LocalDate.of(2010, 10, 20);
        final ShowResultProvider.ShowResult showResult =
                new ShowResultProvider.ShowResult("Movie Title", localDate, ShowResultProvider.ShowType.MOVIE);
        when(showResultProvider.getShowResult(anyString(), any(), any())).thenReturn(Optional.of(showResult));

        final Optional<File> newFile = fileSuggestionProvider.getImprovedName(file);
        Assertions.assertTrue(newFile.isPresent());
        Assertions.assertEquals("Movie Title (2010).txt", newFile.get().getName());
    }

    /**
     * Tests {@link FileSuggestionProvider#getImprovedName(File)} when the file is a directory, and it is based on a movie.
     */
    @Test
    public void testGetImprovedNameDirMovie() {
        final File file = mock(File.class);
        when(file.getName()).thenReturn("Movie title 2010");
        when(file.isFile()).thenReturn(false);
        when(file.getAbsolutePath()).thenReturn("Movie title 2010");

        final LocalDate localDate = LocalDate.of(2010, 10, 20);
        final ShowResultProvider.ShowResult showResult =
                new ShowResultProvider.ShowResult("Movie Title", localDate, ShowResultProvider.ShowType.MOVIE);
        when(showResultProvider.getShowResult(anyString(), any(), any())).thenReturn(Optional.of(showResult));

        final Optional<File> newFile = fileSuggestionProvider.getImprovedName(file);
        Assertions.assertTrue(newFile.isPresent());
        Assertions.assertEquals("Movie Title (2010)", newFile.get().getName());
    }

    /**
     * Tests {@link FileSuggestionProvider#getImprovedName(File)} when it is based on a movie but no results are found.
     */
    @Test
    public void testGetImprovedNameMovieNotFound() {
        final File file = mock(File.class);
        when(file.getName()).thenReturn("Movie title 2010");
        when(file.isFile()).thenReturn(false);
        when(file.getAbsolutePath()).thenReturn("Movie title 2010");

        when(showResultProvider.getShowResult(anyString(), any(), any())).thenReturn(Optional.empty());

        final Optional<File> newFile = fileSuggestionProvider.getImprovedName(file);
        Assertions.assertTrue(newFile.isEmpty());
    }

    /**
     * Tests {@link FileSuggestionProvider#getImprovedName(File)} when the file is a file, and it is based on a tv show.
     */
    @Test
    public void testGetImprovedNameFileTvShow() {
        final File file = mock(File.class);
        when(file.getName()).thenReturn("Tv show title 2010.txt");
        when(file.isFile()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("Tv show title 2010.txt");

        final LocalDate localDate = LocalDate.of(2010, 10, 20);
        final ShowResultProvider.ShowResult showResult =
                new ShowResultProvider.ShowResult("Tv Show Title", localDate, ShowResultProvider.ShowType.TV);
        when(showResultProvider.getShowResult(anyString(), any(), any())).thenReturn(Optional.of(showResult));

        final Optional<File> newFile = fileSuggestionProvider.getImprovedName(file);
        Assertions.assertTrue(newFile.isPresent());
        Assertions.assertEquals("Tv Show Title (2010).txt", newFile.get().getName());
    }

    /**
     * Tests {@link FileSuggestionProvider#getImprovedName(File)} when the file is a directory, and it is based on a tv show.
     */
    @Test
    public void testGetImprovedNameDirTvShow() {
        final File file = mock(File.class);
        when(file.getName()).thenReturn("Tv show title 2010");
        when(file.isFile()).thenReturn(false);
        when(file.getAbsolutePath()).thenReturn("Tv show title 2010");

        final LocalDate localDate = LocalDate.of(2010, 10, 20);
        final ShowResultProvider.ShowResult showResult =
                new ShowResultProvider.ShowResult("Tv Show Title", localDate, ShowResultProvider.ShowType.TV);
        when(showResultProvider.getShowResult(anyString(), any(), any())).thenReturn(Optional.of(showResult));

        final Optional<File> newFile = fileSuggestionProvider.getImprovedName(file);
        Assertions.assertTrue(newFile.isPresent());
        Assertions.assertEquals("Tv Show Title (2010)", newFile.get().getName());
    }

    /**
     * Tests {@link FileSuggestionProvider#getImprovedName(File)} when it is based on a tv show but no results are found.
     */
    @Test
    public void testGetImprovedNameTvShowNotFound() {
        final File file = mock(File.class);
        when(file.getName()).thenReturn("Tv show title 2010");
        when(file.isFile()).thenReturn(false);
        when(file.getAbsolutePath()).thenReturn("Tv show title 2010");

        when(showResultProvider.getShowResult(anyString(), any(), any())).thenReturn(Optional.empty());

        final Optional<File> newFile = fileSuggestionProvider.getImprovedName(file);
        Assertions.assertTrue(newFile.isEmpty());
    }

    /**
     * Tests {@link FileSuggestionProvider#getImprovedName(File)} when the file is a file, and it is based on a tv show episode.
     */
    @Test
    public void testGetImprovedNameFileTvShowEpisode() {
        final File file = mock(File.class);
        when(file.getName()).thenReturn("Tv show title 2010 s1e1.txt");
        when(file.isFile()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("Tv show title 2010 s1e1.txt");

        final LocalDate localDate = LocalDate.of(2010, 10, 20);
        final ShowResultProvider.TvEpisodeResult tvEpisodeResult = new ShowResultProvider.TvEpisodeResult(
                "Tv Show Title", localDate, "Episode Name", 1, 1);
        when(showResultProvider.getTvEpisodeResult(anyString(), anyInt(), anyInt(), any(), any())).thenReturn(Optional.of(tvEpisodeResult));

        final Optional<File> newFile = fileSuggestionProvider.getImprovedName(file);
        Assertions.assertTrue(newFile.isPresent());
        Assertions.assertEquals("Tv Show Title (2010) - S01E01 - Episode Name.txt", newFile.get().getName());
    }

    /**
     * Tests {@link FileSuggestionProvider#getImprovedName(File)} when the file is a directory, and it is based on a tv show episode.
     */
    @Test
    public void testGetImprovedNameDirTvShowEpisode() {
        final File file = mock(File.class);
        when(file.getName()).thenReturn("Tv show title 2010 s1e1");
        when(file.isFile()).thenReturn(false);
        when(file.getAbsolutePath()).thenReturn("Tv show title 2010 s1e1");

        final LocalDate localDate = LocalDate.of(2010, 10, 20);
        final ShowResultProvider.TvEpisodeResult tvEpisodeResult = new ShowResultProvider.TvEpisodeResult(
                "Tv Show Title", localDate, "Episode Name", 1, 1);
        when(showResultProvider.getTvEpisodeResult(anyString(), anyInt(), anyInt(), any(), any())).thenReturn(Optional.of(tvEpisodeResult));

        final Optional<File> newFile = fileSuggestionProvider.getImprovedName(file);
        Assertions.assertTrue(newFile.isPresent());
        Assertions.assertEquals("Tv Show Title (2010) - S01E01 - Episode Name", newFile.get().getName());
    }

    /**
     * Tests {@link FileSuggestionProvider#getImprovedName(File)} when it is based on a tv show episode but no results are found.
     */
    @Test
    public void testGetImprovedNameTvShowEpisodeNotFound() {
        final File file = mock(File.class);
        when(file.getName()).thenReturn("Tv show title 2010 s1e1");
        when(file.isFile()).thenReturn(false);
        when(file.getAbsolutePath()).thenReturn("Tv show title 2010 s1e1");

        when(showResultProvider.getTvEpisodeResult(anyString(), anyInt(), anyInt(), any(), any())).thenReturn(Optional.empty());

        final Optional<File> newFile = fileSuggestionProvider.getImprovedName(file);
        Assertions.assertTrue(newFile.isEmpty());
    }
}
