package software.siani.orchilla.model.operators.century;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.operators.century.CenturyConstants.YearsPerCentury;
import static software.siani.orchilla.model.operators.century.CenturyConstants.yearsIn;

public record SubCenturyTemporalOperator(int value) implements TemporalOperator {

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
