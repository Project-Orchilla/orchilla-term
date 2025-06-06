package software.siani.orchilla.model.operators.day;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record SetDayTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        int year = temporaltag.head().getYear();
        LocalDate targetDate = LocalDate.ofYearDay(year, value);
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end   = targetDate.atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Day, temporaltag.distribution().between(start, end));
    }
}
