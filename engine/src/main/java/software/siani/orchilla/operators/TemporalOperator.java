package software.siani.orchilla.operators;
import software.siani.orchilla.TemporalTag;

public interface TemporalOperator {
    TemporalTag computeFor(TemporalTag temporaltag);
}
