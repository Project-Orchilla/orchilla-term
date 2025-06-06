package software.siani.orchilla.model.operators.hour;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SetHourTemporalOperator(int hour) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        LocalDateTime start = headDate.atTime(hour, 0);
        LocalDateTime end = start.withMinute(59).withSecond(59).withNano(999_999_999);
        return new TemporalTag(start, end, Period.Hour, temporaltag.distribution().between(start, end));
    }
}
