package software.siani.orchilla.model.functions.month;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

public record SubMonthTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Month) <= 0) {
            LocalDateTime start = temporaltag.head().minusMonths(value);
            LocalDateTime end = temporaltag.tail().minusMonths(value);
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusMonths(value);
        LocalDateTime end = temporaltag.tail().minusMonths(value).plusMonths(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Month, temporaltag.distribution().between(start, end));
    }
}
