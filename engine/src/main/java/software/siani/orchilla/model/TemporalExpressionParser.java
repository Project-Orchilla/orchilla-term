package software.siani.orchilla.model;

import software.siani.orchilla.model.distributions.ConstantDistribution;
import software.siani.orchilla.model.functions.CompositeTemporalFunction;
import software.siani.orchilla.model.functions.TemporalFunction;
import software.siani.orchilla.model.functions.boundaries.Head;
import software.siani.orchilla.model.functions.boundaries.Tail;
import software.siani.orchilla.model.functions.businessday.AddBusinessDayTemporalFunction;
import software.siani.orchilla.model.functions.businessday.SubBusinessDayTemporalFunction;
import software.siani.orchilla.model.functions.century.AddCenturyTemporalFunction;
import software.siani.orchilla.model.functions.century.CenturyConstants;
import software.siani.orchilla.model.functions.century.SetCenturyTemporalFunction;
import software.siani.orchilla.model.functions.century.SubCenturyTemporalFunction;
import software.siani.orchilla.model.functions.day.*;
import software.siani.orchilla.model.functions.decade.DecadeConstants;
import software.siani.orchilla.model.functions.decade.NextDecadeTemporalFunction;
import software.siani.orchilla.model.functions.decade.SetDecadeTemporalFunction;
import software.siani.orchilla.model.functions.decade.SubDecadeTemporalFunction;
import software.siani.orchilla.model.functions.events.LastWithArgumentsEventTemporalFunction;
import software.siani.orchilla.model.functions.events.NextWithArgumentsEventTemporalFunction;
import software.siani.orchilla.model.functions.events.SetEventTemporalFunction;
import software.siani.orchilla.model.functions.fuzzy.*;
import software.siani.orchilla.model.functions.granularity.*;
import software.siani.orchilla.model.functions.hour.AddHourTemporalFunction;
import software.siani.orchilla.model.functions.hour.HourConstants;
import software.siani.orchilla.model.functions.hour.SetHourTemporalFunction;
import software.siani.orchilla.model.functions.hour.SubHourTemporalFunction;
import software.siani.orchilla.model.functions.lustrum.AddLustrumTemporalFunction;
import software.siani.orchilla.model.functions.lustrum.LustrumConstants;
import software.siani.orchilla.model.functions.lustrum.SubLustrumTemporalFunction;
import software.siani.orchilla.model.functions.millennium.AddMillenniumTemporalFunction;
import software.siani.orchilla.model.functions.millennium.MillenniumConstants;
import software.siani.orchilla.model.functions.millennium.SetMillenniumTemporalFunction;
import software.siani.orchilla.model.functions.millennium.SubMillenniumTemporalFunction;
import software.siani.orchilla.model.functions.minute.AddMinuteTemporalFunction;
import software.siani.orchilla.model.functions.minute.MinuteConstants;
import software.siani.orchilla.model.functions.minute.SetMinuteTemporalFunction;
import software.siani.orchilla.model.functions.minute.SubMinuteTemporalFunction;
import software.siani.orchilla.model.functions.month.*;
import software.siani.orchilla.model.functions.quarter.QuarterConstants;
import software.siani.orchilla.model.functions.quarter.SetQuarterTemporalFunction;
import software.siani.orchilla.model.functions.season.LastSeasonTemporalFunction;
import software.siani.orchilla.model.functions.season.NextSeasonTemporalFunction;
import software.siani.orchilla.model.functions.season.SetSeasonTemporalFunction;
import software.siani.orchilla.model.functions.second.AddSecondTemporalFunction;
import software.siani.orchilla.model.functions.second.SetSecondTemporalFunction;
import software.siani.orchilla.model.functions.second.SubSecondTemporalFunction;
import software.siani.orchilla.model.functions.semester.SemesterConstants;
import software.siani.orchilla.model.functions.semester.SetSemesterTemporalFunction;
import software.siani.orchilla.model.functions.week.AddWeekTemporalFunction;
import software.siani.orchilla.model.functions.week.SetWeekFunction;
import software.siani.orchilla.model.functions.week.SubWeekTemporalFunction;
import software.siani.orchilla.model.functions.week.WeekConstants;
import software.siani.orchilla.model.functions.weekday.LastWeekdayFunction;
import software.siani.orchilla.model.functions.weekday.NextWeekdayFunction;
import software.siani.orchilla.model.functions.weekday.SetWeekdayFunction;
import software.siani.orchilla.model.functions.weekend.AddWeekendTemporalFunction;
import software.siani.orchilla.model.functions.weekend.SetWeekendFunction;
import software.siani.orchilla.model.functions.weekend.SubWeekendTemporalFunction;
import software.siani.orchilla.model.functions.year.AddYearTemporalFunction;
import software.siani.orchilla.model.functions.year.SetYearTemporalFunction;
import software.siani.orchilla.model.functions.year.SubYearTemporalFunction;
import software.siani.orchilla.model.functions.year.YearConstants;
import software.siani.orchilla.model.units.Month;
import software.siani.orchilla.model.units.Season;
import software.siani.orchilla.model.units.Weekday;
import systems.intino.datamarts.subjectstore.SubjectStore;

