package entities;

import java.util.Arrays;

public class BitArray {
    private final boolean[] bits;
    private final int size;

    public BitArray(int size) {
        this.bits = new boolean[size];
        this.size = size;
    }

    public void set(int position) { bits[position] = true; }
    public boolean get(int position) { return bits[position]; }
    public void clear() { Arrays.fill(bits, false); }
    public int size() { return size; }
}
