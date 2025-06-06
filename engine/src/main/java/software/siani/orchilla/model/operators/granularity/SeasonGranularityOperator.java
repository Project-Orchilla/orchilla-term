package software.siani.orchilla.model.operators.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;
import software.siani.orchilla.model.units.Month;  // your custom Month enum

import java.time.*;

import static java.time.Month.*;
import static java.time.Month.AUGUST;
import static java.time.Month.JANUARY;
import static java.time.Month.JUNE;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static java.time.Month.SEPTEMBER;

public record SeasonGranularityOperator() implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        int year = headDate.getYear();
        Month month = Month.values()[headDate.getMonth().getValue() - 1];
        LocalDate seasonStart;
        LocalDate seasonEnd;
        switch (month) {
            case Dec -> {
                seasonStart = LocalDate.of(year, DECEMBER, 1);
                int nextYear = year + 1;
                boolean isLeapNext = Year.isLeap(nextYear);
                int febLastDay = isLeapNext ? 29 : 28;
                seasonEnd = LocalDate.of(nextYear, FEBRUARY, febLastDay);
            }
            case Jan, Feb -> {
                int prevYear = year - 1;
                seasonStart = LocalDate.of(prevYear, DECEMBER, 1);
                boolean isLeapThis = Year.isLeap(year);
                int febLastDay = isLeapThis ? 29 : 28;
                seasonEnd = LocalDate.of(year, FEBRUARY, febLastDay);
            }
            case Mar, Apr, May -> {
                seasonStart = LocalDate.of(year, MARCH, 1);
                seasonEnd = LocalDate.of(year, MAY, 31);
            }
            case Jun, Jul, Aug -> {
                seasonStart = LocalDate.of(year, JUNE, 1);
                seasonEnd = LocalDate.of(year, AUGUST, 31);
            }
            case Sep, Oct, Nov -> {
                seasonStart = LocalDate.of(year, SEPTEMBER, 1);
                seasonEnd = LocalDate.of(year, NOVEMBER, 30);
            }
            default -> {
                seasonStart = LocalDate.of(year, JANUARY, 1);
                seasonEnd = LocalDate.of(year, DECEMBER, 31);
            }
        }
        LocalDateTime startDateTime = seasonStart.atStartOfDay();
        LocalDateTime endDateTime = seasonEnd.atTime(LocalTime.MAX);
        return new TemporalTag(startDateTime, endDateTime, Period.Month, temporaltag.distribution().between(startDateTime, endDateTime));
    }
}
