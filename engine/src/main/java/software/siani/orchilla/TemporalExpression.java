package software.siani.orchilla;

import software.siani.orchilla.operators.CompositeTemporalOperator;
import software.siani.orchilla.operators.TemporalOperator;

import java.util.List;
import java.util.Objects;

public class TemporalExpression {
    private final TemporalTag context;
    private final CompositeTemporalOperator operator;

    public TemporalExpression(TemporalTag context, CompositeTemporalOperator operator) {
        this.context = context;
        this.operator = operator;
    }

    public TemporalTag context() {
        return context;
    }

    public TemporalTag solve() {
        return operator.computeFor(context);
    }

    public List<TemporalOperator> operators() {
        return operator.temporalOperators();
    }

    public TemporalOperator operator(int index) {
        return operator.temporalOperators().get(index);
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
}
