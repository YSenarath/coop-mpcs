package util;

import java.io.Serializable;

 public class KeyValueContainer implements Serializable {

    private final String key;
    private final String value;

    public KeyValueContainer(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
