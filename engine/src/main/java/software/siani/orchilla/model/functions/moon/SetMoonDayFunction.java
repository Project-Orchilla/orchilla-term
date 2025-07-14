package software.siani.orchilla.model.functions.moon;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;
import java.util.List;

public record SetMoonDayFunction(MoonPhase moonPhase) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        List<Integer> daysOfMoonPhase = MoonPhase.getDaysForMoonPhase(temporaltag.head().getYear(), temporaltag.head().getMonthValue(), moonPhase);
        LocalDateTime start = LocalDateTime.of(temporaltag.head().getYear(), temporaltag.head().getMonthValue(), daysOfMoonPhase.getFirst(), 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(temporaltag.head().getYear(), temporaltag.head().getMonthValue(), daysOfMoonPhase.getLast(), 23, 59, 59);
        return new TemporalTag(start, end, Period.Day, temporaltag.distribution());
    }
}
