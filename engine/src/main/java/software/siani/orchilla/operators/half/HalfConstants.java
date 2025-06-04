package software.siani.orchilla.operators.half;

public class HalfConstants {
    public static final int MinutesPerHalf = 30;

    public static int minutesIn(int halves) {
        return halves * MinutesPerHalf;
    }
}
