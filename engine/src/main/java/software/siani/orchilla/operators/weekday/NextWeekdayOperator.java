package software.siani.orchilla.operators.weekday;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;
import software.siani.orchilla.units.Weekday;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public record NextWeekdayOperator(Weekday day) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime weekday = temporaltag.head().with(TemporalAdjusters.next(DayOfWeek.of(day.ordinal())));
        return new TemporalTag(weekday, weekday.plusDays(1).minusNanos(1), Period.Day, null);
    }
}
