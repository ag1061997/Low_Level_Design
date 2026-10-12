package core;

import entities.Document;

import java.util.HashMap;
import java.util.Map;

public class DocumentStore {
    private final Map<String, Document> store = new HashMap<>();

    public void addDocument(Document doc) { store.put(doc.getId(), doc); }
    public Document getDocument(String docId) { return store.get(docId); }
}
