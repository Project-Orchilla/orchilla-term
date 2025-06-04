package software.siani.orchilla.operators.minute;

import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;

public record SetMinuteTemporalOperator(int minute) implements TemporalOperator {
    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        return null;
    }
}
