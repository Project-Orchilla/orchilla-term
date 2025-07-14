package software.siani.orchilla.model.functions;


import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.functions.second.AddSecondTemporalFunction;
import software.siani.orchilla.model.functions.second.SubSecondTemporalFunction;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class SecondOperatorsTest {

    @Test
    public void add_two_minutes() {
        assertThat(new AddSecondTemporalFunction(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusSeconds(2),
                        event().tail().plusSeconds(2),
                        Period.Second,
                        new ConstantDistribution(0, 0))
                );
    }

    @Test
    public void sub_two_minutes() {
        assertThat(new SubSecondTemporalFunction(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusSeconds(2),
                        event().tail().minusSeconds(2),
                        Period.Second,
                        new ConstantDistribution(0, 0))
                );
    }

    private static TemporalTag event() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                Period.Second,
                new ConstantDistribution(0, 0));
    }
}
