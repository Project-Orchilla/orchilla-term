package software.siani.orchilla.model.operators;


import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.operators.minute.AddMinuteTemporalOperator;
import software.siani.orchilla.model.operators.minute.SubMinuteTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MinuteOperatorsTest {

    @Test
    public void add_two_minutes() {
        assertThat(new AddMinuteTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusMinutes(2),
                        event().tail().plusMinutes(2),
                        Period.Minute,
                        new ConstantDistribution(0, 0))
                );
    }

    @Test
    public void sub_two_minutes() {
        assertThat(new SubMinuteTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusMinutes(2),
                        event().tail().minusMinutes(2),
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
