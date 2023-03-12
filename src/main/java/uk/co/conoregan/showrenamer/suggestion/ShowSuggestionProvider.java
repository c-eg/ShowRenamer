package uk.co.conoregan.showrenamer.suggestion;

import java.util.Optional;

public interface ShowSuggestionProvider {
    Optional<String> getSuggestion(final String fileName);
}
