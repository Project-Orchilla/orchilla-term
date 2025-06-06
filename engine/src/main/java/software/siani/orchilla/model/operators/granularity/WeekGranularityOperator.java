package software.siani.orchilla.model.operators.granularity;

import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record WeekGranularityOperator() implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        LocalDate monday = headDate.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);
        LocalDateTime start = monday.atStartOfDay();
        LocalDateTime end = sunday.atTime(LocalTime.MAX);
        Distribution distribution = temporaltag.distribution().between(start, end);
        return new TemporalTag(start, end, Period.Week, distribution);
    }
}
