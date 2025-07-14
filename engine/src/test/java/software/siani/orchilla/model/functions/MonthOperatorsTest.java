package software.siani.orchilla.model.functions;


import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.functions.month.AddMonthTemporalFunction;
import software.siani.orchilla.model.functions.month.LastMonthTemporalFunction;
import software.siani.orchilla.model.functions.month.NextMonthTemporalFunction;
import software.siani.orchilla.model.functions.month.SubMonthTemporalFunction;
import software.siani.orchilla.model.units.Month;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MonthOperatorsTest {

    @Test
    public void add_two_months() {
        assertThat(new AddMonthTemporalFunction(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusMonths(2),
                        event().tail().plusMonths(2),
                        Period.Month,
                        new ConstantDistribution(0, 0)
                ));
    }

    @Test
    public void sub_two_months() {
        assertThat(new SubMonthTemporalFunction(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusMonths(2),
                        event().tail().minusMonths(2),
                        Period.Month,
                        new ConstantDistribution(0, 0)
                ));
    }

    @Test
    public void next_month() {
        assertThat(new NextMonthTemporalFunction(Month.Apr).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusMonths(1),
                        event().tail().plusMonths(2).minusNanos(1),
                        Period.Month,
                        new ConstantDistribution(0, 2419199)
                ));
    }

    @Test
    public void last_month() {
        assertThat(new LastMonthTemporalFunction(Month.Apr).computeFor(event()))
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
