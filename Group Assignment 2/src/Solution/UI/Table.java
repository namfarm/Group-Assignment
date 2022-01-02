package Solution.UI;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final String[] labels;
    private final List<String[]> rows = new ArrayList<>();
    private static final String delimeter = " | ";

    public Table(String[] labels) {
        this.labels = labels;
    }

    public void addRow(String[] row) {
        rows.add(row);
    }

    private void printRow(String[] row) {
        int[] columnsLength = getColLength();
        System.out.print(delimeter);
        for (int i = 0; i < row.length; i++) {
            String field = row[i];
            System.out.print(field);
            printMultiple(" ", columnsLength[i] - field.length());
            System.out.print(delimeter);
        }
        System.out.println();
    }

    private int[] getColLength() {
        int[] length = new int[labels.length];
        for (int i = 0; i < labels.length; i++) {
            length[i] = labels[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < labels.length; i++) {
                length[i] = Math.max(row[i].length(), length[i]);
            }

        }
        return length;
    }

    private void printBorder() {
        System.out.print(" ");
        printMultiple("-", getWidth() - 2);
        System.out.print(" ");
        System.out.println();
    }

    private int getWidth() {
        int width = (labels.length + 1) * delimeter.length();
        for (int columnLength : getColLength()) {
            width += columnLength;
        }
        return width;
    }

    public static void printMultiple(String s, int times) {
        System.out.print(new String(new char[times]).replace("\0", s));
    }

    public List<String[]> getRows() {
        return rows;
    }
    public void display() {
        printBorder();
        printRow(labels);
        printBorder();
        for (String[] row : rows) {
            printRow(row);
        }
        printBorder();
    }
}

