package software.siani.orchilla.model.operators.fuzzy;


import software.siani.orchilla.model.Period;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;

public class Utils {

    static double unitValue(LocalDateTime localDateTime, Period period) {
        return switch (period) {
            case Second -> localDateTime.getSecond();
            case Minute -> localDateTime.getMinute();
            case Hour -> localDateTime.getHour();
            case Day -> localDateTime.getDayOfMonth();
            case Week -> localDateTime.get(ALIGNED_WEEK_OF_YEAR);
            case Month -> localDateTime.getMonthValue();
            case Year -> localDateTime.getYear();
            case Decade -> localDateTime.getYear() / 10.0;
            case Century -> localDateTime.getYear() / 100.0;
            case Millennium -> localDateTime.getYear() / 1000.0;
        };
    }
}
