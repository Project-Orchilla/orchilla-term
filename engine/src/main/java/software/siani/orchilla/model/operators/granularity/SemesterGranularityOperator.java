package software.siani.orchilla.model.operators.granularity;

import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public record SemesterGranularityOperator() implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        int year = headDate.getYear();
        LocalDateTime start;
        LocalDateTime end;
        if (headDate.getMonthValue() <= 6) {
            start = LocalDate.of(year, Month.JANUARY, 1).atStartOfDay();
            end = LocalDate.of(year, Month.JUNE, 30).atTime(LocalTime.MAX);
        } else {
            start = LocalDate.of(year, Month.JULY, 1).atStartOfDay();
            end = LocalDate.of(year, Month.DECEMBER, 31).atTime(LocalTime.MAX);
        }
        Distribution distribution = temporaltag.distribution().between(start, end);
        return new TemporalTag(start, end, Period.Month, distribution);
    }
}
