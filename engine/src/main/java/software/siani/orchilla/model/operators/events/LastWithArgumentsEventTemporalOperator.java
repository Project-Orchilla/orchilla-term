package software.siani.orchilla.model.operators.events;

import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.operators.TemporalOperator;
import systems.intino.datamarts.subjectstore.SubjectStore;

import java.util.HashMap;

public record LastWithArgumentsEventTemporalOperator(int value, String event, SubjectStore subjectStore) implements TemporalOperator {
    private static HashMap<String, String> events = new HashMap<>();

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        return temporaltag;
    }
}
