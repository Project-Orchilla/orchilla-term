package software.siani.orchilla.operators.quarter;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.quarter.QuarterConstants.minutesIn;

public record SubQuarterTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Minute) <= 0) {
            LocalDateTime start = temporaltag.head().minusMinutes(minutesIn(value));
            LocalDateTime end = temporaltag.tail().minusMinutes(minutesIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusMinutes(minutesIn(value));
        LocalDateTime end = temporaltag.tail().minusMinutes(minutesIn(value)).plusMinutes(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Minute, temporaltag.distribution().between(start, end));
    }
}
