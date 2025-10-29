package software.siani.orchilla.model.functions.businessday;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static software.siani.orchilla.model.functions.businessday.HolidaysChecker.isHoliday;

public record SubBusinessDayTemporalFunction(int value) implements TemporalFunction {


    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate current = temporaltag.head().toLocalDate();
        int daysToSubtract = Math.abs(value);
        int subtracted = 0;
        while (subtracted < daysToSubtract) {
            current = current.minusDays(1);
            DayOfWeek dow = current.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY || isHoliday(current)) {
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
