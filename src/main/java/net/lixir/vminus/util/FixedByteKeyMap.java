package net.lixir.vminus.util;

public class FixedByteKeyMap {
    private final String[] byteToString;
    private final int capacity;

    public FixedByteKeyMap(String[] keys) {
        if (keys.length > 256)
            throw new IllegalArgumentException("FixedByteKeyMap cannot exceed 256 entries");

        this.capacity = keys.length;
        this.byteToString = new String[capacity];

        for (int i = 0; i < capacity; i++) {
            String key = keys[i];
            if (key == null)
                throw new IllegalArgumentException("Key at index " + i + " is null");
            byteToString[i] = key;
        }
    }

    public String getString(byte index) {
        int i = index & 0xFF;
        if (i >= capacity)
            throw new IndexOutOfBoundsException("Invalid byte key: " + i);
        return byteToString[i];
    }

    public int size() {
        return capacity;
    }
}
