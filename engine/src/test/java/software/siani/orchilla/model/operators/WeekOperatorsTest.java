package software.siani.orchilla.model.operators;


import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.operators.week.AddWeekTemporalOperator;
import software.siani.orchilla.model.operators.week.SubWeekTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class WeekOperatorsTest {

    @Test
    public void add_two_weeks() {
        assertThat(new AddWeekTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusDays(14),
                        event().tail().plusDays(14),
                        Period.Week,
                        new ConstantDistribution(0, 0))
                );
    }

    @Test
    public void sub_two_weeks() {
        assertThat(new SubWeekTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusDays(14),
                        event().tail().minusDays(14),
                        Period.Week,
                        new ConstantDistribution(0, 0))
                );
    }

    private static TemporalTag event() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                Period.Week,
                new ConstantDistribution(0, 0));
    }
}
