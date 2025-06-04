package software.siani.orchilla.operators.quarter;

public class QuarterConstants {
    public static final int minutesPerQuarter = 15;

    public static int minutesIn(int quarters) {
        return quarters * minutesPerQuarter;
    }
}
