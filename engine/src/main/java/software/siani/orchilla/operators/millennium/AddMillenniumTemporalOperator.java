package software.siani.orchilla.operators.millennium;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.millennium.MillenniumConstants.YearsPerMillennium;
import static software.siani.orchilla.operators.millennium.MillenniumConstants.yearsIn;


public record AddMillenniumTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Millennium) <= 0) {
            LocalDateTime start = temporaltag.head().plusYears(yearsIn(value));
            LocalDateTime end = temporaltag.tail().plusYears(yearsIn(value));
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusYears(yearsIn(value));
        LocalDateTime end = temporaltag.tail().plusYears(yearsIn(value)).plusYears(YearsPerMillennium).minusNanos(1);
        return new TemporalTag(start, end, Period.Millennium, temporaltag.distribution().between(start, end));
    }
}
