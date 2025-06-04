package software.siani.orchilla.operators.day;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record SetOrdinalDayTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        int year = temporaltag.head().getYear();
        LocalDate targetDate = LocalDate.ofYearDay(year, value);
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end   = targetDate.atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Day, temporaltag.distribution().between(start, end));
    }
}
