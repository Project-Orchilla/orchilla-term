package software.siani.orchilla.model.operators.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record DecadeGranularityOperator() implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        int decadeBase = (temporaltag.head().getYear() / 10) * 10;
        LocalDateTime start = LocalDate.of(decadeBase, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(decadeBase + 9, 12, 31).atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Decade, temporaltag.distribution().between(start, end));
    }
}
