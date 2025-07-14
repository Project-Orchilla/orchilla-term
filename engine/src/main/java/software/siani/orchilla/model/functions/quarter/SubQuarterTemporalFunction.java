package software.siani.orchilla.model.functions.quarter;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.*;

public record SubQuarterTemporalFunction() implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        int year = temporaltag.head().toLocalDate().getYear();
        int prevQuarterIndex = (temporaltag.head().toLocalDate().getMonthValue() - 1) / 3 - 1;
        if (prevQuarterIndex < 0) {
            prevQuarterIndex = 3;
            year -= 1;
        }
        int startMonth = prevQuarterIndex * 3 + 1;
        LocalDateTime start = YearMonth.of(year, startMonth).atDay(1).atStartOfDay();
        LocalDateTime end = YearMonth.of(year, Month.of(startMonth + 2).getValue()).atEndOfMonth().atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Month, temporaltag.distribution().between(start, end));
    }
}
