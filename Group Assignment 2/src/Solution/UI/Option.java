package Solution.UI;

import Solution.Menu.Command;

public class Option {
    private final String key;
    private final String label;
    private final Command callback;

    private static final String[] fields = {"Option", "Action"};

    public static String[] getFields() {
        return fields;
    }

    public Option(String key, String label, Command callback) {
        this.key = key;
        this.label = label;
        this.callback = callback;
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public Command getCallback() {
        return callback;
    }
}
