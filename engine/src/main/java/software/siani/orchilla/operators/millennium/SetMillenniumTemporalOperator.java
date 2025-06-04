package software.siani.orchilla.operators.millennium;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.millennium.MillenniumConstants.YearsPerMillennium;

public record SetMillenniumTemporalOperator(int value) implements TemporalOperator {

    public TemporalTag computeFor(TemporalTag temporaltag) {
        int startYear = (value - 1) * YearsPerMillennium + 1;
        int endYear   = value * YearsPerMillennium;
        LocalDateTime start = LocalDateTime.of(startYear, 1, 1, 0, 0, 0, 0);
        LocalDateTime end   = LocalDateTime.of(endYear, 12, 31, 23, 59, 59, 999_999_999);
        return new TemporalTag(start, end, Period.Millennium, temporaltag.distribution().between(start, end));
    }
}
