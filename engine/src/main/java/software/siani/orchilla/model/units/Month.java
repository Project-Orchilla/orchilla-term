package software.siani.orchilla.model.units;

public enum Month {
    Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec;

    public static double numberOfDays() {
        return 30;
    }

    public static double numberOfWeeks() {
        return 4;
    }

    public int value() {
        return this.ordinal() + 1;
    }
}
