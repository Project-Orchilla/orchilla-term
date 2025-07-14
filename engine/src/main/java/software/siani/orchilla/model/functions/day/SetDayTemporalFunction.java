package software.siani.orchilla.model.functions.day;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

public record SetDayTemporalFunction(int day) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        int year  = headDate.getYear();
        int month = headDate.getMonthValue();
        LocalDate target;
        if (temporaltag.period().ordinal() <= Period.Month.ordinal()) {
            YearMonth ym = YearMonth.of(year, month);
            int lastDay = ym.lengthOfMonth();
            int dayOfMonth = Math.min(day, lastDay);
            target = LocalDate.of(year, month, dayOfMonth);
        } else target = LocalDate.ofYearDay(year, day);
        LocalDateTime start = target.atStartOfDay();
        LocalDateTime end   = target.atTime(LocalTime.MAX);
        return new TemporalTag(
                start,
                end,
                Period.Day,
                temporaltag.distribution().between(start, end)
        );
    }
}
