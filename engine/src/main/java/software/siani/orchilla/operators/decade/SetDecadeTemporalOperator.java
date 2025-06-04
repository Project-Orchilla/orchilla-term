package software.siani.orchilla.operators.decade;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.decade.DecadeConstants.yearsIn;

public record SetDecadeTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime start = decade();
        LocalDateTime end = decade().plusYears(yearsIn(1)).minusNanos(1);
        return new TemporalTag(start, end, Period.Decade, temporaltag.distribution().between(start, end));
    }

    private LocalDateTime decade() {
        return LocalDateTime.of(1900 + value, 1, 1, 0, 0);
    }
}
