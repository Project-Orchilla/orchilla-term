package software.siani.orchilla.model.functions.events;

import software.siani.orchilla.model.TemporalExpression;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.TemporalFunction;
import software.siani.orchilla.model.functions.month.AddMonthTemporalFunction;
import software.siani.orchilla.model.functions.year.AddYearTemporalFunction;
import systems.intino.datamarts.subjectstore.SubjectStore;

public record NextWithArgumentsEventTemporalFunction(int value, String event, SubjectStore subjectStore) implements TemporalFunction {

    @Override
    public TemporalTag computeFor(TemporalTag temporaltag) {
        String function = subjectStore.open(event).get("date");
        String dependence = subjectStore.open(event).get("dependence");
        if (isInPython(function)) return temporaltag;
        if (isSet(function)) {
            TemporalTag result = new TemporalExpression.Builder().with(subjectStore).build("now>>" + function.trim()).solve();
            if (dependence.equals("Year")) return new AddYearTemporalFunction(1).computeFor(result);
            if (dependence.equals("Month")) return new AddMonthTemporalFunction(1).computeFor(result);
            return result;
        }
        TemporalTag result = new TemporalExpression.Builder().with(subjectStore).build(function).solve();
        if (dependence.equals("Year")) return new AddYearTemporalFunction(1).computeFor(result);
        if (dependence.equals("Month")) return new AddMonthTemporalFunction(1).computeFor(result);
        return result;
    }

    private boolean isSet(String date) {
        return date.startsWith("set");
    }

    private boolean isInPython(String date) {
        return date.startsWith("python");
    }
}
