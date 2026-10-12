package strategy.search;

import entities.LibraryItem;

import java.util.List;
import java.util.stream.Collectors;

public class SearchByAuthorStrategy implements SearchStrategy {
    @Override
    public List<LibraryItem> search(String query, List<LibraryItem> items) {
        return items.stream()
                .filter(item -> item.getAuthorOrPublisher().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
