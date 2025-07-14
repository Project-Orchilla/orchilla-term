package software.siani.orchilla.model.functions.century;

public class CenturyConstants {
    public static final int YearsPerCentury = 100;

    public static int yearsIn(int centuries) {
        return centuries * YearsPerCentury;
    }
}
