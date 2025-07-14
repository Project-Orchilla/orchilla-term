package software.siani.orchilla.model.functions.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public record YearGranularityFunction() implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        int year = headDate.getYear();
        LocalDateTime start = LocalDate.of(year, Month.JANUARY, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, Month.DECEMBER, 31).atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Year, temporaltag.distribution().between(start, end));
    }
}
