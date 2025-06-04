package software.siani.orchilla.operators.events;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.operators.TemporalOperator;
import systems.intino.datamarts.subjectstore.SubjectStore;

public record SubEventTemporalOperator(int value, String event, SubjectStore subjectStore) implements TemporalOperator {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        return null;
    }
}
