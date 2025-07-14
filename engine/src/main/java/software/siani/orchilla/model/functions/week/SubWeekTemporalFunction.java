package software.siani.orchilla.model.functions.week;

import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.functions.week.WeekConstants.daysIn;

public record SubWeekTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Week) <= 0) {
            LocalDateTime start = temporaltag.head().minusDays(daysIn(value));
            LocalDateTime end = temporaltag.tail().minusDays(daysIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusDays(daysIn(value));
        LocalDateTime end = temporaltag.tail().minusDays(daysIn(value)).plusWeeks(1).minusNanos(1);
        Distribution distribution = temporaltag.distribution().between(start, end);
        return new TemporalTag(start, end, Period.Week, distribution);
    }
}
