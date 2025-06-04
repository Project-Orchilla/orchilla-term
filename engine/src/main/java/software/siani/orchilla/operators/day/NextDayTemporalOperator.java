package software.siani.orchilla.operators.day;
import software.siani.orchilla.Distribution;
import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record NextDayTemporalOperator(int day) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate target = LocalDate.ofYearDay(temporaltag.head().toLocalDate().getYear(), day);
        if (!target.isAfter(temporaltag.head().toLocalDate()))
            target = LocalDate.ofYearDay(temporaltag.head().toLocalDate().getYear() + 1, day);
        LocalDateTime start = target.atStartOfDay();
        LocalDateTime end   = target.atTime(LocalTime.MAX);
        Distribution distribution = temporaltag
                .distribution()
                .between(start, end);
        return new TemporalTag(start, end, Period.Day, distribution);
    }
}
