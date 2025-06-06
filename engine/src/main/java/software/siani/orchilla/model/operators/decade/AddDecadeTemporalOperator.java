package software.siani.orchilla.model.operators.decade;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.operators.decade.DecadeConstants.YearsPerDecade;
import static software.siani.orchilla.model.operators.decade.DecadeConstants.yearsIn;


public record AddDecadeTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Decade) <= 0) {
            LocalDateTime start = temporaltag.head().plusYears(yearsIn(value));
            LocalDateTime end = temporaltag.tail().plusYears(yearsIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusYears(yearsIn(value));
        LocalDateTime end = temporaltag.tail().plusYears(yearsIn(value)).plusYears(YearsPerDecade).minusNanos(1);
        return new TemporalTag(start, end, Period.Decade, temporaltag.distribution().between(start, end));
    }
}
