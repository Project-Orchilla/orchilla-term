package software.siani.orchilla.model.functions.decade;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.functions.decade.DecadeConstants.YearsPerDecade;
import static software.siani.orchilla.model.functions.decade.DecadeConstants.yearsIn;

public record SubDecadeTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Decade) <= 0) {
            LocalDateTime start = temporaltag.head().minusYears(yearsIn(value));
            LocalDateTime end = temporaltag.tail().minusYears(yearsIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusYears(yearsIn(value));
        LocalDateTime end = start.plusYears(YearsPerDecade).minusNanos(1);
        return new TemporalTag(start, end, Period.Decade, temporaltag.distribution().between(start, end));
    }
}
