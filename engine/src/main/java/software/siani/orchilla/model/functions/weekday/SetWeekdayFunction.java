package software.siani.orchilla.model.functions.weekday;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;
import software.siani.orchilla.model.units.Weekday;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public record SetWeekdayFunction(Weekday day, int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime weekday = temporaltag.head()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.of(day.ordinal() + 1)));
        return new TemporalTag(weekday, weekday.plusDays(1).minusNanos(1), Period.Day, temporaltag.distribution().between(temporaltag.head(), temporaltag.tail()));
    }
}