import java.time.LocalDateTime;
import java.util.*;

public class TemporalExpressionParser {
    private final SubjectStore subjectStore;

    public TemporalExpressionParser(SubjectStore subjectStore) {
        this.subjectStore = subjectStore;
    }

    public TemporalExpression parse(String string) {
        char[] charArray = string.toCharArray();
        List<TemporalFunction> temporalFunctions = new ArrayList<>();
        char previous;
        StringBuilder sb = null;
        for (int i = 0; i < charArray.length; i++) {
            previous = charArray[i!=0 ? i-1 : i];
            if (previous == '>' && charArray[i] == ' ') continue;
            if (sb != null && charArray[i] != '>') sb.append(charArray[i]);
            if (previous == charArray[i] && charArray[i] == '>' || i == charArray.length - 1) {
                if (sb!=null) temporalFunctions.addAll(operatorFor(sb.toString().trim()));
                sb = new StringBuilder();
            }
        }
        return new TemporalExpression(context(string.substring(0, string.indexOf(">>")).trim()), new CompositeTemporalFunction(temporalFunctions));
    }

    private TemporalTag context(String context) {
        if (context.equalsIgnoreCase("???")) return null;
        if (isInterval(context)) return intervalFor(context);
        return temporalTagFrom(context);
    }

    private TemporalTag intervalFor(String context) {
        String firstTag = context.substring(0, context.indexOf("/"));
        String secondTag = context.substring(context.indexOf("/") + 1);
        if (isPeriod(firstTag)) return periodForSecondTag(firstTag, secondTag);
        if (isPeriod(secondTag)) return periodForFirstTag(firstTag, secondTag);
        return periodForBothTags(context, firstTag, secondTag);
    }

    private TemporalTag periodForFirstTag(String firstTag, String secondTag) {
        TemporalTag secondTemporalTagSolved = parse(firstTag + ">>add " + secondTag.substring(1)).solve();
        TemporalTag firstTemporalTag = context(firstTag);
        return new TemporalTag(firstTemporalTag.head(), secondTemporalTagSolved.head().minusNanos(1), firstTemporalTag.period(), null);
    }

    private TemporalTag periodForSecondTag(String firstTag, String secondTag) {
        TemporalTag firstTemporalTagSolved = parse(secondTag + ">>sub " + firstTag.substring(1)).solve();
        TemporalTag secondTemporalTag = context(secondTag);
        return new TemporalTag(firstTemporalTagSolved.head(), secondTemporalTag.head().minusNanos(1), secondTemporalTag.period(), null);
    }

    private boolean isPeriod(String s) {
        return s.startsWith("P");
    }

    private TemporalTag periodForBothTags(String context, String firstTag, String secondTag) {
        LocalDateTime start = localDateTime(firstTag);
        LocalDateTime end = localDateTime(secondTag);
        Period periodForFistTag = periodFor(firstTag);
        Period periodForSecondTag = periodFor(secondTag);
        if (periodForFistTag != periodForSecondTag)
            throw new RuntimeException("The period must be equal for both tags in the context, first one in " + periodForFistTag + ", second one in " + periodForSecondTag);
        return new TemporalTag(
                start,
                end,
                periodFor(context),
                new ConstantDistribution(0, 0));
    }

    private static boolean isInterval(String context) {
        return context.contains("/");
    }

    private TemporalTag temporalTagFrom(String context) {
        if (context.trim().startsWith("now")) return new TemporalTag(LocalDateTime.now(), LocalDateTime.now(), Period.Second, new ConstantDistribution(0, 0));
        return new TemporalTag(
                localDateTime(context),
                localDateTime(context),
                periodFor(context),
                new ConstantDistribution(0, 0)
        );
    }

