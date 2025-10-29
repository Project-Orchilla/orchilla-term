package software.siani.orchilla.model.functions.businessday;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static software.siani.orchilla.model.functions.businessday.HolidaysChecker.isHoliday;

public record AddBusinessDayTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate current = temporaltag.head().toLocalDate();
        int daysToAdd = value;
        int absDays = Math.abs(daysToAdd);
        int added = 0;
        int direction = daysToAdd >= 0 ? 1 : -1;
        while (added < absDays) {
            current = current.plusDays(direction);
            DayOfWeek dow = current.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY || isHoliday(current)) added++;
        }
        LocalDateTime start = current.atStartOfDay();
        LocalDateTime end = current.atTime(LocalTime.MAX);
        return new TemporalTag(
                start,
                end,
                Period.Day,
                temporaltag.distribution().between(start, end)
        );
    }
}
