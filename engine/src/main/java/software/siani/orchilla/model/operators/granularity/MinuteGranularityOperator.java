package software.siani.orchilla.model.operators.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;

public record MinuteGranularityOperator() implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime start = temporaltag.head().withSecond(0).withNano(0);
        LocalDateTime end = start.withSecond(59).withNano(999_999_999);
        return new TemporalTag(start, end, Period.Minute, temporaltag.distribution().between(start, end));
    }
}
