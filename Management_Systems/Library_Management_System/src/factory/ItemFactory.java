package factory;

import entities.Book;
import entities.LibraryItem;
import entities.Magazine;
import enums.ItemType;

public class ItemFactory {
    public static LibraryItem createItem(ItemType type, String id, String title, String authorOrPublisher) {
        switch (type) {
            case BOOK: return new Book(id, title, authorOrPublisher);
            case MAGAZINE: return new Magazine(id, title, authorOrPublisher);
            default: throw new IllegalArgumentException("Unknown item type.");
        }
    }
}
