package software.siani.orchilla.operators.decade;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.decade.DecadeConstants.YearsPerDecade;
import static software.siani.orchilla.operators.decade.DecadeConstants.yearsIn;

public record SubDecadeTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Decade) <= 0) {
            LocalDateTime start = temporaltag.head().minusYears(yearsIn(value));
            LocalDateTime end = temporaltag.tail().minusYears(yearsIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusYears(yearsIn(value));
        LocalDateTime end = temporaltag.tail().minusYears(yearsIn(value)).plusYears(YearsPerDecade).minusNanos(1);
        return new TemporalTag(start, end, Period.Decade, temporaltag.distribution().between(start, end));
    }
}
