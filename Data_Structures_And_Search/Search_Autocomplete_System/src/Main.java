import core.AutocompleteSystem;
import core.AutocompleteSystemBuilder;
import strategy.AlphabeticalRanking;
import strategy.FrequencyBasedRanking;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("----------- SCENARIO 1: Frequency-based Ranking -----------");

        AutocompleteSystem systemByFrequency = new AutocompleteSystemBuilder()
                .withMaxSuggestions(5)
                .withRankingStrategy(new FrequencyBasedRanking())
                .build();

        List<String> dictionary = List.of(
                "car", "cat", "cart", "cartoon", "canada", "candy",
                "car", "canada", "canada", "car", "canada", "canopy", "captain"
        );
        systemByFrequency.addWords(dictionary);

        String prefix1 = "ca";
        List<String> suggestions1 = systemByFrequency.getSuggestions(prefix1);
        System.out.println("Suggestions for '" + prefix1 + "': " + suggestions1);

        String prefix2 = "car";
        List<String> suggestions2 = systemByFrequency.getSuggestions(prefix2);
        System.out.println("Suggestions for '" + prefix2 + "': " + suggestions2);


        System.out.println("\n----------- SCENARIO 2: Alphabetical Ranking -----------");

        AutocompleteSystem systemAlphabetical = new AutocompleteSystemBuilder()
                .withRankingStrategy(new AlphabeticalRanking())
                .build();

        systemAlphabetical.addWords(dictionary);

        List<String> suggestions3 = systemAlphabetical.getSuggestions(prefix1);
        System.out.println("Suggestions for '" + prefix1 + "' (alphabetical): " + suggestions3);
    }
}