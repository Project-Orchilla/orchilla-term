package software.siani.orchilla.operators;


import org.junit.Test;
import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.distributions.ConstantDistribution;
import software.siani.orchilla.operators.month.AddMonthTemporalOperator;
import software.siani.orchilla.operators.month.LastMonthTemporalOperator;
import software.siani.orchilla.operators.month.NextMonthTemporalOperator;
import software.siani.orchilla.operators.month.SubMonthTemporalOperator;
import software.siani.orchilla.units.Month;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MonthOperatorsTest {

    @Test
    public void add_two_months() {
        assertThat(new AddMonthTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusMonths(2),
                        event().tail().plusMonths(2),
                        Period.Month,
                        new ConstantDistribution(0, 0)
                ));
    }

    @Test
    public void sub_two_months() {
        assertThat(new SubMonthTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusMonths(2),
                        event().tail().minusMonths(2),
                        Period.Month,
                        new ConstantDistribution(0, 0)
                ));
    }

    @Test
    public void next_month() {
        assertThat(new NextMonthTemporalOperator(Month.Apr).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusMonths(1),
                        event().tail().plusMonths(2).minusNanos(1),
                        Period.Month,
                        new ConstantDistribution(0, 2419199)
                ));
    }

    @Test
    public void last_month() {
        assertThat(new LastMonthTemporalOperator(Month.Apr).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusMonths(1),
                        event().tail().minusNanos(1),
                        Period.Month,
                        new ConstantDistribution(0, 2678399)
                ));
    }

    private static TemporalTag event() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                Period.Month,
                new ConstantDistribution(0, 0));
    }
}
