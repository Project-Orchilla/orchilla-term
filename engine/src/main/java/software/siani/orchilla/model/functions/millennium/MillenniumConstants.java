package software.siani.orchilla.model.functions.millennium;

public class MillenniumConstants {
    public static final int YearsPerMillennium = 1000;

    public static int yearsIn(int millennium) {
        return millennium * YearsPerMillennium;
    }
}
