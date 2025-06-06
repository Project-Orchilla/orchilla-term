package software.siani.orchilla.model.operators;

import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.operators.quarterhour.AddQuarterHourTemporalOperator;
import software.siani.orchilla.model.operators.quarterhour.SubQuarterHourTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class QuarterOperatorsTest {

    @Test
    public void add_two_quarters() {
        assertThat(new AddQuarterHourTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusMinutes(30),
                        event().tail().plusMinutes(30),
                        Period.Minute,
                        new ConstantDistribution(0, 0))
                );
    }

    @Test
    public void sub_two_quarters() {
        assertThat(new SubQuarterHourTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusMinutes(30),
                        event().tail().minusMinutes(30),
                        Period.Minute,
                        new ConstantDistribution(0, 0))
                );
    }

    private static TemporalTag event() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                Period.Minute,
                new ConstantDistribution(0, 0));
    }
}
