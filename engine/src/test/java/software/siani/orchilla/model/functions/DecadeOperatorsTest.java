package software.siani.orchilla.model.functions;

import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.functions.decade.AddDecadeTemporalFunction;
import software.siani.orchilla.model.functions.decade.SetDecadeTemporalFunction;
import software.siani.orchilla.model.functions.decade.SubDecadeTemporalFunction;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class DecadeOperatorsTest {

    @Test
    public void add_two_decades() {
         assertThat(new AddDecadeTemporalFunction(2).computeFor(event()))
                 .isEqualTo(new TemporalTag(
                         event().head().plusYears(20),
                         event().tail().plusYears(20),
                         Period.Decade,
                         new ConstantDistribution(0, 315532800)));
    }

    @Test
    public void sub_two_decades() {
        assertThat(new SubDecadeTemporalFunction(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusYears(20),
                        event().tail().minusYears(20),
                        Period.Decade,
                        new ConstantDistribution(0, 315532800)));
    }

    @Test
    public void set_twenties() {
        LocalDateTime twenties = LocalDateTime.of(1920, 1, 1, 0, 0);
        assertThat(new SetDecadeTemporalFunction(20).computeFor(event()))
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
