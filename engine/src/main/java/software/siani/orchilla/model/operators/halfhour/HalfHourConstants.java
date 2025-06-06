package software.siani.orchilla.model.operators.halfhour;

public class HalfHourConstants {
    public static final int MinutesPerHalf = 30;

    public static int minutesIn(int halves) {
        return halves * MinutesPerHalf;
    }
}
