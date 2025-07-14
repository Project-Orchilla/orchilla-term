package software.siani.orchilla.model.functions.month;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;
import software.siani.orchilla.model.units.Month;

import java.time.LocalDateTime;

public record NextMonthTemporalFunction(Month month) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime start = startOfNextMonth(temporaltag);
        LocalDateTime end = start.plusMonths(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Month, temporaltag.distribution().between(start, end));
    }

    private static LocalDateTime startOfNextMonth(TemporalTag temporaltag) {
        LocalDateTime plusOneMonth = temporaltag.head().plusMonths(1);
        return LocalDateTime.of(plusOneMonth.getYear(), plusOneMonth.getMonth(), 1, 0, 0, 0);
    }
}
