package software.siani.orchilla.model.operators;


import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.operators.millennium.AddMillenniumTemporalOperator;
import software.siani.orchilla.model.operators.millennium.SubMillenniumTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MillenniumOperatorsTest {
    @Test
    public void addTwoMillennium() {
        assertThat(new AddMillenniumTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusYears(2000),
                        event().tail().plusYears(2000),
                        Period.Millennium,
                        new ConstantDistribution(0, 0)
                ));
    }

    @Test
    public void subTwoMillennium() {
        assertThat(new SubMillenniumTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusYears(2000),
                        event().tail().minusYears(2000),
                        Period.Millennium,
                        new ConstantDistribution(0, 0)
                ));
    }

    private static TemporalTag event() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                Period.Millennium,
                new ConstantDistribution(0, 0));
    }
}
