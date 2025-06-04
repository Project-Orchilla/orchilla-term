package software.siani.orchilla.operators.fuzzy;


import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.distributions.GeneralizedBetaDistribution;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.fuzzy.Utils.unitValue;

public final class Around implements TemporalOperator {
    private static final double Alpha = 2.0;
    private static final double Beta = 2.0;

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        Period period = temporaltag.period();
        LocalDateTime start = applyUnits(temporaltag.head(), period, -1);
        LocalDateTime end = applyUnits(temporaltag.tail(), period, 1);
        double lower = unitValue(start, period);
        double upper = unitValue(end, period);
        return new TemporalTag(
                start,
                end,
                period,
                new GeneralizedBetaDistribution(upper, lower, Alpha, Beta)
        );
    }

    private LocalDateTime applyUnits(LocalDateTime localDateTime, Period period, int amount) {
        return switch (period) {
            case Second -> localDateTime.plusSeconds(amount);
            case Minute -> localDateTime.plusSeconds(amount * 30L);
            case Hour -> localDateTime.plusMinutes(amount * 15L);
            case Day -> localDateTime.plusHours(amount * 4L);
            case Week -> localDateTime.plusDays(amount * 2L);
            case Month -> localDateTime.plusDays(amount * 7L);
            case Year -> localDateTime.plusMonths(amount * 2L);
            case Decade -> localDateTime.plusYears(amount * 2L);
            case Century -> localDateTime.plusYears(amount * 20L);
            case Millennium -> localDateTime.plusYears(amount * 200L);
        };
    }
}
