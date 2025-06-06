package software.siani.orchilla.model.operators.fuzzy;

import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.GeneralizedBetaDistribution;
import software.siani.orchilla.model.operators.TemporalOperator;

import static software.siani.orchilla.model.operators.fuzzy.Utils.unitValue;

public record Late() implements TemporalOperator {
    private static final double Alpha = 2.0;
    private static final double Beta  = 1.0;

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
