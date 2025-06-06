package software.siani.orchilla.model.operators.events;

import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;
import systems.intino.datamarts.subjectstore.SubjectStore;

public record SetEventTemporalOperator(String event, SubjectStore subjectStore) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        return null;
    }
}
