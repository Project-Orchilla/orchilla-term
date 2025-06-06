package software.siani.orchilla.model.operators.day;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

public record LastDayTemporalOperator(int day) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().ordinal() <= Period.Month.ordinal()) {
            LocalDate headDate = temporaltag.head().toLocalDate();
            int year  = headDate.getYear();
            int month = headDate.getMonthValue();
            LocalDate target;
            if (headDate.getDayOfMonth() > day)
                target = LocalDate.of(year, month, Math.min(day, YearMonth.of(year, month).lengthOfMonth()));
            else {
                YearMonth previousDayOfMonth = YearMonth.of(year, month).minusMonths(1);
                int lengthPrevMonth = previousDayOfMonth.lengthOfMonth();
                target = LocalDate.of(previousDayOfMonth.getYear(), previousDayOfMonth.getMonthValue(), Math.min(day, lengthPrevMonth));
            }
            LocalDateTime start = target.atStartOfDay();
            LocalDateTime end   = target.atTime(LocalTime.MAX);
            return new TemporalTag(start, end, Period.Day, temporaltag.distribution().between(start, end));
        }
        LocalDate target = LocalDate.ofYearDay(temporaltag.head().toLocalDate().getYear(), day);
        if (!target.isBefore(temporaltag.head().toLocalDate()))
            target = LocalDate.ofYearDay(temporaltag.head().toLocalDate().getYear() - 1, day);
        LocalDateTime start = target.atStartOfDay();
        LocalDateTime end   = target.atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Day, temporaltag.distribution().between(start, end));
    }

}