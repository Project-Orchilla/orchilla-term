package software.siani.orchilla.model.functions.decade;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.functions.decade.DecadeConstants.yearsIn;

public record SetDecadeTemporalFunction(int value) implements TemporalFunction {

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
