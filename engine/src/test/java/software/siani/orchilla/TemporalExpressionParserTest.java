package software.siani.orchilla;

import org.junit.Test;
import software.siani.orchilla.operators.day.AddDayTemporalOperator;
import software.siani.orchilla.operators.day.NextDayTemporalOperator;
import software.siani.orchilla.operators.day.SetOrdinalDayTemporalOperator;
import software.siani.orchilla.operators.fuzzy.Early;
import software.siani.orchilla.operators.fuzzy.Late;
import software.siani.orchilla.operators.fuzzy.Mid;
import software.siani.orchilla.operators.month.AddMonthTemporalOperator;
import software.siani.orchilla.operators.month.NextMonthTemporalOperator;
import software.siani.orchilla.operators.month.SetMonthTemporalOperator;
import software.siani.orchilla.operators.week.AddWeekTemporalOperator;
import software.siani.orchilla.operators.year.SetYearTemporalOperator;
import software.siani.orchilla.units.Month;
import systems.intino.datamarts.subjectstore.SubjectStore;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TemporalExpressionParserTest {
    private final static SubjectStore subjectStore = createSubjectStore();

    private static SubjectStore createSubjectStore() {
        try {
            return new SubjectStore(new File("index.triplets"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void context_should_be_well_rendered() {
        String string = "20250402T14:30:24>>set M04d02";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).context().head())
                .isEqualTo(LocalDateTime.of(2025, 4, 2, 14, 30, 24));
    }

    @Test
    public void should_be_two_set_predicates() {
        String string = "2025>>set M04d02";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(new SetMonthTemporalOperator(Month.Apr), new SetOrdinalDayTemporalOperator(2)));
    }

    @Test
    public void should_be_three_set_predicates() {
        String string = "2025>>set Y2024M04d02";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new SetYearTemporalOperator(2024),
                        new SetMonthTemporalOperator(Month.Apr),
                        new SetOrdinalDayTemporalOperator(2))
                );
    }

    @Test
    public void should_be_three_set_predicates_event_if_separated() {
        String string = "2025>>set Y2024>>set M04>>set d02";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new SetYearTemporalOperator(2024),
                        new SetMonthTemporalOperator(Month.Apr),
                        new SetOrdinalDayTemporalOperator(2))
                );
    }

    @Test
    public void should_be_one_add_predicate() {
        String string = "2025>>add 1d";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new AddDayTemporalOperator(1)
                ));
    }

    @Test
    public void should_be_two_add_predicates() {
        String string = "2025>>add 4d4W";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new AddWeekTemporalOperator(4),
                        new AddDayTemporalOperator(4)
                ));
    }


   @Test
    public void should_be_three_add_predicates() {
        String string = "2025>>add 4d4W>>add 2M";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new AddWeekTemporalOperator(4),
                        new AddDayTemporalOperator(4),
                        new AddMonthTemporalOperator(2)
                ));
    }

    @Test
    public void should_be_a_set_and_an_add() {
        String string = "2025>>next d04M04>>add 2M";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new NextMonthTemporalOperator(Month.Apr),
                        new NextDayTemporalOperator(4),
                        new AddMonthTemporalOperator(2)
                ));
    }

    @Test
    public void should_be_a_set_and_an_add_and_a_start() {
        String string = "2025>>next d04M04>>add 2M>>start";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new NextMonthTemporalOperator(Month.Apr),
                        new NextDayTemporalOperator(4),
                        new AddMonthTemporalOperator(2),
                        new Early()
                ));
    }

    @Test
    public void should_be_a_set_and_an_add_and_a_mid() {
        String string = "2025>>next d04M04>>add 2M>>mid";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new NextMonthTemporalOperator(Month.Apr),
                        new NextDayTemporalOperator(4),
                        new AddMonthTemporalOperator(2),
                        new Mid()
                ));
    }


    @Test
    public void should_be_a_set_and_an_add_and_an_end() {
        String string = "2025>>next d04M04>>add 2M>>end";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new NextMonthTemporalOperator(Month.Apr),
                        new NextDayTemporalOperator(4),
                        new AddMonthTemporalOperator(2),
                        new Late()
                ));
    }

    @Test
    public void render_context_with_year_duration() {
        String string = "2025/P1Y>>add 4d4W>>add 2M";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).context())
                .isEqualTo(new TemporalTag(
                        LocalDateTime.of(2025, 1, 1, 0, 0),
                        LocalDateTime.of(2026, 1, 1, 0, 0).minusNanos(1),
                        Period.Year,
                        null));
    }

    @Test
    public void render_context_with_duration_year() {
        String string = "P1Y/2025>>add 4d4W>>add 2M";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).context())
                .isEqualTo(new TemporalTag(
                        LocalDateTime.of(2024, 1, 1, 0, 0),
                        LocalDateTime.of(2025, 1, 1, 0, 0).minusNanos(1),
                        Period.Year,
                        null));
    }
}
