package software.siani.orchilla.model.operators.millennium;

public class MillenniumConstants {
    public static final int YearsPerMillennium = 1000;

    public static int yearsIn(int millennium) {
        return millennium * YearsPerMillennium;
    }
}
