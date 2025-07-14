package software.siani.orchilla;

import org.junit.Test;
import software.siani.orchilla.model.Period;
import software.siani.orchilla.model.TemporalExpression;
import software.siani.orchilla.model.TemporalExpressionParser;
import software.siani.orchilla.model.TemporalTag;
import software.siani.orchilla.model.functions.day.AddDayTemporalFunction;
import software.siani.orchilla.model.functions.day.NextDayTemporalFunction;
import software.siani.orchilla.model.functions.day.SetDayTemporalFunction;
import software.siani.orchilla.model.functions.day.SubDayTemporalFunction;
import software.siani.orchilla.model.functions.decade.SetDecadeTemporalFunction;
import software.siani.orchilla.model.functions.events.LastWithArgumentsEventTemporalFunction;
import software.siani.orchilla.model.functions.events.NextWithArgumentsEventTemporalFunction;
import software.siani.orchilla.model.functions.events.SetEventTemporalFunction;
import software.siani.orchilla.model.functions.fuzzy.*;
import software.siani.orchilla.model.functions.hour.SetHourTemporalFunction;
import software.siani.orchilla.model.functions.hour.SubHourTemporalFunction;
import software.siani.orchilla.model.functions.minute.SetMinuteTemporalFunction;
import software.siani.orchilla.model.functions.month.AddMonthTemporalFunction;
import software.siani.orchilla.model.functions.month.LastMonthTemporalFunction;
import software.siani.orchilla.model.functions.month.NextMonthTemporalFunction;
import software.siani.orchilla.model.functions.month.SetMonthTemporalFunction;
import software.siani.orchilla.model.functions.week.AddWeekTemporalFunction;
import software.siani.orchilla.model.functions.week.SetWeekFunction;
import software.siani.orchilla.model.functions.weekday.LastWeekdayFunction;
import software.siani.orchilla.model.functions.weekday.NextWeekdayFunction;
import software.siani.orchilla.model.functions.weekday.SetWeekdayFunction;
import software.siani.orchilla.model.functions.weekend.SetWeekendFunction;
import software.siani.orchilla.model.functions.year.SetYearTemporalFunction;
import software.siani.orchilla.model.units.Month;
import software.siani.orchilla.model.units.Weekday;
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
                .isEqualTo(List.of(
                        new SetMonthTemporalFunction(Month.Apr),
                        new SetDayTemporalFunction(2)));
    }

    @Test
    public void should_be_three_set_predicates() {
        String string = "2025>>set Y2024M04d02";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new SetYearTemporalFunction(2024),
                        new SetMonthTemporalFunction(Month.Apr),
                        new SetDayTemporalFunction(2))
                );
    }

    @Test
    public void should_be_three_set_predicates_event_if_separated() {
        String string = "2025>>set Y2024>>set M04>>set d02";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new SetYearTemporalFunction(2024),
                        new SetMonthTemporalFunction(Month.Apr),
                        new SetDayTemporalFunction(2))
                );
    }

    @Test
    public void should_be_one_add_predicate() {
        String string = "2025>>add 1d";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new AddDayTemporalFunction(1)
                ));
    }

    @Test
    public void should_be_two_add_predicates() {
        String string = "2025>>add 4d4w";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new AddWeekTemporalFunction(4),
                        new AddDayTemporalFunction(4)
                ));
    }


   @Test
    public void should_be_three_add_predicates() {
        String string = "2025>>add 4d4w>>add 2M";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new AddWeekTemporalFunction(4),
                        new AddDayTemporalFunction(4),
                        new AddMonthTemporalFunction(2)
                ));
    }

    @Test
    public void should_be_a_set_and_an_add() {
        String string = "2025>>next d04M04>>add 2M";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new NextMonthTemporalFunction(Month.Apr),
                        new NextDayTemporalFunction(4),
                        new AddMonthTemporalFunction(2)
                ));
    }

    @Test
    public void should_be_a_set_and_an_add_and_a_start() {
        String string = "2025>>next d04M04>>add 2M>>early";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new NextMonthTemporalFunction(Month.Apr),
                        new NextDayTemporalFunction(4),
                        new AddMonthTemporalFunction(2),
                        new Early()
                ));
    }

    @Test
    public void should_be_a_set_and_an_add_and_a_mid() {
        String string = "2025>>next d04M04>>add 2M>>mid";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new NextMonthTemporalFunction(Month.Apr),
                        new NextDayTemporalFunction(4),
                        new AddMonthTemporalFunction(2),
                        new Mid()
                ));
    }


    @Test
    public void should_be_a_set_and_an_add_and_an_end() {
        String string = "2025>>next d04M04>>add 2M>>late";
        assertThat(new TemporalExpressionParser(subjectStore).parse(string).operators())
                .isEqualTo(List.of(
                        new NextMonthTemporalFunction(Month.Apr),
                        new NextDayTemporalFunction(4),
                        new AddMonthTemporalFunction(2),
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

    @Test
    public void render_expression_1() {
        String string = "???>>set M05>>first wd2>>sub 1d>>set h17m05";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SetMonthTemporalFunction(Month.May),
                        new SetWeekdayFunction(Weekday.Tuesday, 1),
                        new SubDayTemporalFunction(1),
                        new SetHourTemporalFunction(17),
                        new SetMinuteTemporalFunction(5)));
    }

    @Test
    public void render_expression_2() {
        String string = "???>>add 1M>>second wd2>>set h18m00";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new AddMonthTemporalFunction(1),
                        new SetWeekdayFunction(Weekday.Tuesday, 2),
                        new SetHourTemporalFunction(18),
                        new SetMinuteTemporalFunction(0)));
    }

    @Test
    public void render_expression_3() {
        String string = "???>>last wd4";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new LastWeekdayFunction(1, Weekday.Thursday)));
    }

    @Test
    public void render_expression_4() {
        String string = "???>>set \"medieval times\"";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SetEventTemporalFunction("medieval times", subjectStore)));
    }

    @Test
    public void render_expression_5() {
        String string = "???>>morning>>set h6m45";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new Morning(),
                        new SetHourTemporalFunction(6),
                        new SetMinuteTemporalFunction(45)));
    }

    @Test
    public void render_expression_6() {
        String string = "???>>set \"Halloween\">>add 1d";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SetEventTemporalFunction("Halloween", subjectStore),
                        new AddDayTemporalFunction(1)));
    }

    @Test
    public void render_expression_7() {
        String string = "???>>next wd05>>before";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new NextWeekdayFunction(1, Weekday.Friday),
                        new Before()));
    }

    @Test
    public void render_expression_8() {
        String string = "???>>set D90>>before";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SetDecadeTemporalFunction(90),
                        new Before()));
    }

    @Test
    public void render_expression_9() {
        String string = "???>>set M04>>set n-1wd04";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SetMonthTemporalFunction(Month.Apr),
                        new SetWeekdayFunction(Weekday.Thursday, -1)));
    }

    @Test
    public void render_expression_10() {
        String string = "???>>set \"fiscal quarter\">>last n1wd01>>set h09m00>>before";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SetEventTemporalFunction("fiscal quarter", subjectStore),
                        new LastWeekdayFunction(1, Weekday.Monday),
                        new SetHourTemporalFunction(9),
                        new SetMinuteTemporalFunction(0),
                        new Before()));
    }

    @Test
    public void render_expression_11() {
        String string = "???>>set Y2024M07d17";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SetYearTemporalFunction(2024),
                        new SetMonthTemporalFunction(Month.Jul),
                        new SetDayTemporalFunction(17)));
    }

    @Test
    public void render_expression_12() {
        String string = "???>>first w";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SetWeekFunction(1)));
    }

    @Test
    public void render_expression_13() {
        String string = "???>>second we";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SetWeekendFunction(2)));
    }

    @Test
    public void render_expression_14() {
        String string = "???>>next n59wd02";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new NextWeekdayFunction(59, Weekday.Tuesday)));
    }

    @Test
    public void render_expression_15() {
        String string = "???>>add 2.5M";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new AddMonthTemporalFunction(2),
                        new AddWeekTemporalFunction(2)));
    }

    @Test
    public void render_expression_16() {
        String string = "???>>sub 1.5d";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SubDayTemporalFunction(1),
                        new SubHourTemporalFunction(12)));
    }

    @Test
    public void render_expression_17() {
        String string = "???>>next Monday";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new NextWeekdayFunction(1, Weekday.Monday)));
    }

    @Test
    public void render_expression_18() {
        String string = "???>>last june";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new LastMonthTemporalFunction(Month.Jun)));
    }

    @Test
    public void render_expression_19() {
        String string = "  ???  >>   last    june  ";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new LastMonthTemporalFunction(Month.Jun)));
    }

    @Test
    public void render_expression_20() {
        String string = "  ???  >>  set E Semana Santa >> add 1d";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new SetEventTemporalFunction("Semana Santa", subjectStore),
                        new AddDayTemporalFunction(1)));
    }

    @Test
    public void render_expression_21() {
        String string = "  ???  >>  next E Semana Santa >> add 1d";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new NextWithArgumentsEventTemporalFunction(1, "Semana Santa", subjectStore),
                        new AddDayTemporalFunction(1)
                ));
    }

    @Test
    public void render_expression_22() {
        String string = "  ???  >>  last E Semana Santa >> add 1d";
        assertThat(new TemporalExpression.Builder().with(subjectStore).build(string).operators())
                .isEqualTo(List.of(
                        new LastWithArgumentsEventTemporalFunction(1, "Semana Santa", subjectStore),
                        new AddDayTemporalFunction(1)
                ));
    }

}
