package software.siani.orchilla.model.operators;


import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.operators.hour.AddHourTemporalOperator;
import software.siani.orchilla.model.operators.hour.SubHourTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class HourOperatorsTest {

    @Test
    public void add_two_hours() {
        assertThat(new AddHourTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusHours(2),
                        event().tail().plusHours(2),
                        Period.Hour,
                        new ConstantDistribution(0, 0))
                );
    }

    @Test
    public void sub_two_hours() {
        assertThat(new SubHourTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusHours(2),
                        event().tail().minusHours(2),
                        Period.Hour,
                        new ConstantDistribution(0, 0))
                );
    }

    private static TemporalTag event() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                Period.Hour,
                new ConstantDistribution(0, 0));
    }
}
