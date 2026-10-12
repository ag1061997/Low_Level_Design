package strategies.ranking;

import entities.SearchResult;

import java.util.Comparator;
import java.util.List;

public class ScoreBasedRankingStrategy implements RankingStrategy {
    @Override
    public void rank(List<SearchResult> results) {
        results.sort(Comparator.comparing(SearchResult::getScore).reversed());
    }
}
