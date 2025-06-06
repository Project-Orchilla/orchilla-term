package software.siani.orchilla.model.operators.boundaries;

import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.operators.TemporalOperator;

public class Head implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        return new TemporalTag(
                temporaltag.head(),
                temporaltag.head(),
                temporaltag.period(),
                new ConstantDistribution(0, 0)
        );
    }
}