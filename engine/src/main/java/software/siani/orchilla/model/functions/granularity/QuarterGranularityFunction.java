package software.siani.orchilla.model.functions.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

public record QuarterGranularityFunction() implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        int startMonth = (temporaltag.head().toLocalDate().getMonthValue() - 1) / 3 * 3 + 1;
        int endMonth = startMonth + 2;
        int year = temporaltag.head().toLocalDate().getYear();
        YearMonth startYm = YearMonth.of(year, startMonth);
        YearMonth endYm = YearMonth.of(year, endMonth);
        LocalDateTime start = startYm.atDay(1).atStartOfDay();
        LocalDateTime end = endYm.atEndOfMonth().atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Year, temporaltag.distribution().between(start, end));
    }
}
