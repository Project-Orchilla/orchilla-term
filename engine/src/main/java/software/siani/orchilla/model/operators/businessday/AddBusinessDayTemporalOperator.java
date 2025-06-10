package software.siani.orchilla.model.operators.businessday;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AddBusinessDayTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate current = temporaltag.head().toLocalDate();
        int daysToAdd = value;
        int absDays   = Math.abs(daysToAdd);
        int added     = 0;
        int direction = daysToAdd >= 0 ? 1 : -1;
        while (added < absDays) {
            current = current.plusDays(direction);
            DayOfWeek dow = current.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                added++;
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