    private static LocalDateTime localDateTime(String context) {
        int millennium = Integer.parseInt(String.valueOf(context.charAt(0)));
        int century = context.length() > 1 ? Integer.parseInt(context.substring(1, 2)) : 0;
        int year = context.length() > 2 ? Integer.parseInt(context.substring(2, 4)) : 0;
        int month = context.length() > 4 ? Integer.parseInt(context.substring(4, 6)) : 1;
        int day = context.length() > 6 ? Integer.parseInt(context.substring(6, 8)) : 1;
        int hour = context.length() > 9 ? Integer.parseInt(context.substring(9, 11)) : 0;
        int minute = context.length() > 12 ? Integer.parseInt(context.substring(12, 14)) : 0;
        int second = context.length() > 15 ? Integer.parseInt(context.substring(15, 17)) : 0;
        return LocalDateTime.of(millennium * 1000 + century * 100 + year, month, day, hour, minute, second);
    }

    private Period periodFor(String context) {
        if (context.length() > 15) return Period.Second;
        if (context.length() > 12) return Period.Minute;
        if (context.length() > 9) return Period.Hour;
        if (context.length() > 6) return Period.Day;
        if (context.length() > 4) return Period.Month;
        if (context.length() > 2) return Period.Year;
        if (context.length() > 1) return Period.Century;
        return Period.Millennium;
    }

    private List<TemporalFunction> operatorFor(String string) {
        if (isOrdinalOperator(string)) return ordinalOperator(string);
        if (string.startsWith("head")) return List.of(new Head());
        if (string.startsWith("tail")) return List.of(new Tail());
        if (string.startsWith("early")) return List.of(new Early());
        if (string.startsWith("mid")) return List.of(new Mid());
        if (string.startsWith("late")) return List.of(new Late());
        if (string.startsWith("set")) return setFor(string);
        if (string.startsWith("add")) return addFor(string);
        if (string.startsWith("sub")) return subFor(string);
        if (string.startsWith("next")) return nextFor(string);
        if (string.startsWith("last")) return lastFor(string);
        if (string.startsWith("before")) return List.of(new Before());
        if (string.startsWith("after")) return List.of(new After());
        if (string.toLowerCase().startsWith("morning")) return List.of(new Morning());
        if (string.toLowerCase().startsWith("afternoon")) return List.of(new Afternoon());
        if (string.toLowerCase().startsWith("millennium")) return List.of(new MillenniumGranularityFunction());
        if (string.toLowerCase().startsWith("century")) return List.of(new CenturyGranularityFunction());
        if (string.toLowerCase().startsWith("decade")) return List.of(new DecadeGranularityFunction());
        if (string.toLowerCase().startsWith("lustrum")) return List.of(new LustrumGranularityFunction());
        if (string.toLowerCase().startsWith("year")) return List.of(new YearGranularityFunction());
        if (string.toLowerCase().startsWith("semester")) return List.of(new SemesterGranularityFunction());
        if (string.toLowerCase().startsWith("season")) return List.of(new SeasonGranularityFunction());
        if (string.toLowerCase().startsWith("quarter")) return List.of(new QuarterGranularityFunction());
        if (string.toLowerCase().startsWith("month")) return List.of(new MonthGranularityFunction());
        if (string.toLowerCase().startsWith("week")) return List.of(new WeekGranularityFunction());
        if (string.toLowerCase().startsWith("day")) return List.of(new DayGranularityFunction());
        if (string.toLowerCase().startsWith("minute")) return List.of(new MinuteGranularityFunction());
        if (string.toLowerCase().startsWith("second")) return List.of(new SecondGranularityFunction());
        return List.of();
    }

    private static boolean isOrdinalOperator(String string) {
        return Ordinal.isOrdinal(string.split(" ")[0]);
    }

    private List<TemporalFunction> ordinalOperator(String string) {
        String[] split = string.split(" ");
        String ordinalOperator = split[0];
        List<TemporalFunction> result = new ArrayList<>();
        Ordinal.parseToNumber(ordinalOperator);
        if (split[1].contains("wd")) {
            HashMap<String, Double> arguments = argumentsFromBeginning(split[1].toCharArray());
            result.add(new SetWeekdayFunction(Weekday.values()[arguments.get("wd").intValue()-1], Ordinal.parseToNumber(ordinalOperator)));
        }
        if (split[1].equals("SM")) result.add(new SetSemesterTemporalFunction(Ordinal.parseToNumber(ordinalOperator)));
        if (split[1].equals("Q")) result.add(new SetQuarterTemporalFunction(Ordinal.parseToNumber(ordinalOperator)));
        if (split[1].equals("M")) result.add(new SetMonthTemporalFunction(Month.values()[Ordinal.parseToNumber(ordinalOperator) - 1]));
        if (split[1].equals("w")) result.add(new SetWeekFunction(Ordinal.parseToNumber(ordinalOperator)));
        if (split[1].equals("we")) result.add(new SetWeekendFunction(Ordinal.parseToNumber(ordinalOperator)));
        if (split[1].equals("h")) result.add(new SetHourTemporalFunction(Ordinal.parseToNumber(ordinalOperator)));
        if (split[1].equals("d")) result.add(new SetDayTemporalFunction(Ordinal.parseToNumber(ordinalOperator)));
        return result;
    }

