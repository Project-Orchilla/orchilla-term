package software.siani.orchilla.operators.year;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

public record SetYearTemporalOperator(int year) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime start = yearOfPredicate();
        LocalDateTime end = yearOfPredicate().plusYears(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Year, null);
    }

    private LocalDateTime yearOfPredicate() {
        return LocalDateTime.of(year, 1, 1, 0, 0);
    }
}
