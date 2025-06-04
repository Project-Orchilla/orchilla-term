package software.siani.orchilla.operators;


import org.junit.Test;
import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.distributions.ConstantDistribution;
import software.siani.orchilla.operators.year.AddYearTemporalOperator;
import software.siani.orchilla.operators.year.SubYearTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class YearOperatorsTest {

    @Test
    public void add_two_years() {
        assertThat(new AddYearTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusYears(2),
                        event().tail().plusYears(2),
                        Period.Year,
                        new ConstantDistribution(0, 0))
                );
    }

    @Test
    public void sub_two_years() {
        assertThat(new SubYearTemporalOperator(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().minusYears(2),
                        event().tail().minusYears(2),
                        Period.Year,
                        new ConstantDistribution(0, 0))
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