    private List<TemporalFunction> setFor(String string) {
        String substring = string.substring(4).trim();
        if (substring.startsWith("\"")) return List.of(new SetEventTemporalFunction(substring.substring(1, substring.length()-1).trim(), subjectStore));
        if (substring.startsWith("E")) return List.of(new SetEventTemporalFunction(substring.substring(1).trim(), subjectStore));
        List<TemporalFunction> result = new ArrayList<>();
        HashMap<String, Double> arguments = argumentsFromBeginning(string.substring(4).toCharArray());
        if (arguments.containsKey("ML")) result.add(new SetMillenniumTemporalFunction(arguments.get("ML").intValue()));
        if (arguments.containsKey("C")) result.add(new SetCenturyTemporalFunction(arguments.get("C").intValue()));
        if (arguments.containsKey("D")) result.add(new SetDecadeTemporalFunction(arguments.get("D").intValue()));
        if (arguments.containsKey("Y")) result.add(new SetYearTemporalFunction(arguments.get("Y").intValue()));
        if (arguments.containsKey("SM")) result.add(new SetSemesterTemporalFunction(arguments.get("SM").intValue()));
        if (arguments.containsKey("Q")) result.add(new SetQuarterTemporalFunction(arguments.get("Q").intValue()));
        if (arguments.containsKey("S")) result.add(new SetSeasonTemporalFunction(Season.values()[arguments.get("S").intValue() -1]));
        if (arguments.containsKey("M")) result.add(new SetMonthTemporalFunction(Month.values()[arguments.get("M").intValue()-1]));
        if (arguments.containsKey("w")) result.add(new SetWeekFunction(arguments.get("w").intValue()));
        if (arguments.containsKey("wd")) result.add(new SetWeekdayFunction(Weekday.values()[arguments.get("wd").intValue()-1], arguments.getOrDefault("n", 1.).intValue()));
        if (arguments.containsKey("we")) result.add(new SetWeekendFunction(arguments.get("we").intValue()));
        if (arguments.containsKey("d")) result.add(new SetDayTemporalFunction(arguments.get("d").intValue()));
        if (arguments.containsKey("h")) result.add(new SetHourTemporalFunction(arguments.get("h").intValue()));
        if (arguments.containsKey("m")) result.add(new SetMinuteTemporalFunction(arguments.get("m").intValue()));
        if (arguments.containsKey("s")) result.add(new SetSecondTemporalFunction(arguments.get("s").intValue()));
        return result;
    }

