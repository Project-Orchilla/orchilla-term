package software.siani.orchilla.model.operators.season;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;
import software.siani.orchilla.model.units.Season;

import java.time.*;

public record SetSeasonTemporalOperator(Season season) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate headDate = temporaltag.head().toLocalDate();
        int year = headDate.getYear();
        LocalDate seasonStart = null;
        LocalDate seasonEnd = null;
        switch (season) {
            case Spring -> {
                seasonStart = LocalDate.of(year, Month.MARCH, 1);
                seasonEnd   = LocalDate.of(year, Month.MAY, 31);
            }
            case Summer -> {
                seasonStart = LocalDate.of(year, Month.JUNE, 1);
                seasonEnd   = LocalDate.of(year, Month.AUGUST, 31);
            }
            case Autumn -> {
                seasonStart = LocalDate.of(year, Month.SEPTEMBER, 1);
                seasonEnd   = LocalDate.of(year, Month.NOVEMBER, 30);
            }
            case Winter -> {
                java.time.Month m = headDate.getMonth();
                if (m == Month.JANUARY || m == Month.FEBRUARY) {
                    seasonStart = LocalDate.of(year, Month.JANUARY, 1);
                    seasonEnd   = LocalDate.of(year, Month.FEBRUARY,
                            Year.isLeap(year) ? 29 : 28);
                } else if (m == Month.DECEMBER) {
                    seasonStart = LocalDate.of(year, Month.DECEMBER, 1);
                    seasonEnd   = LocalDate.of(year, Month.DECEMBER, 31);
                } else {
                    seasonStart = LocalDate.of(year, Month.JANUARY, 1);
                    seasonEnd   = LocalDate.of(year, Month.FEBRUARY,
                            Year.isLeap(year) ? 29 : 28);
                }
            }
        }
        LocalDateTime startDateTime = seasonStart.atStartOfDay();
        LocalDateTime endDateTime   = seasonEnd.atTime(LocalTime.MAX);
        return new TemporalTag(startDateTime, endDateTime, Period.Month, temporaltag.distribution().between(startDateTime, endDateTime));
    }
}
