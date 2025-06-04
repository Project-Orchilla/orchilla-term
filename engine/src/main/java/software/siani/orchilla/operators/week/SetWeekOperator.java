package software.siani.orchilla.operators.week;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;

public record SetWeekOperator(int number) implements TemporalOperator {
    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate startDate = LocalDate.of(temporaltag.head().getYear(), 1, 4)
                .with(WeekFields.ISO.weekOfWeekBasedYear(), number)
                .with(WeekFields.ISO.dayOfWeek(), 1);
        LocalDate endDate = startDate.plusDays(6);
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Week, temporaltag.distribution().between(start, end));
    }
}
