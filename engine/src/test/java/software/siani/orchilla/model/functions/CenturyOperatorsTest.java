package software.siani.orchilla.model.functions;

import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.functions.century.AddCenturyTemporalFunction;
import software.siani.orchilla.model.functions.century.SetCenturyTemporalFunction;
import software.siani.orchilla.model.functions.century.SubCenturyTemporalFunction;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CenturyOperatorsTest {

    @Test
    public void setTwentiethCentury() {
        SetCenturyTemporalFunction setCenturyPredicate = new SetCenturyTemporalFunction(20);
        assertThat(setCenturyPredicate.computeFor(timeTag()))
                .isEqualTo(twentiethCentury());
    }

    @Test
    public void addTwoCenturies() {
        AddCenturyTemporalFunction setCenturyPredicate = new AddCenturyTemporalFunction(2);
        TemporalTag nowEvent = timeTag();
        assertThat(setCenturyPredicate.computeFor(nowEvent))
                .isEqualTo(twentyThirdCentury(nowEvent));
    }

    @Test
    public void subTwoCenturies() {
        SubCenturyTemporalFunction subCenturyPredicate = new SubCenturyTemporalFunction(2);
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