    private List<TemporalFunction> addFor(String string) {
        String substring = string.substring(4);
        List<TemporalFunction> result = new ArrayList<>();
        HashMap<String, Double> arguments = argumentsFromEnd(substring.toCharArray());
        if (arguments.containsKey("ML")) {
            result.add(new AddMillenniumTemporalFunction(arguments.get("ML").intValue()));
            if (arguments.get("ML").intValue() - arguments.get("ML") != 0)
                result.addAll(addFor("add " + (arguments.get("ML") - arguments.get("ML").intValue()) * MillenniumConstants.YearsPerMillennium + "Y"));
        }
        if (arguments.containsKey("C")) {
            result.add(new AddCenturyTemporalFunction(arguments.get("C").intValue()));
            if (arguments.get("C").intValue() - arguments.get("C") != 0)
                result.addAll(addFor("add " + (arguments.get("C") - arguments.get("C").intValue()) * CenturyConstants.YearsPerCentury + "C"));
        }
        if (arguments.containsKey("D")) {
            result.add(new AddDayTemporalFunction(arguments.get("D").intValue()));
            if (arguments.get("D").intValue() - arguments.get("D") != 0)
                result.addAll(addFor("add " + (arguments.get("D") - arguments.get("D").intValue()) * DecadeConstants.YearsPerDecade  + "Y"));
        }
        if (arguments.containsKey("L")) {
            result.add(new AddLustrumTemporalFunction(arguments.get("L").intValue()));
            if (arguments.get("L").intValue() - arguments.get("L") != 0)
                result.addAll(addFor("add " + (arguments.get("L") - arguments.get("L").intValue()) * LustrumConstants.YearsPerLustrum  + "Y"));
        }
        if (arguments.containsKey("Y")) {
            result.add(new AddYearTemporalFunction(arguments.get("Y").intValue()));
            if (arguments.get("Y").intValue() - arguments.get("Y") != 0)
                result.addAll(addFor("add " + (arguments.get("Y") - arguments.get("Y").intValue()) * YearConstants.numberOfMonths()  + "M"));
        }
        if (arguments.containsKey("SM")) {
            result.add(new SetSemesterTemporalFunction(arguments.get("SM").intValue()));
            if (arguments.get("SM").intValue() - arguments.get("SM") != 0)
                result.addAll(addFor("add " + (arguments.get("SM") - arguments.get("SM").intValue()) * SemesterConstants.numberOfMonths  + "M"));
        }
        if (arguments.containsKey("Q")) {
            result.add(new SetQuarterTemporalFunction(arguments.get("Q").intValue()));
            if (arguments.get("Q").intValue() - arguments.get("Q") != 0)
                result.addAll(addFor("add " + (arguments.get("Q") - arguments.get("Q").intValue()) * QuarterConstants.numberOfMonths + "M"));
        }
        if (arguments.containsKey("M")) {
            result.add(new AddMonthTemporalFunction(arguments.get("M").intValue()));
            if (arguments.get("M").intValue() - arguments.get("M") != 0)
                result.addAll(addFor("add " + (arguments.get("M") - arguments.get("M").intValue()) * Month.numberOfWeeks()  + "w"));
        }
        if (arguments.containsKey("w")) {
            result.add(new AddWeekTemporalFunction(arguments.get("w").intValue()));
            if (arguments.get("w").intValue() - arguments.get("w") != 0)
                result.addAll(addFor("add " + (arguments.get("w") - arguments.get("w").intValue()) * WeekConstants.daysPerWeek  + "d"));
        }
        if (arguments.containsKey("we")) {
            result.add(new AddWeekendTemporalFunction(arguments.get("we").intValue()));
        }
        if (arguments.containsKey("d")) {
            result.add(new AddDayTemporalFunction(arguments.get("d").intValue()));
            if (arguments.get("d").intValue() - arguments.get("d") != 0)
                result.addAll(addFor("add " + (arguments.get("d") - arguments.get("d").intValue()) * DayConstants.numberOfHours  + "h"));
        }
        if (arguments.containsKey("bd")) {
            result.add(new AddBusinessDayTemporalFunction(arguments.get("bd").intValue()));
        }
        if (arguments.containsKey("h")) {
            result.add(new AddHourTemporalFunction(arguments.get("h").intValue()));
            if (arguments.get("h").intValue() - arguments.get("h") != 0)
                result.addAll(addFor("add " + (arguments.get("h") - arguments.get("h").intValue()) * HourConstants.minutesInHour + "m"));
        }
        if (arguments.containsKey("m")) {
            result.add(new AddMinuteTemporalFunction(arguments.get("m").intValue()));
            if (arguments.get("m").intValue() - arguments.get("m") != 0)
                result.addAll(addFor("add " + (arguments.get("m") - arguments.get("m").intValue()) * MinuteConstants.secondsInMinute + "s"));
        }
        if (arguments.containsKey("s")) {
            result.add(new AddSecondTemporalFunction(arguments.get("s").intValue()));
        }
        return result;
    }

