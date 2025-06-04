package software.siani.orchilla.operators;

import org.junit.Test;
import software.siani.orchilla.Period;
import software.siani.orchilla.TemporalTag;
import software.siani.orchilla.distributions.ConstantDistribution;
import software.siani.orchilla.operators.century.AddCenturyTemporalOperator;
import software.siani.orchilla.operators.century.SetCenturyTemporalOperator;
import software.siani.orchilla.operators.century.SubCenturyTemporalOperator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CenturyOperatorsTest {

    @Test
    public void setTwentiethCentury() {
        SetCenturyTemporalOperator setCenturyPredicate = new SetCenturyTemporalOperator(20);
        assertThat(setCenturyPredicate.computeFor(timeTag()))
                .isEqualTo(twentiethCentury());
    }

    @Test
    public void addTwoCenturies() {
        AddCenturyTemporalOperator setCenturyPredicate = new AddCenturyTemporalOperator(2);
        TemporalTag nowEvent = timeTag();
        assertThat(setCenturyPredicate.computeFor(nowEvent))
                .isEqualTo(twentyThirdCentury(nowEvent));
    }

    @Test
    public void subTwoCenturies() {
        SubCenturyTemporalOperator subCenturyPredicate = new SubCenturyTemporalOperator(2);
        TemporalTag nowEvent = timeTag();
        assertThat(subCenturyPredicate.computeFor(nowEvent))
                .isEqualTo(twentiethCentury(nowEvent));
    }

    private static TemporalTag timeTag() {
        return new TemporalTag(
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                Period.Century,
                new ConstantDistribution(0 ,0));
    }

    private static TemporalTag twentiethCentury() {
        LocalDateTime startOfNineteenthCentury = LocalDateTime.of(1900, 1, 1, 0, 0);
        return new TemporalTag(
                startOfNineteenthCentury,
                startOfNineteenthCentury.plusYears(100).minusNanos(1),
                Period.Century,
                new ConstantDistribution(0 ,3155673599L)
        );
    }


    private static TemporalTag twentyThirdCentury(TemporalTag event) {
        return new TemporalTag(
                event.head().plusYears(200),
                event.tail().plusYears(200),
                Period.Century,
                event.distribution()
        );
    }

    private static TemporalTag twentiethCentury(TemporalTag event) {
        return new TemporalTag(
                event.head().minusYears(200),
                event.tail().minusYears(200),
                Period.Century,
                event.distribution()
        );
    }
}