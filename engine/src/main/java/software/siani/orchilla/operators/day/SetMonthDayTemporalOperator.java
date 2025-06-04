package software.siani.orchilla.operators.day;
import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record SetMonthDayTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime anchor = temporaltag.head();
        try {
            LocalDate targetDate = LocalDate.of(anchor.getYear(), anchor.getMonthValue(), value);
            LocalDateTime start = targetDate.atStartOfDay();
            LocalDateTime end   = targetDate.atTime(LocalTime.MAX);
            return new TemporalTag(
                    start,
                    end,
                    Period.Day,
                    temporaltag.distribution().between(start, end)
            );
        } catch (DateTimeException ex) {
            throw new IllegalArgumentException(
                    "Invalid day-of-month " + value + " for " + anchor.getYear() + "-" + anchor.getMonthValue(), ex);
        }
    }
}
