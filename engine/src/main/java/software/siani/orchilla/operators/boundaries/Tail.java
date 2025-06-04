package software.siani.orchilla.operators.boundaries;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.distributions.ConstantDistribution;
import software.siani.orchilla.operators.TemporalOperator;

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
