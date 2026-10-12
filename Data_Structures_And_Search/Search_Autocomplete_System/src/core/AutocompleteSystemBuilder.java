package core;

import strategy.FrequencyBasedRanking;
import strategy.RankingStrategy;

public class AutocompleteSystemBuilder {
    private RankingStrategy rankingStrategy = new FrequencyBasedRanking();
    private int maxSuggestions = 10;

    public AutocompleteSystemBuilder withRankingStrategy(RankingStrategy strategy) {
        this.rankingStrategy = strategy;
        return this;
    }

    public AutocompleteSystemBuilder withMaxSuggestions(int max) {
        this.maxSuggestions = max;
        return this;
    }

    public AutocompleteSystem build() {
        return new AutocompleteSystem(rankingStrategy, maxSuggestions);
    }
}
