package software.siani.orchilla.operators.day;
import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

public record AddDayTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Day) <= 0) {
            LocalDateTime start = temporaltag.head().plusDays(value);
            LocalDateTime end = temporaltag.tail().plusDays(value);
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusDays(value);
        LocalDateTime end = temporaltag.tail().plusDays(value).plusDays(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Day, temporaltag.distribution().between(start, end));
    }
}
