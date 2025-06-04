package software.siani.orchilla.operators.century;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.century.CenturyConstants.YearsPerCentury;
import static software.siani.orchilla.operators.century.CenturyConstants.yearsIn;

public record AddCenturyTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Century) <= 0) {
            LocalDateTime start = temporaltag.head().plusYears(yearsIn(value));
            LocalDateTime end = temporaltag.tail().plusYears(yearsIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusYears(yearsIn(value));
        LocalDateTime end = temporaltag.tail().plusYears(yearsIn(value)).plusYears(YearsPerCentury).minusNanos(1);
        return new TemporalTag(start, end, Period.Century, temporaltag.distribution().between(start, end));
    }
}
