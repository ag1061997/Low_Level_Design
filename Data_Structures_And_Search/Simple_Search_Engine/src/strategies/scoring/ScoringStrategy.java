package strategies.scoring;

import entities.Document;
import entities.Posting;

public interface ScoringStrategy {
    double calculateScore(String term, Posting posting, Document document);
}
