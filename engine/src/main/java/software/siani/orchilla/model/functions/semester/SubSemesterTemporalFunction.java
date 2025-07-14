package software.siani.orchilla.model.functions.semester;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.*;


public record SubSemesterTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate cursor = temporaltag.head().toLocalDate();
        LocalDate start = null;
        LocalDate end = null;
        for (int i = 0; i < value; i++) {
            LocalDate[] window = lastSemesterWindow(cursor);
            start = window[0];
            end   = window[1];
            cursor = start.minusDays(1);
        }
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime   = end.atTime(LocalTime.MAX);
        return new TemporalTag(startDateTime, endDateTime, Period.Month, temporaltag.distribution().between(startDateTime, endDateTime));
    }

    private LocalDate[] lastSemesterWindow(LocalDate date) {
        int year = date.getYear();
        Month month = date.getMonth();
        if (month.getValue() >= 7) {
            LocalDate start = LocalDate.of(year, Month.JANUARY, 1);
            LocalDate end   = LocalDate.of(year, Month.JUNE, 30);
            return new LocalDate[]{ start, end };
        } else {
            int prevYear = year - 1;
            LocalDate start = LocalDate.of(prevYear, Month.JULY, 1);
            LocalDate end   = LocalDate.of(prevYear, Month.DECEMBER, 31);
            return new LocalDate[]{ start, end };
        }
    }
}
