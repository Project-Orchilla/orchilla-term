package software.siani.orchilla.model.operators.weekend;

import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.*;

public record SetWeekendOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        YearMonth yearMonth = YearMonth.of(headDate.getYear(), headDate.getMonthValue());
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int daysUntilSaturday = (DayOfWeek.SATURDAY.getValue() - firstOfMonth.getDayOfWeek().getValue() + 7) % 7;
        LocalDate firstSaturday = firstOfMonth.plusDays(daysUntilSaturday);
        LocalDate nthSaturday = firstSaturday.plusWeeks(value - 1);
        if (nthSaturday.getMonthValue() != yearMonth.getMonthValue()) {
            LocalDate lastOfMonth = yearMonth.atEndOfMonth();
            int offsetBackToSaturday = (lastOfMonth.getDayOfWeek().getValue() - DayOfWeek.SATURDAY.getValue() + 7) % 7;
            nthSaturday = lastOfMonth.minusDays(offsetBackToSaturday);
        }
        LocalDate saturday = nthSaturday;
        LocalDate sunday = saturday.plusDays(1);
        LocalDateTime start = saturday.atStartOfDay();
        LocalDateTime end = sunday.atTime(LocalTime.MAX);
        Distribution distribution = temporaltag.distribution().between(start, end);
        return new TemporalTag(start, end, Period.Day, distribution);
    }
}