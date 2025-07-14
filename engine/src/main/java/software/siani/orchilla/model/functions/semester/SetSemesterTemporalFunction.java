package software.siani.orchilla.model.functions.semester;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.*;

public record SetSemesterTemporalFunction(int value) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        LocalDate[] window = semesterWindow(getYear(headDate), clampSemester(value));
        LocalDateTime startDateTime = window[0].atStartOfDay();
        LocalDateTime endDateTime   = window[1].atTime(LocalTime.MAX);
        return new TemporalTag(startDateTime, endDateTime, Period.Month, temporaltag.distribution().between(startDateTime, endDateTime));
    }

    private static int getYear(LocalDate headDate) {
        int year = headDate.getYear();
        return year;
    }

    private int clampSemester(int sem) {
        return (sem == 1 || sem == 2) ? sem : 1;
    }

    private LocalDate[] semesterWindow(int year, int semester) {
        if (semester == 1) {
            LocalDate start = LocalDate.of(year, Month.JANUARY, 1);
            LocalDate end   = LocalDate.of(year, Month.JUNE, 30);
            return new LocalDate[]{ start, end };
        } else {
            LocalDate start = LocalDate.of(year, Month.JULY, 1);
            LocalDate end   = LocalDate.of(year, Month.DECEMBER, 31);
            return new LocalDate[]{ start, end };
        }
    }
}
