package strategy;

import entities.Split;

import java.util.List;

public interface SplitStrategy {
    void validate(List<Split> splits, double totalAmount);
    void calculateSplits(List<Split> splits, double totalAmount);
}
