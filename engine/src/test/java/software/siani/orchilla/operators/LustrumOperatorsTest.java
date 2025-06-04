package software.siani.orchilla.operators;


import org.junit.Test;
import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.distributions.ConstantDistribution;
import software.siani.orchilla.operators.lustrum.AddLustrumTemporalOperator;
import software.siani.orchilla.operators.lustrum.SubLustrumTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class LustrumOperatorsTest {

    @Test
    public void add_one_lustrum() {
        TemporalTag event = event();
        assertThat(new AddLustrumTemporalOperator(1).computeFor(event))
                .isEqualTo(new TemporalTag(
                        event.head().plusYears(5),
                        event.tail().plusYears(5),
                        Period.Year,
                        new ConstantDistribution(0, 0)
                        )
                );
    }

    @Test
    public void sub_one_lustrum() {
        TemporalTag event = event();
        assertThat(new SubLustrumTemporalOperator(1).computeFor(event))
                .isEqualTo(new TemporalTag(
                                event.head().minusYears(5),
                                event.tail().minusYears(5),
                                Period.Year,
                                new ConstantDistribution(0, 0)
                        )
                );
    }

    private static TemporalTag event() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                Period.Year,
                new ConstantDistribution(0, 0));
    }
}
