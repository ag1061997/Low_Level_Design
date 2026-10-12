package strategies.scoring;

import entities.Document;
import entities.Posting;

public class TermFrequencyScoringStrategy implements ScoringStrategy {
    @Override
    public double calculateScore(String term, Posting posting, Document document) {
        return posting.getFrequency();
    }
}
