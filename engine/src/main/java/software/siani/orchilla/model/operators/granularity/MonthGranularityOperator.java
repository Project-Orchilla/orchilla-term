package software.siani.orchilla.model.operators.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

public record MonthGranularityOperator() implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        YearMonth yearMonth = YearMonth.of(headDate.getYear(), headDate.getMonthValue());
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Month, temporaltag.distribution().between(start, end));
    }
}
