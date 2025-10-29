package software.siani.orchilla.model.functions.year;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

public record SubYearTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Year) <= 0) {
            LocalDateTime start = temporaltag.head().minusYears(value);
            LocalDateTime end = temporaltag.tail().minusYears(value);
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusYears(value);
        LocalDateTime end = start.plusYears(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Year, temporaltag.distribution().between(start, end));
    }
}
