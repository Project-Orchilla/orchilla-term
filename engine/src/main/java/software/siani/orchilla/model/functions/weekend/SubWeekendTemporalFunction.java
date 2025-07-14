package software.siani.orchilla.model.functions.weekend;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public record SubWeekendTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime friday = temporaltag.head()
                .with(TemporalAdjusters.next(DayOfWeek.FRIDAY))
                .plusWeeks(value);
        LocalDateTime sunday = friday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).plusDays(1).minusNanos(1);
        return new TemporalTag(friday, sunday, Period.Day, temporaltag.distribution().between(friday, sunday));
    }
}
