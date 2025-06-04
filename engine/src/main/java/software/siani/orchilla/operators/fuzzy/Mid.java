package software.siani.orchilla.operators.fuzzy;

import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.distributions.GeneralizedBetaDistribution;
import software.siani.orchilla.operators.TemporalOperator;

import static software.siani.orchilla.operators.fuzzy.Utils.unitValue;


public record Mid() implements TemporalOperator {
    private static final double Alpha = 2.0;
    private static final double Beta = 2.0;

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        double lower = unitValue(temporaltag.head(), temporaltag.period());
        double upper = unitValue(temporaltag.tail(), temporaltag.period());
        return new TemporalTag(
                temporaltag.head(),
                temporaltag.tail(),
                temporaltag.period(),
                new GeneralizedBetaDistribution(lower, upper, Alpha, Beta)
        );
    }
}
