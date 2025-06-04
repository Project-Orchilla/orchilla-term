package software.siani.orchilla.operators.half;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.half.HalfConstants.MinutesPerHalf;
import static software.siani.orchilla.operators.half.HalfConstants.minutesIn;

public record AddHalfTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Minute) <= 0) {
            LocalDateTime start = temporaltag.head().plusMinutes(minutesIn(value));
            LocalDateTime end = temporaltag.tail().plusMinutes(minutesIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusMinutes(minutesIn(value));
        LocalDateTime end = temporaltag.tail().plusMinutes(minutesIn(value)).plusMinutes(MinutesPerHalf).minusNanos(1);
        return new TemporalTag(start, end, Period.Minute, temporaltag.distribution().between(start, end));
    }
}
