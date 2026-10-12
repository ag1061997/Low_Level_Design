package strategy.recommend;

import entities.LibraryItem;
import entities.Member;

import java.util.List;

public interface RecommendationStrategy {
    List<LibraryItem> recommend(Member member, List<LibraryItem> catalog);
}
