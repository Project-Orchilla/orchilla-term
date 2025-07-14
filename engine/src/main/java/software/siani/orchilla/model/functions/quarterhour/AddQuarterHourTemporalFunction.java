package software.siani.orchilla.model.functions.quarterhour;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.functions.quarterhour.QuarterHourConstants.minutesIn;

public record AddQuarterHourTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Minute) <= 0) {
            LocalDateTime start = temporaltag.head().plusMinutes(minutesIn(value));
            LocalDateTime end = temporaltag.tail().plusMinutes(minutesIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusMinutes(minutesIn(value));
        LocalDateTime end = temporaltag.tail().plusMinutes(minutesIn(value)).plusMinutes(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Minute, temporaltag.distribution().between(start, end));
    }
}
