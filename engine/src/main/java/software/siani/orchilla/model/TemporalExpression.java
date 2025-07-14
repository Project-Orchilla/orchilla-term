package software.siani.orchilla.model;

import software.siani.orchilla.model.functions.CompositeTemporalFunction;
import software.siani.orchilla.model.functions.TemporalFunction;
import systems.intino.datamarts.subjectstore.SubjectStore;

import java.util.List;
import java.util.Objects;

public class TemporalExpression {
    private TemporalTag context;
    private final CompositeTemporalFunction operator;

    public TemporalExpression(TemporalTag context, CompositeTemporalFunction operator) {
        this.context = context;
        this.operator = operator;
    }

    public TemporalTag solve() {
        if (context == null)
            throw new RuntimeException("To solve the temporal expression you need to have a context, right now it is null.");
        return operator.computeFor(context);
    }

    public TemporalExpression addContext(TemporalTag context) {
        this.context = context;
        return this;
    }

    public TemporalTag context() {
        return context;
    }

    public List<TemporalFunction> operators() {
        return operator.temporalFunctions();
    }

    public TemporalFunction operator(int index) {
        return operator.temporalFunctions().get(index);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TemporalExpression that = (TemporalExpression) o;
        return Objects.equals(context, that.context) && Objects.equals(operator, that.operator);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(context);
        result = 31 * result + Objects.hashCode(operator);
        return result;
    }

    @Override
    public String toString() {
        return "TemporalExpression{" +
                "context=" + context +
                ", predicates=" + operator +
                '}';
    }

    public static class Builder {
        private SubjectStore subjectStore;

        public Builder with(SubjectStore subjectStore) {
            this.subjectStore = subjectStore;
            return this;
        }

        public TemporalExpression build(String string) {
            return new TemporalExpressionParser(this.subjectStore).parse(string);
        }
    }
}
