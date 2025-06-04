package software.siani.orchilla.operators;

import org.junit.Test;
import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.distributions.ConstantDistribution;
import software.siani.orchilla.operators.decade.AddDecadeTemporalOperator;
import software.siani.orchilla.operators.decade.SetDecadeTemporalOperator;
import software.siani.orchilla.operators.decade.SubDecadeTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class DecadeOperatorsTest {

    @Test
    public void add_two_decades() {
         assertThat(new AddDecadeTemporalOperator(2).computeFor(event()))
                 .isEqualTo(new TemporalTag(
                         event().head().plusYears(20),
                         event().tail().plusYears(20),
                         Period.Decade,
                         new ConstantDistribution(0, 315532800)));
    }

    @Test
    public void sub_two_decades() {
        assertThat(new SubDecadeTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusYears(20),
                        event().tail().minusYears(20),
                        Period.Decade,
                        new ConstantDistribution(0, 315532800)));
    }

    @Test
    public void set_twenties() {
        LocalDateTime twenties = LocalDateTime.of(1920, 1, 1, 0, 0);
        assertThat(new SetDecadeTemporalOperator(20).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        twenties,
                        twenties.plusYears(10).minusNanos(1),
                        Period.Decade,
                        new ConstantDistribution(0, 315619199)));
    }


    private static TemporalTag event() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2035, 1, 1, 0, 0),
                Period.Decade,
                new ConstantDistribution(0, 0));
    }
}
