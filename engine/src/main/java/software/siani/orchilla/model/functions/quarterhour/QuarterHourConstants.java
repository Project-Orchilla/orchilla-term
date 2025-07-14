package software.siani.orchilla.model.functions.quarterhour;

public class QuarterHourConstants {
    public static final int minutesPerQuarter = 15;

    public static int minutesIn(int quarters) {
        return quarters * minutesPerQuarter;
    }
}
