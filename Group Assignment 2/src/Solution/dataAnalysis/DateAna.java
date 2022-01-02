package Solution.dataAnalysis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateAna {
    private static final String DATE_FORMAT = "MM/dd/yyyy";

    private DateAna() {
    }

    public static Date parse(String s) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        return sdf.parse(s);
    }

    public static Date parseNullable(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String format(Date d) {
        if (d == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(d);
    }

    public static boolean isSameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) return false;
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        return fmt.format(d1).equals(fmt.format(d2));
    }

    public static boolean isBeforeInclusive(Date d1, Date d2) {
        if (d1 == null || d2 == null) return false;
        return d1.before(d2) || isSameDay(d1, d2);
    }

    public static boolean isAfterInclusive(Date d1, Date d2) {
        if (d1 == null || d2 == null) return false;
        return d1.after(d2) || isSameDay(d1, d2);
    }

    public static boolean isBetween(Date d, Date start, Date end) {
        return isAfterInclusive(d, start) && isBeforeInclusive(d, end);
    }

    public static int compareDateAsString(String s1, String s2) {
        try {
            return parse(s1).compareTo(parse(s2));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}