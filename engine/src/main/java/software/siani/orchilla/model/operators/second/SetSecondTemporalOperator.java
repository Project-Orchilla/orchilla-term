package software.siani.orchilla.model.operators.second;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;

public record SetSecondTemporalOperator(int second) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime start = temporaltag.head()
                .withSecond(second)
                .withNano(0);
        LocalDateTime end = start.withNano(999_999_999);
        return new TemporalTag(start, end, Period.Second, null);
    }
}
