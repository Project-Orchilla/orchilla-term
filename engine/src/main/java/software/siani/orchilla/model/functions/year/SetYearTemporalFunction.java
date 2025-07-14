package software.siani.orchilla.model.functions.year;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

public record SetYearTemporalFunction(int year) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime start = yearOfPredicate();
        LocalDateTime end = yearOfPredicate().plusYears(1).minusNanos(1);
        return new TemporalTag(start, end, Period.Year, temporaltag.distribution().between(start, end));
    }

    private LocalDateTime yearOfPredicate() {
        return LocalDateTime.of(year, 1, 1, 0, 0);
    }
}
