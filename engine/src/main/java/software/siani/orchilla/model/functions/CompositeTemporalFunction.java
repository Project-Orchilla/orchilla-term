package software.siani.orchilla.model.functions;

import software.siani.orchilla.model.TemporalTag;

import java.util.List;

public record CompositeTemporalFunction(List<TemporalFunction> temporalFunctions) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        TemporalTag result = temporaltag;
        for (TemporalFunction temporalFunction : temporalFunctions)
            result = temporalFunction.computeFor(result);
        return result;
    }
}
