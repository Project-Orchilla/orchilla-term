package software.siani.orchilla.model.operators.boundaries;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.operators.TemporalOperator;

public class Tail implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        return new TemporalTag(
                temporaltag.tail(),
                temporaltag.tail(),
                temporaltag.period(),
                new ConstantDistribution(0, 0)
        );
    }
}
