package software.siani.orchilla.model.functions;

import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.functions.halfhour.AddHalfHourTemporalFunction;
import software.siani.orchilla.model.functions.halfhour.SubHalfHourTemporalFunction;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class HalfOperatorsTest {

    @Test
    public void add_two_halves_an_hour() {
        assertThat(new AddHalfHourTemporalFunction(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusMinutes(60),
                        event().tail().plusMinutes(60),
                        Period.Minute,
                        new ConstantDistribution(0, 0)
                ));
    }

    @Test
    public void sub_two_halves_an_hour() {
        assertThat(new SubHalfHourTemporalFunction(2).computeFor(event()))
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