    private List<TemporalFunction> subFor(String predicate) {
        String substring = predicate.substring(4);
        List<TemporalFunction> result = new ArrayList<>();
        HashMap<String, Double> arguments = argumentsFromEnd(substring.toCharArray());
        if (arguments.containsKey("ML")) {
            result.add(new SubMillenniumTemporalFunction(arguments.get("ML").intValue()));
            if (arguments.get("ML").intValue() - arguments.get("ML") != 0)
                result.addAll(subFor("sub " + (arguments.get("ML") - arguments.get("ML").intValue()) * MillenniumConstants.YearsPerMillennium + "Y"));
        }
        if (arguments.containsKey("C")) {
            result.add(new SubCenturyTemporalFunction(arguments.get("C").intValue()));
            if (arguments.get("C").intValue() - arguments.get("C") != 0)
                result.addAll(subFor("sub " + (arguments.get("C") - arguments.get("C").intValue()) * CenturyConstants.YearsPerCentury + "C"));
        }
        if (arguments.containsKey("D")) {
            result.add(new SubDecadeTemporalFunction(arguments.get("D").intValue()));
            if (arguments.get("D").intValue() - arguments.get("D") != 0)
                result.addAll(subFor("sub " + (arguments.get("D") - arguments.get("D").intValue()) * DecadeConstants.YearsPerDecade + "Y"));
        }
        if (arguments.containsKey("L")) {
            result.add(new SubLustrumTemporalFunction(arguments.get("L").intValue()));
            if (arguments.get("L").intValue() - arguments.get("L") != 0)
                result.addAll(subFor("sub " + (arguments.get("L") - arguments.get("L").intValue()) * LustrumConstants.YearsPerLustrum + "Y"));
        }
        if (arguments.containsKey("Y")) {
            result.add(new SubYearTemporalFunction(arguments.get("Y").intValue()));
            if (arguments.get("Y").intValue() - arguments.get("Y") != 0)
                result.addAll(subFor("sub " + (arguments.get("Y") - arguments.get("Y").intValue()) * YearConstants.numberOfMonths() + "M"));
        }
        if (arguments.containsKey("SM")) {
            result.add(new SetSemesterTemporalFunction(arguments.get("SM").intValue()));
            if (arguments.get("SM").intValue() - arguments.get("SM") != 0)
                result.addAll(subFor("sub " + (arguments.get("SM") - arguments.get("SM").intValue()) * SemesterConstants.numberOfMonths  + "M"));
        }
        if (arguments.containsKey("Q")) {
            result.add(new SetQuarterTemporalFunction(arguments.get("Q").intValue()));
            if (arguments.get("Q").intValue() - arguments.get("Q") != 0)
                result.addAll(subFor("sub " + (arguments.get("Q") - arguments.get("Q").intValue()) * QuarterConstants.numberOfMonths + "M"));
        }
        if (arguments.containsKey("M")) {
            result.add(new SubMonthTemporalFunction(arguments.get("M").intValue()));
            if (arguments.get("M").intValue() - arguments.get("M") != 0)
                result.addAll(subFor("sub " + (arguments.get("M") - arguments.get("M").intValue()) * Month.numberOfWeeks() + "w"));
        }
        if (arguments.containsKey("w")) {
            result.add(new SubWeekTemporalFunction(arguments.get("w").intValue()));
            if (arguments.get("w").intValue() - arguments.get("w") != 0)
                result.addAll(subFor("sub " + (arguments.get("w") - arguments.get("w").intValue()) * WeekConstants.daysPerWeek + "d"));
        }
        if (arguments.containsKey("we")) {
            result.add(new SubWeekendTemporalFunction(arguments.get("we").intValue()));
        }
        if (arguments.containsKey("d")) {
            result.add(new SubDayTemporalFunction(arguments.get("d").intValue()));
            if (arguments.get("d").intValue() - arguments.get("d") != 0)
                result.addAll(subFor("sub " + (arguments.get("d") - arguments.get("d").intValue()) * DayConstants.numberOfHours + "h"));
        }
        if (arguments.containsKey("bd")) {
            result.add(new SubBusinessDayTemporalFunction(arguments.get("bd").intValue()));
        }
        if (arguments.containsKey("h")) {
            result.add(new SubHourTemporalFunction(arguments.get("h").intValue()));
            if (arguments.get("h").intValue() - arguments.get("h") != 0)
                result.addAll(subFor("sub " + (arguments.get("h") - arguments.get("h").intValue()) * HourConstants.minutesInHour + "m"));
        }
        if (arguments.containsKey("m")) {
            result.add(new SubMinuteTemporalFunction(arguments.get("m").intValue()));
            if (arguments.get("m").intValue() - arguments.get("m") != 0)
                result.addAll(subFor("sub " + (arguments.get("m") - arguments.get("m").intValue()) * MinuteConstants.secondsInMinute + "s"));
        }
        if (arguments.containsKey("s")) {
            result.add(new SubSecondTemporalFunction(arguments.get("s").intValue()));
        }
        return result;
    }


