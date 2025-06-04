package software.siani.orchilla.operators.lustrum;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.lustrum.LustrumConstants.YearsPerLustrum;
import static software.siani.orchilla.operators.lustrum.LustrumConstants.yearsIn;


public record AddLustrumTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Year) <= 0) {
            LocalDateTime start = temporaltag.head().plusYears(yearsIn(value));
            LocalDateTime end = temporaltag.tail().plusYears(yearsIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusYears(yearsIn(value));
        LocalDateTime end = temporaltag.tail().plusYears(yearsIn(value)).plusYears(YearsPerLustrum).minusNanos(1);
        return new TemporalTag(start, end, Period.Year, temporaltag.distribution().between(start, end));
    }
}
