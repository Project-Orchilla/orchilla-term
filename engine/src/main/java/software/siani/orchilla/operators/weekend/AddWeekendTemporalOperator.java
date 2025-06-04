package software.siani.orchilla.operators.weekend;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public record AddWeekendTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime friday = temporaltag.head()
                .with(TemporalAdjusters.next(DayOfWeek.FRIDAY))
                .plusWeeks(value);
        LocalDateTime sunday = friday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).plusDays(1).minusNanos(1);
        return new TemporalTag(friday, sunday, Period.Day, temporaltag.distribution().between(friday, sunday));
    }
}
