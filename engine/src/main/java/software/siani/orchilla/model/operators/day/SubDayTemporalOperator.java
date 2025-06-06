package software.siani.orchilla.model.operators.day;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;

public record SubDayTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Day) <= 0) {
            LocalDateTime start = temporaltag.head().minusDays(value);
            LocalDateTime end = temporaltag.tail().minusDays(value);
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusDays(value);
        LocalDateTime end = temporaltag.tail().minusDays(value).plusDays(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Day, temporaltag.distribution().between(start, end));
    }
}
