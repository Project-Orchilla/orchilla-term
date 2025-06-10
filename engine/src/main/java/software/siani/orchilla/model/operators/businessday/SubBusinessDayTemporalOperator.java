package software.siani.orchilla.model.operators.businessday;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record SubBusinessDayTemporalOperator(int value) implements TemporalOperator {


    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate current = temporaltag.head().toLocalDate();
        int daysToSubtract = Math.abs(value);
        int subtracted    = 0;
        while (subtracted < daysToSubtract) {
            current = current.minusDays(1);
            DayOfWeek dow = current.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                subtracted++;
            }
        }
        LocalDateTime start = current.atStartOfDay();
        LocalDateTime end   = current.atTime(LocalTime.MAX);
        return new TemporalTag(
                start,
                end,
                Period.Day,
                temporaltag.distribution().between(start, end)
        );
    }
}
