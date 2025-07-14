package software.siani.orchilla.model.functions.millennium;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.functions.millennium.MillenniumConstants.YearsPerMillennium;
import static software.siani.orchilla.model.functions.millennium.MillenniumConstants.yearsIn;

public record SubMillenniumTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Millennium) <= 0) {
            LocalDateTime start = temporaltag.head().minusYears(yearsIn(value));
            LocalDateTime end = temporaltag.tail().minusYears(yearsIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().minusYears(yearsIn(value));
        LocalDateTime end = temporaltag.tail().minusYears(yearsIn(value)).plusYears(YearsPerMillennium).minusNanos(1);
        return new TemporalTag(start, end, Period.Millennium, temporaltag.distribution().between(start, end));
    }
}
