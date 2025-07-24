package cli;

import store.Datastore;
import ttl.TTLManager;

import java.util.Scanner;

public class RedisLiteCli {

    private final Datastore store;
    private final TTLManager ttlManager;
    private final Thread ttlThread;

    public RedisLiteCli() {
        this.store = new Datastore();
        this.ttlManager = new TTLManager(store);
        this.ttlThread = new Thread(ttlManager);
        this.ttlThread.start();
    }

    public void start() {
        System.out.println("=== RedisLite CLI ===");
        System.out.println("Commands: SET key value [ttlMs], GET key, DEL key, HELP, EXIT");

        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            Command command = Command.fromString(parts[0]);
            if (command == null) {
                System.out.println("Unknown command: " + parts[0]);
                continue;
            }

            try {
                switch (command) {
                    case SET -> handleSet(parts);
                    case GET -> handleGet(parts);
                    case DEL -> handleDel(parts);
                    case HELP -> showHelp();
                    case EXIT -> {
                        shutdown();
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handleSet(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Usage: SET key value [ttlMs]");
            return;
        }
        String key = parts[1];
        String value = parts[2];
        if (parts.length == 4) {
            long ttl = Long.parseLong(parts[3]);
            store.setStore(key, value, ttl);
        } else {
            store.setStore(key, value);
        }
        System.out.println("OK");
    }

    private void handleGet(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Usage: GET key");
            return;
        }
        String key = parts[1];
        Object val = store.getValue(key);
        System.out.println(val != null ? val : "(nil)");
    }

    private void handleDel(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Usage: DEL key");
            return;
        }
        store.delete(parts[1]);
        System.out.println("Deleted");
    }

    private void shutdown() {
        ttlManager.stop();
        try {
            ttlThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Bye!");
    }

    private void showHelp() {
        System.out.println("""
                    Available Commands:
                    SET key "value with spaces" [ttlMs]
                    GET key
                    DEL key
                    EXIT
                """);
    }
}
