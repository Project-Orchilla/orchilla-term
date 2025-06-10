package software.siani.orchilla.model.operators.minute;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;

public record SetMinuteTemporalOperator(int minute) implements TemporalOperator {
    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime headDateTime = temporaltag.head();
        int year   = headDateTime.getYear();
        int month  = headDateTime.getMonthValue();
        int day    = headDateTime.getDayOfMonth();
        int hour   = headDateTime.getHour();
        int minuteOfHour = Math.max(0, Math.min(minute, 59));
        LocalDateTime start = LocalDateTime.of(year, month, day, hour, minuteOfHour, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, month, day, hour, minuteOfHour, 59, 999_999_999);
        return new TemporalTag(
                start,
                end,
                Period.Minute,
                temporaltag.distribution().between(start, end)
        );
    }
}