    private List<TemporalFunction> nextFor(String string) {
        String substring = string.substring(5);
        if (substring.endsWith("\"")) {
            int idx = 0;
            while (idx < substring.length() && Character.isDigit(substring.charAt(idx))) idx++;
            return List.of(new NextWithArgumentsEventTemporalFunction(Integer.parseInt(substring.substring(0, idx)), substring.substring(idx), subjectStore));
        }
        if (substring.startsWith("E")) return List.of(new NextWithArgumentsEventTemporalFunction(1, substring.substring(1).trim(), subjectStore));
        if (isSyntacticSugarOfWeekday(substring)) return List.of(new NextWeekdayFunction(1, Weekday.valueOf(capitalize(substring))));
        if (isSyntacticSugarOfMonth(substring)) return List.of(new NextMonthTemporalFunction(Month.from(capitalize(substring))));
        List<TemporalFunction> result = new ArrayList<>();
        HashMap<String, Double> arguments = argumentsFromBeginning(substring.toCharArray());
        if (arguments.containsKey("S")) result.add(new NextSeasonTemporalFunction(arguments.get("n").intValue(), Season.values()[arguments.get("S").intValue() -1]));
        if (arguments.containsKey("D")) result.add(new NextDecadeTemporalFunction(arguments.get("D").intValue()));
        if (arguments.containsKey("M")) result.add(new NextMonthTemporalFunction(Month.values()[arguments.get("M").intValue()-1]));
        if (arguments.containsKey("wd")) result.add(new NextWeekdayFunction(arguments.getOrDefault("n", 1.).intValue(), Weekday.values()[arguments.get("wd").intValue()-1]));
        if (arguments.containsKey("d")) result.add(new NextDayTemporalFunction(arguments.get("d").intValue()));
        return result;
    }

    private List<TemporalFunction> lastFor(String string) {
        String substring = string.substring(5).trim();
        if (substring.endsWith("\"")) {
            int idx = 0;
            while (idx < substring.length() && Character.isDigit(substring.charAt(idx))) idx++;
            return List.of(new LastWithArgumentsEventTemporalFunction(Integer.parseInt(substring.substring(0, idx)), substring.substring(idx), subjectStore));
        }
        if (substring.startsWith("E")) return List.of(new LastWithArgumentsEventTemporalFunction(1, substring.substring(1).trim(), subjectStore));
        if (isSyntacticSugarOfWeekday(substring)) return List.of(new LastWeekdayFunction(1, Weekday.valueOf(capitalize(substring))));
        if (isSyntacticSugarOfMonth(substring)) return List.of(new LastMonthTemporalFunction(Month.from(capitalize(substring))));
        List<TemporalFunction> result = new ArrayList<>();
        HashMap<String, Double> arguments = argumentsFromBeginning(substring.toCharArray());
        if (arguments.containsKey("S")) result.add(new LastSeasonTemporalFunction(arguments.get("n").intValue(), Season.values()[arguments.get("S").intValue() -1]));
        if (arguments.containsKey("D")) result.add(new NextDecadeTemporalFunction(arguments.get("D").intValue()));
        if (arguments.containsKey("M")) result.add(new LastMonthTemporalFunction(Month.values()[arguments.get("M").intValue()-1]));
        if (arguments.containsKey("wd")) result.add(new LastWeekdayFunction(arguments.getOrDefault("n", 1.).intValue(), Weekday.values()[arguments.get("wd").intValue()-1]));
        if (arguments.containsKey("d")) result.add(new LastDayTemporalFunction(arguments.get("d").intValue()));
        return result;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        s = s.trim();
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private boolean isSyntacticSugarOfWeekday(String string) {
        Set<String> days = Set.of(
                "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"
        );
        return days.contains(string.toLowerCase().trim().toLowerCase());
    }

    private boolean isSyntacticSugarOfMonth(String string) {
        Set<String> months = Set.of(
                "january", "february", "march", "april", "may", "june",
                "july", "august", "september", "october", "november", "december"
        );
        return months.contains(string.toLowerCase().trim().toLowerCase());
    }

    private HashMap<String, Double> argumentsFromBeginning(char[] arguments) {
        HashMap<String, Double> result = new HashMap<>();
        List<Character> numChars = new LinkedList<>();
        StringBuilder nowArgument = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            if (Character.isDigit(arguments[i]) || arguments[i] == '-' || arguments[i] == '.') numChars.add(arguments[i]);
            if (!(Character.isDigit(arguments[i]) || arguments[i] == '-' || arguments[i] == '.') && !numChars.isEmpty() || i == arguments.length - 1) {
                result.put(nowArgument.toString(), parseFloat(numChars));
                numChars = new ArrayList<>();
                nowArgument = new StringBuilder();
                nowArgument.append(arguments[i]);
                continue;
            }
            if (!(Character.isDigit(arguments[i]) || arguments[i] == '-' || arguments[i] == '.')) nowArgument.append(arguments[i]);
        }
        return result;
    }

