package software.siani.orchilla.model.functions.lustrum;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.functions.lustrum.LustrumConstants.YearsPerLustrum;
import static software.siani.orchilla.model.functions.lustrum.LustrumConstants.yearsIn;


public record SubLustrumTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Year) <= 0) {
            LocalDateTime start = temporaltag.head().minusYears(yearsIn(value));
            LocalDateTime end = temporaltag.tail().minusYears(yearsIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusYears(yearsIn(value));
        LocalDateTime end = temporaltag.tail().minusYears(yearsIn(value)).plusYears(YearsPerLustrum).minusNanos(1);
        return new TemporalTag(start, end, Period.Year, temporaltag.distribution().between(start, end));
    }
}
