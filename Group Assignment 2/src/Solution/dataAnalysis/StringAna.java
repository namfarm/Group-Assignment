package Solution.dataAnalysis;

public class StringAna {
    public static String multiply(String s, int times) {
        return new String(new char[times]).replace("\0", s);
    }

    public static String replace(String str, int index, char replace) {
        if (str == null) return str;
        if (index < 0 || index >= str.length()) return str;
        char[] chars = str.toCharArray();
        chars[index] = replace;
        return String.valueOf(chars);
    }

    public static String replace(String str, int[] indexes, char replace) {
        char[] chars = str.toCharArray();
        for (var index : indexes) {
            chars[index] = replace;
        }
        return String.valueOf(chars);
    }
}
