package software.siani.orchilla.model.operators.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record DayGranularityOperator() implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime start = temporaltag.head().toLocalDate().atStartOfDay();
        LocalDateTime end = temporaltag.head().toLocalDate().atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Day, temporaltag.distribution().between(start, end));
    }
}
