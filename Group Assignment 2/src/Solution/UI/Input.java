package Solution.UI;


import java.util.Scanner;

public class Input {
    private final String label;
    private Validator validated;
    private String err = "Invalid input!";

    public Input(String label) {
        this.label = label;
    }

    public Input setValid(Validator validated) {
        this.validated = validated;
        return this;
    }

    public Input setErr(String err) {
        this.err = err;
        return this;
    }

    public String getInput() {
        System.out.print(label);
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        if (validated == null) return input;
        if (!validated.validated(input)) {
            System.out.println(err);
            return getInput();
        }
        return input;
    }

    private static final Input enterInput = new Input("Press Enter to continue...");

    public static void waitForEnter() {
        enterInput.getInput();
    }
}
