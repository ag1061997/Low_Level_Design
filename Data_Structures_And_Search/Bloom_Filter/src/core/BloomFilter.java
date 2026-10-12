package core;

import entities.BitArray;
import entities.BloomFilterConfig;
import strategy.HashStrategy;
import strategy.MurmurHashStrategy;

public class BloomFilter {
    private final BloomFilterConfig config;
    private final BitArray bitArray;
    private final HashStrategy hashStrategy;

    private BloomFilter(BloomFilterConfig config, HashStrategy hashStrategy) {
        this.config = config;
        this.bitArray = new BitArray(config.getBitArraySize());
        this.hashStrategy = hashStrategy;
    }

    public synchronized void add(String element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        for (int i = 0; i < config.getNumHashFunctions(); i++) {
            int position = hashStrategy.hash(element, i, config.getBitArraySize());
            bitArray.set(position);
        }
    }

    public synchronized boolean mightContain(String element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        for (int i = 0; i < config.getNumHashFunctions(); i++) {
            int position = hashStrategy.hash(element, i, config.getBitArraySize());
            if (!bitArray.get(position)) {
                return false;
            }
        }
        return true;
    }

    public synchronized void clear() { bitArray.clear(); }
    public BloomFilterConfig getConfig() { return config; }


    public static class Builder {
        private final int expectedElements;
        private double falsePositiveRate = 0.01;
        private HashStrategy hashStrategy = new MurmurHashStrategy();

        public Builder(int expectedElements) {
            this.expectedElements = expectedElements;
        }

        public Builder falsePositiveRate(double rate) {
            this.falsePositiveRate = rate;
            return this;
        }

        public Builder hashStrategy(HashStrategy strategy) {
            this.hashStrategy = strategy;
            return this;
        }

        public BloomFilter build() {
            if (expectedElements <= 0) {
                throw new IllegalArgumentException("Expected elements must be positive");
            }
            if (falsePositiveRate <= 0 || falsePositiveRate >= 1) {
                throw new IllegalArgumentException(
                        "False positive rate must be between 0 and 1 (exclusive)");
            }

            BloomFilterConfig config = new BloomFilterConfig(expectedElements, falsePositiveRate);
            return new BloomFilter(config, hashStrategy);
        }
    }
}
