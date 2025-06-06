package software.siani.orchilla.model.operators.quarter;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.*;

public record AddQuarterTemporalOperator() implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        int year = temporaltag.head().toLocalDate().getYear();
        int nextQuarterIndex = (temporaltag.head().toLocalDate().getMonthValue() - 1) / 3 + 1;
        if (nextQuarterIndex > 3) {
            nextQuarterIndex = 0;
            year += 1;
        }
        int startMonth = nextQuarterIndex * 3 + 1;
        LocalDateTime start = YearMonth.of(year, startMonth).atDay(1).atStartOfDay();
        LocalDateTime end = YearMonth.of(year, Month.of(startMonth + 2).getValue()).atEndOfMonth().atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Month, temporaltag.distribution().between(start, end));
    }
}
