package store;

import utils.ValueWithTTL;

import java.util.concurrent.ConcurrentHashMap;

public class Datastore {

    private final ConcurrentHashMap<String, Object> store = new ConcurrentHashMap<>();

    public void setStore(String key, Object value) {
        store.put(key, value);
    }

    public void setStore(String key, Object value, long expiry) {
        store.put(key, new ValueWithTTL(value, expiry));
    }

    public Object getValue(String key) {
        return getIfNotExpired(key);
    }

    public boolean isExists(String key) {
        return getIfNotExpired(key) != null;
    }

    public void delete(String key) {
        store.remove(key);
    }

    private Object getIfNotExpired(String key) {
        Object obj = store.get(key);
        if (obj instanceof ValueWithTTL val && val.isExpired()) {
            store.remove(key);
            return null;
        }
        return obj;
    }

    public ConcurrentHashMap<String, Object> getStore() {
        return store;
    }

    public boolean isKeyExpired(String key) {
        Object obj = store.get(key);
        if (obj instanceof ValueWithTTL val && val.isExpired()) {
            store.remove(key);
            return true;
        }
        return false;
    }

}
