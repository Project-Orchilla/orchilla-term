package software.siani.orchilla.units;

public enum Weekday {
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;

    public static boolean isWeekDay(String s) {
        try {
            Weekday.valueOf(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int value() {
        return this.ordinal() + 1;
    }
}
