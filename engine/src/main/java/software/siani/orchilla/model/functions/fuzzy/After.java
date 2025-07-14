package software.siani.orchilla.model.functions.fuzzy;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.GeneralizedBetaDistribution;
import software.siani.orchilla.model.functions.TemporalFunction;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.functions.fuzzy.Utils.unitValue;

public record After() implements TemporalFunction {
    private static final double Alpha = 2.0;
    private static final double Beta  = 1.0;

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        Period period = temporaltag.period();
        double lower = unitValue(temporaltag.tail(), period);
        double upper = unitValue(applyUnits(temporaltag.tail(), period), period);
        return new TemporalTag(temporaltag.tail(),
                applyUnits(temporaltag.tail(), period),
                period,
                new GeneralizedBetaDistribution(lower, upper, Alpha, Beta)
        );
    }

    private LocalDateTime applyUnits(LocalDateTime dt, Period period) {
        return switch (period) {
            case Second -> dt.plusSeconds(1);
            case Minute -> dt.plusSeconds(30L);
            case Hour -> dt.plusMinutes(15L);
            case Day -> dt.plusHours(4L);
            case Week -> dt.plusDays(2L);
            case Month -> dt.plusDays(7L);
            case Year -> dt.plusMonths(2L);
            case Decade -> dt.plusYears(2L);
            case Century -> dt.plusYears(20L);
            case Millennium -> dt.plusYears(200L);
        };
    }
}
