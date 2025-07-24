package utils;

public class ValueWithTTL {
    private final Object value;
    private final long expiryTime;

    public ValueWithTTL(Object value, long expiryTime) {
        this.value = value;
        this.expiryTime = expiryTime;
    }

    public boolean isExpired() {
        return expiryTime != -1 && System.currentTimeMillis() > expiryTime;
    }

}
