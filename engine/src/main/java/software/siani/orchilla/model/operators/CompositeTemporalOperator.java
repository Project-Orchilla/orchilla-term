package software.siani.orchilla.model.operators;

import software.siani.orchilla.model.TemporalTag;

import java.util.List;

public record CompositeTemporalOperator(List<TemporalOperator> temporalOperators) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        TemporalTag result = temporaltag;
        for (TemporalOperator temporalOperator : temporalOperators)
            result = temporalOperator.computeFor(result);
        return result;
    }
}
