package software.siani.orchilla.model.operators.century;

import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.operators.TemporalOperator;

import java.time.LocalDateTime;

import static software.siani.orchilla.model.operators.century.CenturyConstants.yearsIn;

public record SetCenturyTemporalOperator(int value) implements TemporalOperator {

    public TemporalTag computeFor(TemporalTag temporaltag) {
        LocalDateTime start = century();
        LocalDateTime end = century().plusYears(yearsIn(1)).minusNanos(1);
        return new TemporalTag(start, end, Period.Century, new ConstantDistribution(0 ,3155673599L));
    }

    private LocalDateTime century() {
        return LocalDateTime.of(value > 100 ? value : (value - 1) * 100, 1, 1, 0, 0);
    }
}
