package cli;

public enum Command {
    SET,
    GET,
    DEL,
    HELP,
    EXIT;

    public static Command fromString(String cmd) {
        try {
            return Command.valueOf(cmd.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // unknown command
        }
    }
}
