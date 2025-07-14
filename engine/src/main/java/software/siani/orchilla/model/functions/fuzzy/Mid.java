package software.siani.orchilla.model.functions.fuzzy;

import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.GeneralizedBetaDistribution;
import software.siani.orchilla.model.functions.TemporalFunction;

import static software.siani.orchilla.model.functions.fuzzy.Utils.unitValue;


public record Mid() implements TemporalFunction {
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
