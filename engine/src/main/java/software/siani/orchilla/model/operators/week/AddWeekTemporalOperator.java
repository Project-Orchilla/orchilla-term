package software.siani.orchilla.model.operators.week;

import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.operators.week.WeekConstants.daysIn;

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
