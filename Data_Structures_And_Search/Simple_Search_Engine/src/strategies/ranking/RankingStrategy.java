package strategies.ranking;

import entities.SearchResult;

import java.util.List;

public interface RankingStrategy {
    void rank(List<SearchResult> results);
}
