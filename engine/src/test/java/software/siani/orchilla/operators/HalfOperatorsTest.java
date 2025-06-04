package software.siani.orchilla.operators;

import org.junit.Test;
import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.distributions.ConstantDistribution;
import software.siani.orchilla.operators.half.AddHalfTemporalOperator;
import software.siani.orchilla.operators.half.SubHalfTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class HalfOperatorsTest {

    @Test
    public void add_two_halves_an_hour() {
        assertThat(new AddHalfTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusMinutes(60),
                        event().tail().plusMinutes(60),
                        Period.Minute,
                        new ConstantDistribution(0, 0)
                ));
    }

    @Test
    public void sub_two_halves_an_hour() {
        assertThat(new SubHalfTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusMinutes(60),
                        event().tail().minusMinutes(60),
                        Period.Minute,
                        new ConstantDistribution(0, 0)
                ));
    }

    private static TemporalTag event() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                Period.Minute,
                new ConstantDistribution(0, 0));
    }
}
