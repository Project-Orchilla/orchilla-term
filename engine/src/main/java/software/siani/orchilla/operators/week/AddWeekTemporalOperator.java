package software.siani.orchilla.operators.week;

import software.siani.orchilla.Distribution;
import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.week.WeekConstants.daysIn;

public record AddWeekTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Week) <= 0) {
            LocalDateTime start = temporaltag.head().plusDays(daysIn(value));
            LocalDateTime end = temporaltag.tail().plusDays(daysIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusDays(daysIn(value));
        LocalDateTime end = temporaltag.tail().plusDays(daysIn(value)).plusWeeks(1).minusNanos(1);
        Distribution distribution = temporaltag.distribution().between(start, end);
        return new TemporalTag(start, end, Period.Week, distribution);
    }
}
