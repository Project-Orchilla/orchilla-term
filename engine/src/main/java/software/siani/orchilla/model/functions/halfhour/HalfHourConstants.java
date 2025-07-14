package software.siani.orchilla.model.functions.halfhour;

public class HalfHourConstants {
    public static final int MinutesPerHalf = 30;

    public static int minutesIn(int halves) {
        return halves * MinutesPerHalf;
    }
}
