package software.siani.orchilla.model.functions.hour;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

public record SubHourTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Hour) <= 0) {
            LocalDateTime start = temporaltag.head().minusHours(value);
            LocalDateTime end = temporaltag.tail().minusHours(value);
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusHours(value);
        LocalDateTime end = start.plusHours(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Hour, temporaltag.distribution().between(start, end));
    }
}
