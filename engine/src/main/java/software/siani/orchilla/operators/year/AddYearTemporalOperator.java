package software.siani.orchilla.operators.year;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

public record AddYearTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Year) <= 0) {
            LocalDateTime start = temporaltag.head().plusYears(value);
            LocalDateTime end = temporaltag.tail().plusYears(value);
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusYears(value);
        LocalDateTime end = temporaltag.tail().plusYears(value).plusYears(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Year, temporaltag.distribution().between(start, end));
    }
}
