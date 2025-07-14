package software.siani.orchilla.model.functions.season;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;
import software.siani.orchilla.model.units.Season;

import java.time.*;

public record NextSeasonTemporalFunction(int value, Season season) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        LocalDate start = null;
        LocalDate end = null;
        LocalDate cursor = headDate;
        for (int i = 0; i < value; i++) {
            LocalDate[] window = nextWindowForSeason(cursor, season);
            start = window[0];
            end = window[1];
            cursor = end.plusDays(1);
        }
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
        return new TemporalTag(startDateTime, endDateTime, Period.Month, temporaltag.distribution().between(startDateTime, endDateTime));
    }

    private LocalDate[] nextWindowForSeason(LocalDate date, Season season) {
        return switch (season) {
            case Spring -> nextSpringWindow(date);
            case Summer -> nextSummerWindow(date);
            case Autumn -> nextAutumnWindow(date);
            case Winter -> nextWinterWindow(date);
        };
    }

    private LocalDate[] nextSpringWindow(LocalDate date) {
        int year = date.getYear();
        LocalDate springStart = LocalDate.of(year, Month.MARCH, 1);
        if (!date.isBefore(springStart)) {
            year++;
            springStart = LocalDate.of(year, Month.MARCH, 1);
        }
        LocalDate springEnd = LocalDate.of(year, Month.MAY, 31);
        return new LocalDate[]{ springStart, springEnd };
    }

    private LocalDate[] nextSummerWindow(LocalDate date) {
        int year = date.getYear();
        LocalDate summerStart = LocalDate.of(year, Month.JUNE, 1);
        if (!date.isBefore(summerStart)) {
            year++;
            summerStart = LocalDate.of(year, Month.JUNE, 1);
        }
        LocalDate summerEnd = LocalDate.of(year, Month.AUGUST, 31);
        return new LocalDate[]{ summerStart, summerEnd };
    }

    private LocalDate[] nextAutumnWindow(LocalDate date) {
        int year = date.getYear();
        LocalDate autumnStart = LocalDate.of(year, Month.SEPTEMBER, 1);
        if (!date.isBefore(autumnStart)) {
            year++;
            autumnStart = LocalDate.of(year, Month.SEPTEMBER, 1);
        }
        LocalDate autumnEnd = LocalDate.of(year, Month.NOVEMBER, 30);
        return new LocalDate[]{ autumnStart, autumnEnd };
    }

    private LocalDate[] nextWinterWindow(LocalDate date) {
        int year = date.getYear();
        LocalDate decStart = LocalDate.of(year, Month.DECEMBER, 1);
        if (!date.isBefore(decStart)) {
            year++;
            LocalDate janStart = LocalDate.of(year, Month.JANUARY, 1);
            int febLastDay = Year.isLeap(year) ? 29 : 28;
            LocalDate febEnd = LocalDate.of(year, Month.FEBRUARY, febLastDay);
            return new LocalDate[]{ janStart, febEnd };
        } else {
            LocalDate decEnd = LocalDate.of(year, Month.DECEMBER, 31);
            return new LocalDate[]{ decStart, decEnd };
        }
    }
}
