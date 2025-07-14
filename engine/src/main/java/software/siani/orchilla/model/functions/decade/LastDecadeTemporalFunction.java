package software.siani.orchilla.model.functions.decade;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record LastDecadeTemporalFunction(int decade) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        int startYear = (temporaltag.head().getYear() / 100) * 100 + decade;
        if (startYear > temporaltag.head().getYear()) startYear -= 100;
        LocalDateTime start = LocalDate.of(startYear, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(startYear + 9, 12, 31).atTime(LocalTime.MAX);
        return new TemporalTag(start, end, Period.Decade, temporaltag.distribution().between(start, end));
    }
}







