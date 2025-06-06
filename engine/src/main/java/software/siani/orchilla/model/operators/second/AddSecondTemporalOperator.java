package software.siani.orchilla.model.operators.second;

import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;

public record AddSecondTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        if (temporaltag.period().compareTo(Period.Second) <= 0) {
            LocalDateTime start = temporaltag.head().plusSeconds(value);
            LocalDateTime end = temporaltag.tail().plusSeconds(value);
            return new TemporalTag(start, end, temporaltag.period(), temporaltag.distribution().between(start, end));
        }
        LocalDateTime start = temporaltag.head().plusSeconds(value);
        LocalDateTime end = temporaltag.tail().plusSeconds(value).plusSeconds(1).minusNanos(1);
        Distribution distribution = temporaltag.distribution().between(start, end);
        return new TemporalTag(start, end, Period.Second, distribution);
    }
}
