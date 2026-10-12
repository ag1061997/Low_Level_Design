package entities;

import java.util.*;

public class InvertedIndex {
    private final Map<String, List<Posting>> index = new HashMap<>();

    public void add(String term, String documentId, int frequency) {
        List<Posting> postings = index.getOrDefault(term, new ArrayList<>());
        postings.add(new Posting(documentId, frequency));
        index.put(term, postings);
    }

    public List<Posting> getPostings(String term) {
        return index.getOrDefault(term, Collections.emptyList());
    }
}
