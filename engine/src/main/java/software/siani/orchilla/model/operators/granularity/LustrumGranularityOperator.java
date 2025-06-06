package software.siani.orchilla.model.operators.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record LustrumGranularityOperator() implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        int lustrumBase = (temporaltag.head().getYear() / 5) * 5;
        LocalDateTime start = LocalDate.of(lustrumBase, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(lustrumBase + 4, 12, 31).atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Year, temporaltag.distribution().between(start, end));
    }
}