    private HashMap<String, Double> argumentsFromEnd(char[] arguments) {
        HashMap<String, Double> result = new HashMap<>();
        List<Character> numChars = new LinkedList<>();
        StringBuilder nowArgument = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            if (!(Character.isDigit(arguments[i]) || arguments[i] == '-' || arguments[i] == '.')) nowArgument.append(arguments[i]);
            if ((Character.isDigit(arguments[i]) || arguments[i] == '-' || arguments[i] == '.') && !nowArgument.isEmpty() || i == arguments.length - 1) {
                result.put(nowArgument.toString(), parseFloat(numChars));
                numChars = new ArrayList<>();
                nowArgument = new StringBuilder();
                numChars.add(arguments[i]);
                continue;
            }
            if (Character.isDigit(arguments[i]) || arguments[i] == '-' || arguments[i] == '.') numChars.add(arguments[i]);
        }
        return result;
    }

    private double parseFloat(List<Character> chars) {
        return Double.parseDouble(chars.stream().map(String::valueOf).reduce((accum, c) -> accum + c).get());
    }

    private static class Ordinal {
        private static final Map<String, Integer> ordinalWordToNumber = new HashMap<>();

        static {
            ordinalWordToNumber.put("first", 1);
            ordinalWordToNumber.put("second", 2);
            ordinalWordToNumber.put("third", 3);
            ordinalWordToNumber.put("fourth", 4);
            ordinalWordToNumber.put("fifth", 5);
            ordinalWordToNumber.put("sixth", 6);
            ordinalWordToNumber.put("seventh", 7);
            ordinalWordToNumber.put("eighth", 8);
            ordinalWordToNumber.put("ninth", 9);
            ordinalWordToNumber.put("tenth", 10);
            ordinalWordToNumber.put("eleventh", 11);
            ordinalWordToNumber.put("twelfth", 12);
            ordinalWordToNumber.put("thirteenth", 13);
            ordinalWordToNumber.put("fourteenth", 14);
            ordinalWordToNumber.put("fifteenth", 15);
            ordinalWordToNumber.put("sixteenth", 16);
            ordinalWordToNumber.put("seventeenth", 17);
            ordinalWordToNumber.put("eighteenth", 18);
            ordinalWordToNumber.put("nineteenth", 19);
            ordinalWordToNumber.put("twentieth", 20);
            ordinalWordToNumber.put("twenty-first", 21);
            ordinalWordToNumber.put("twenty-second", 22);
            ordinalWordToNumber.put("twenty-third", 23);
            ordinalWordToNumber.put("twenty-fourth", 24);
            ordinalWordToNumber.put("twenty-fifth", 25);
            ordinalWordToNumber.put("twenty-sixth", 26);
            ordinalWordToNumber.put("twenty-seventh", 27);
            ordinalWordToNumber.put("twenty-eighth", 28);
            ordinalWordToNumber.put("twenty-ninth", 29);
            ordinalWordToNumber.put("thirtieth", 30);
            ordinalWordToNumber.put("thirty-first", 31);
            ordinalWordToNumber.put("thirty-second", 32);
            ordinalWordToNumber.put("thirty-third", 33);
            ordinalWordToNumber.put("thirty-fourth", 34);
            ordinalWordToNumber.put("thirty-fifth", 35);
            ordinalWordToNumber.put("thirty-sixth", 36);
            ordinalWordToNumber.put("thirty-seventh", 37);
            ordinalWordToNumber.put("thirty-eighth", 38);
            ordinalWordToNumber.put("thirty-ninth", 39);
            ordinalWordToNumber.put("fortieth", 40);
            ordinalWordToNumber.put("forty-first", 41);
            ordinalWordToNumber.put("forty-second", 42);
            ordinalWordToNumber.put("forty-third", 43);
            ordinalWordToNumber.put("forty-fourth", 44);
            ordinalWordToNumber.put("forty-fifth", 45);
            ordinalWordToNumber.put("forty-sixth", 46);
            ordinalWordToNumber.put("forty-seventh", 47);
            ordinalWordToNumber.put("forty-eighth", 48);
            ordinalWordToNumber.put("forty-ninth", 49);
            ordinalWordToNumber.put("fiftieth", 50);
            ordinalWordToNumber.put("fifty-first", 51);
            ordinalWordToNumber.put("fifty-second", 52);
            ordinalWordToNumber.put("fifty-third", 53);
        }

        public static int parseToNumber(String string) {
            return ordinalWordToNumber.get(string);
        }

        public static boolean isOrdinal(String string) {
            return ordinalWordToNumber.containsKey(string);
        }
    }
}
