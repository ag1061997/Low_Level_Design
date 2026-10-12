package strategies.ranking;

import entities.SearchResult;

import java.util.Comparator;
import java.util.List;

public class ScoreThenAlphabeticalRankingStrategy implements RankingStrategy {
    @Override
    public void rank(List<SearchResult> results) {
        Comparator<SearchResult> compositeComparator = Comparator
                .comparing(SearchResult::getScore)
                .reversed()
                .thenComparing(result -> result.getDocument().getTitle());

        results.sort(compositeComparator);
    }
}
