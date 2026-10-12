package entities;

public class BloomFilterConfig {
    private final int expectedElements;
    private final double falsePositiveRate;
    private final int bitArraySize;
    private final int numHashFunctions;

    public BloomFilterConfig(int expectedElements, double falsePositiveRate) {
        this.expectedElements = expectedElements;
        this.falsePositiveRate = falsePositiveRate;

        this.bitArraySize = (int) Math.ceil(
                -(expectedElements * Math.log(falsePositiveRate)) / (Math.log(2) * Math.log(2))
        );

        this.numHashFunctions = Math.max(1, (int) Math.round(
                ((double) this.bitArraySize / expectedElements) * Math.log(2)
        ));
    }

    public int getExpectedElements() { return expectedElements; }
    public double getFalsePositiveRate() { return falsePositiveRate; }
    public int getBitArraySize() { return bitArraySize; }
    public int getNumHashFunctions() { return numHashFunctions; }
}
