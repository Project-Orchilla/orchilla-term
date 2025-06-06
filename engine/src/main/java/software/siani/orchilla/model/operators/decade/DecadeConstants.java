package software.siani.orchilla.model.operators.decade;

public class DecadeConstants {
    public static final int YearsPerDecade = 10;

    public static int yearsIn(int decades) {
        return decades * YearsPerDecade;
    }
}
