package strategy;

import entities.Suggestion;

import java.util.List;

public interface RankingStrategy {
    List<Suggestion> rank(List<Suggestion> suggestions);
}
