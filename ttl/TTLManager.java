package ttl;

import store.Datastore;

public class TTLManager implements Runnable {
    private final Datastore store;
    private volatile boolean isRunning;

    public TTLManager(Datastore store) {
        this.store = store;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(1000);
                for (String key : store.getStore().keySet()) {
                    store.isKeyExpired(key);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }

    }

    public void stop() {
        isRunning = false;
    }

}
