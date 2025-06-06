package software.siani.orchilla.model.operators;
import software.siani.orchilla.model.TemporalTag;

public interface TemporalOperator {
    TemporalTag computeFor(TemporalTag temporaltag);
}
