package strategy.recommend;

import entities.LibraryItem;
import entities.Member;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SameAuthorStrategy implements RecommendationStrategy {
    @Override
    public List<LibraryItem> recommend(Member member, List<LibraryItem> catalog) {
        Set<String> authors = member.getLoans().stream()
                .map(loan -> loan.getCopy().getItem().getAuthorOrPublisher())
                .collect(Collectors.toSet());
        return catalog.stream()
                .filter(item -> authors.contains(item.getAuthorOrPublisher()))
                .collect(Collectors.toList());
    }
}
