package util;

public class KeyValueContainer {

    private final int key;
    private final String value;

    public KeyValueContainer(int key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
