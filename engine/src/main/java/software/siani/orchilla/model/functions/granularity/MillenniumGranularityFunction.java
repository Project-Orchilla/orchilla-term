package software.siani.orchilla.model.functions.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record MillenniumGranularityFunction() implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        int millenniumBase = (temporaltag.head().getYear() / 1000) * 1000;
        LocalDateTime start = LocalDate.of(millenniumBase, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(millenniumBase + 999, 12, 31).atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Millennium, temporaltag.distribution().between(start, end));
    }
}
