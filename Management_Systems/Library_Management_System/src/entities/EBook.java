package entities;

public class EBook extends LibraryItem {
    private final String author;
    private final int totalLicenses;
    private int activeLoans = 0;

    public EBook(String id, String title, String author, int totalLicenses) {
        super(id, title);
        this.author = author;
        this.totalLicenses = totalLicenses;
    }

    @Override
    public String getAuthorOrPublisher() { return author; }

    public synchronized boolean lend() {
        if (activeLoans >= totalLicenses) return false;
        activeLoans++;
        return true;
    }

    public synchronized void returnLicense() {
        if (activeLoans > 0) activeLoans--;
    }
}
