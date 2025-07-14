package software.siani.orchilla.model.functions.moon;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;
import java.util.List;

public record LastMoonDayFunction(int n, MoonPhase moonPhase) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime lastMonth = temporaltag.head().minusMonths(n);
        List<Integer> daysOfMoonPhase = MoonPhase.getDaysForMoonPhase(lastMonth.getYear(), lastMonth.getMonthValue(), moonPhase);
        LocalDateTime start = LocalDateTime.of(lastMonth.getYear(), lastMonth.getMonthValue(), daysOfMoonPhase.getFirst(), 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(lastMonth.getYear(), lastMonth.getMonthValue(), daysOfMoonPhase.getLast(), 23, 59, 59);
        return new TemporalTag(start, end, Period.Day, temporaltag.distribution());
    }
}
