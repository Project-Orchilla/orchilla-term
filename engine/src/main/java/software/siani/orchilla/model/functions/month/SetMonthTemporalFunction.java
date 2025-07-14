package software.siani.orchilla.model.functions.month;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;
import software.siani.orchilla.model.units.Month;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public record SetMonthTemporalFunction(Month month) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDate startDate = LocalDate.of(temporaltag.head().getYear(), month.ordinal() + 1, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        return new TemporalTag(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), Period.Month, temporaltag.distribution().between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)));
    }
}
