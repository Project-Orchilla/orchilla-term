package software.siani.orchilla.model.functions.fuzzy;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Afternoon() implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        LocalDateTime start = headDate.atTime(13, 0);
        LocalDateTime end = headDate.atTime(19, 59, 59, 999_999_999);
        return new TemporalTag(start, end, Period.Hour, temporaltag.distribution().between(start, end));
    }
}
