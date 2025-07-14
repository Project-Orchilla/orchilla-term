package software.siani.orchilla.model.functions.events;

import software.siani.orchilla.model.TemporalExpression;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;
import systems.intino.datamarts.subjectstore.SubjectStore;

public record SetEventTemporalFunction(String event, SubjectStore subjectStore) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        String function = subjectStore.open(event).get("date");
        if (isInPython(function)) return temporaltag;
        if (isSet(function)) return new TemporalExpression.Builder().with(subjectStore).build("now>>" + function.trim()).solve();
        return new TemporalExpression.Builder().with(subjectStore).build(function).solve();
    }

    private boolean isSet(String date) {
        return date.startsWith("set");
    }

    private boolean isInPython(String date) {
        return date.startsWith("python");
    }
}
