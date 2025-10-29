package software.siani.orchilla.model.functions.day;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

public record AddDayTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Day) <= 0) {
            LocalDateTime start = temporaltag.head().plusDays(value);
            LocalDateTime end = temporaltag.tail().plusDays(value);
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusDays(value);
        LocalDateTime end = start.plusDays(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Day, temporaltag.distribution().between(start, end));
    }
}
