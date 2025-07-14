package software.siani.orchilla.model.functions.weekday;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;
import software.siani.orchilla.model.units.Weekday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public record NextWeekdayFunction(int value, Weekday day) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        DayOfWeek dow = DayOfWeek.of(day.ordinal() + 1);
        LocalDate next = headDate.with(TemporalAdjusters.next(dow));
        LocalDate targetDate = next.plusWeeks(Math.max(0, value - 1));
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end   = targetDate.atTime(LocalTime.MAX);
        return new TemporalTag(
                start,
                end,
                Period.Day,
                temporaltag.distribution().between(start, end)
        );
    }
}
