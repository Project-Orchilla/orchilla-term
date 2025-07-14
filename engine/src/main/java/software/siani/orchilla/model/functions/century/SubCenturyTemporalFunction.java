package software.siani.orchilla.model.functions.century;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.functions.century.CenturyConstants.YearsPerCentury;
import static software.siani.orchilla.model.functions.century.CenturyConstants.yearsIn;

public record SubCenturyTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Century) <= 0) {
            LocalDateTime start = temporaltag.head().minusYears(yearsIn(value));
            LocalDateTime end = temporaltag.tail().minusYears(yearsIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusYears(yearsIn(value));
        LocalDateTime end = temporaltag.tail().minusYears(yearsIn(value)).plusYears(YearsPerCentury).minusNanos(1);
        return new TemporalTag(start, end, Period.Century, temporaltag.distribution().between(start, end));
    }
}
