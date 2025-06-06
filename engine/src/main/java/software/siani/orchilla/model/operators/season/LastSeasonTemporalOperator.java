package software.siani.orchilla.model.operators.season;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;
import software.siani.orchilla.model.units.Season;

import java.time.*;

public record LastSeasonTemporalOperator(int value, Season season) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        LocalDate start = null;
        LocalDate end = null;
        LocalDate cursor = headDate;
        for (int i = 0; i < value; i++) {
            LocalDate[] window = lastWindowForSeason(cursor, season);
            start = window[0];
            end = window[1];
            cursor = start.minusDays(1);
        }
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
        return new TemporalTag(startDateTime, endDateTime, Period.Month, temporaltag.distribution().between(startDateTime, endDateTime));
    }

    private LocalDate[] lastWindowForSeason(LocalDate date, Season season) {
        return switch (season) {
            case Spring -> lastSpringWindow(date);
            case Summer -> lastSummerWindow(date);
            case Autumn -> lastAutumnWindow(date);
            case Winter -> lastWinterWindow(date);
        };
    }

    private LocalDate[] lastSpringWindow(LocalDate date) {
        int year = date.getYear();
        LocalDate springEnd = LocalDate.of(year, Month.MAY, 31);
        if (!date.isAfter(springEnd)) {
            year--;
            springEnd = LocalDate.of(year, Month.MAY, 31);
        }
        LocalDate springStart = LocalDate.of(year, Month.MARCH, 1);
        return new LocalDate[]{ springStart, springEnd };
    }

    private LocalDate[] lastSummerWindow(LocalDate date) {
        int year = date.getYear();
        LocalDate summerEnd = LocalDate.of(year, Month.AUGUST, 31);
        if (!date.isAfter(summerEnd)) {
            year--;
            summerEnd = LocalDate.of(year, Month.AUGUST, 31);
        }
        LocalDate summerStart = LocalDate.of(year, Month.JUNE, 1);
        return new LocalDate[]{ summerStart, summerEnd };
    }

    private LocalDate[] lastAutumnWindow(LocalDate date) {
        int year = date.getYear();
        LocalDate autumnEnd = LocalDate.of(year, Month.NOVEMBER, 30);
        if (!date.isAfter(autumnEnd)) {
            year--;
            autumnEnd = LocalDate.of(year, Month.NOVEMBER, 30);
        }
        LocalDate autumnStart = LocalDate.of(year, Month.SEPTEMBER, 1);
        return new LocalDate[]{ autumnStart, autumnEnd };
    }

    private LocalDate[] lastWinterWindow(LocalDate date) {
        int year = date.getYear();
        LocalDate febEnd = LocalDate.of(year, Month.FEBRUARY, Year.isLeap(year) ? 29 : 28);
        if (date.isAfter(febEnd)) {
            LocalDate start = LocalDate.of(year, Month.JANUARY, 1);
            return new LocalDate[]{ start, febEnd };
        } else {
            year--;
            LocalDate decStart = LocalDate.of(year, Month.DECEMBER, 1);
            LocalDate decEnd = LocalDate.of(year, Month.DECEMBER, 31);
            return new LocalDate[]{ decStart, decEnd };
        }
    }
}
