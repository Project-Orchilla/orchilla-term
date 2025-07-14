package software.siani.orchilla.model.functions;
import software.siani.orchilla.model.TemporalTag;

public interface TemporalFunction {
    TemporalTag computeFor(TemporalTag temporaltag);
}
