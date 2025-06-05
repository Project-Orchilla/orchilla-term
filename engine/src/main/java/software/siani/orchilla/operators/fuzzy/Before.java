package software.siani.orchilla.operators.fuzzy;

import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.distributions.GeneralizedBetaDistribution;
import software.siani.orchilla.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.operators.fuzzy.Utils.unitValue;

public record Before() implements TemporalOperator {
    private static final double Alpha = 1.0;
    private static final double Beta  = 2.0;

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        Period period = temporaltag.period();
        double lower = unitValue(applyUnits(temporaltag.head(), period), period);
        double upper = unitValue(temporaltag.head(), period);
        return new TemporalTag(
                applyUnits(temporaltag.head(), period),
                temporaltag.head(),
                period,
                new GeneralizedBetaDistribution(lower, upper, Alpha, Beta)
        );
    }

    private LocalDateTime applyUnits(LocalDateTime dt, Period period) {
        return switch (period) {
            case Second -> dt.plusSeconds(-1);
            case Minute -> dt.plusSeconds(-1 * 30L);
            case Hour -> dt.plusMinutes(-1 * 15L);
            case Day -> dt.plusHours(-1 * 4L);
            case Week -> dt.plusDays(-1 * 2L);
            case Month -> dt.plusDays(-1 * 7L);
            case Year -> dt.plusMonths(-1 * 2L);
            case Decade -> dt.plusYears(-1 * 2L);
            case Century -> dt.plusYears(-1 * 20L);
            case Millennium -> dt.plusYears(-1 * 200L);
        };
    }
}
