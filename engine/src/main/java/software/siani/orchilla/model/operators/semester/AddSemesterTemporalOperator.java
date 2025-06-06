package software.siani.orchilla.model.operators.semester;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.*;


public record AddSemesterTemporalOperator(int value) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate cursor = temporaltag.head().toLocalDate();
        LocalDate start = null;
        LocalDate end = null;
        for (int i = 0; i < value; i++) {
            LocalDate[] window = nextSemesterWindow(cursor);
            start = window[0];
            end   = window[1];
            cursor = end.plusDays(1);
        }
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime   = end.atTime(LocalTime.MAX);
        var distribution = temporaltag.distribution().between(startDateTime, endDateTime);
        return new TemporalTag(startDateTime, endDateTime, Period.Month, distribution);
    }


    private LocalDate[] nextSemesterWindow(LocalDate date) {
        int year = date.getYear();
        Month month = date.getMonth();

        if (month.getValue() <= 6) {
            LocalDate start = LocalDate.of(year, Month.JULY, 1);
            LocalDate end   = LocalDate.of(year, Month.DECEMBER, 31);
            return new LocalDate[]{ start, end };
        } else {
            int nextYear = year + 1;
            LocalDate start = LocalDate.of(nextYear, Month.JANUARY, 1);
            LocalDate end   = LocalDate.of(nextYear, Month.JUNE, 30);
            return new LocalDate[]{ start, end };
        }
    }
}
