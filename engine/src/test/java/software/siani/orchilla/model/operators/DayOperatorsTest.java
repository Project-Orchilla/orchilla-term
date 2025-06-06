package software.siani.orchilla.model.operators;


import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.operators.day.AddDayTemporalOperator;
import software.siani.orchilla.model.operators.day.SubDayTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DayOperatorsTest {

    @Test
    public void add_two_days() {
        assertThat(new AddDayTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusDays(2),
                        event().tail().plusDays(2),
                        Period.Day,
                        new ConstantDistribution(0, 0)));
    }

    @Test
    public void minus_two_days() {
        assertThat(new SubDayTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusDays(2),
                        event().tail().minusDays(2),
                        Period.Day,
                        new ConstantDistribution(0, 0)));
    }

    private static TemporalTag event() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                Period.Day,
                new ConstantDistribution(0, 0));
    }
}
