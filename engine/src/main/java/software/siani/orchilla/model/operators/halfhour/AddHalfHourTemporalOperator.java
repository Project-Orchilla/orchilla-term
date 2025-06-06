package software.siani.orchilla.model.operators.halfhour;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.operators.halfhour.HalfHourConstants.MinutesPerHalf;
import static software.siani.orchilla.model.operators.halfhour.HalfHourConstants.minutesIn;

public record AddHalfHourTemporalOperator(int value) implements TemporalOperator {

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
