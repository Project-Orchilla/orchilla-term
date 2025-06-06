package software.siani.orchilla.model.operators.lustrum;

public class LustrumConstants {
    public static final int YearsPerLustrum = 5;

    public static int yearsIn(int lustrums) {
        return lustrums * YearsPerLustrum;
    }

    public static int yearsPerLustrum() {
        return YearsPerLustrum;
    }
}
