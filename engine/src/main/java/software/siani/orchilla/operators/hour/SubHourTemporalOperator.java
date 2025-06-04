package software.siani.orchilla.operators.hour;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

public record SubHourTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Hour) <= 0) {
            LocalDateTime start = temporaltag.head().minusHours(value);
            LocalDateTime end = temporaltag.tail().minusHours(value);
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusHours(value);
        LocalDateTime end = temporaltag.tail().minusHours(value).plusHours(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Hour, temporaltag.distribution().between(start, end));
    }
}
