package software.siani.orchilla.model.functions.granularity;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

public record SecondGranularityFunction() implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime headDateTime = temporaltag.head();
        LocalDateTime start = headDateTime.withNano(0);
        LocalDateTime end = start.withNano(999_999_999);
        return new TemporalTag(start, end, Period.Second, temporaltag.distribution().between(start, end));
    }
}
