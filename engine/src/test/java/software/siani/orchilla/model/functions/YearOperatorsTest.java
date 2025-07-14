package software.siani.orchilla.model.functions;


import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.functions.year.AddYearTemporalFunction;
import software.siani.orchilla.model.functions.year.SubYearTemporalFunction;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class YearOperatorsTest {

    @Test
    public void add_two_years() {
        assertThat(new AddYearTemporalFunction(2).computeFor(event()))
                .isEqualTo(new TemporalTag(
                        event().head().plusYears(2),
                        event().tail().plusYears(2),
                        Period.Year,
                        new ConstantDistribution(0, 0))
                );
    }

    @Test
    public void sub_two_years() {
        assertThat(new SubYearTemporalFunction(2).computeFor(event()))
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
