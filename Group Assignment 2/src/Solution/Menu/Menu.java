package Solution.Menu;

import Solution.UI.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final List<Option> options = new ArrayList<>();

    public void addOption(Option option) {
        options.add(option);
    }

    public void displayOptions() {
        Table table = new Table(Option.getFields());
        options.forEach(option -> {
            table.addRow(new String[]{option.getKey(), option.getLabel()});
        });
        table.display();
    }

    public void execute() {
        Scanner sc = new Scanner(System.in);
        displayOptions();
        while (true) {
            System.out.print("Enter an option: ");
            String input = sc.next();
            for (Option option : options) {
                if (option.getKey().equals(input)) {
                    option.getCallback().exe();
                    return;
                }
            }
            System.out.println("Invalid input.");
        }
    }
}
