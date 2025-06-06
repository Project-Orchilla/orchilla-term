package software.siani.orchilla.model.operators.minute;

import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;

public record SetMinuteTemporalOperator(int minute) implements TemporalOperator {
    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        return null;
    }
}
