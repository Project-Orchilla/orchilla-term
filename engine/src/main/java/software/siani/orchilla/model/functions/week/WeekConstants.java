package software.siani.orchilla.model.functions.week;

public class WeekConstants {
    public static final int daysPerWeek = 7;

    public static int daysIn(int weeks) {
        return weeks * daysPerWeek;
    }
}
