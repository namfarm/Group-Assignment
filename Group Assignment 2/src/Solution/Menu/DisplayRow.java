package Solution.Menu;

public class DisplayRow {
    private final String range;

    private final int value;

    public DisplayRow(String range, int value) {
        this.range = range;
        this.value = value;
    }

    public String getRange() {
        return range;
    }

    public int getValue() {
        return value;
    }
}
