package software.siani.orchilla.model.functions.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CenturyGranularityFunction() implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        int centuryBase = (temporaltag.head().getYear() / 100) * 100;
        LocalDateTime start = LocalDate.of(centuryBase, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(centuryBase + 99, 12, 31).atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Century, temporaltag.distribution().between(start, end));
    }
}