package entities;

public class Posting {
    private final String documentId;
    private final int frequency;

    public Posting(String documentId, int frequency) {
        this.documentId = documentId;
        this.frequency = frequency;
    }

    public String getDocumentId() { return documentId; }
    public int getFrequency() { return frequency; }
}
