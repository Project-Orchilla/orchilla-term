package software.siani.orchilla.model.units;

public enum Month {
    Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec;

    public static double numberOfDays() {
        return 30;
    }

    public static double numberOfWeeks() {
        return 4;
    }

    public static Month from(String capitalize) {
        return switch (capitalize) {
            case "January" -> Jan;
            case "February" -> Feb;
            case "March" -> Mar;
            case "April" -> Apr;
            case "May" -> May;
            case "June" -> Jun;
            case "July" -> Jul;
            case "August" -> Aug;
            case "September" -> Sep;
            case "October" -> Oct;
            case "November" -> Nov;
            case "December" -> Dec;
            default -> throw new IllegalStateException("Unexpected value: " + capitalize);
        };
    }

    public int value() {
        return this.ordinal() + 1;
    }
}
