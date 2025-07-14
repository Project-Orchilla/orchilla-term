package software.siani.orchilla.model.functions.fuzzy;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Night() implements TemporalFunction {
    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        LocalDateTime start = headDate.atTime(20, 0);
        LocalDateTime end = headDate.atTime(23, 59, 59, 999_999_999);
        return new TemporalTag(start, end, Period.Hour, temporaltag.distribution().between(start, end));
    }
}
