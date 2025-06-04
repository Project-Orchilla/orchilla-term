package software.siani.orchilla;

import software.siani.orchilla.distributions.ConstantDistribution;
import software.siani.orchilla.operators.century.*;
import software.siani.orchilla.operators.day.*;
import software.siani.orchilla.operators.decade.*;
import software.siani.orchilla.operators.events.*;
import software.siani.orchilla.operators.fuzzy.*;
import software.siani.orchilla.operators.hour.*;
import software.siani.orchilla.operators.lustrum.*;
import software.siani.orchilla.operators.millennium.*;
import software.siani.orchilla.operators.minute.*;
import software.siani.orchilla.operators.month.*;
import software.siani.orchilla.operators.second.*;
import software.siani.orchilla.operators.week.*;
import software.siani.orchilla.operators.weekday.LastWeekdayOperator;
import software.siani.orchilla.operators.weekday.NextWeekdayOperator;
import software.siani.orchilla.operators.weekday.SetWeekdayOperator;
import software.siani.orchilla.operators.weekend.*;
import software.siani.orchilla.operators.year.*;
import software.siani.orchilla.operators.CompositeTemporalOperator;
import software.siani.orchilla.operators.TemporalOperator;
import software.siani.orchilla.units.Month;
import software.siani.orchilla.units.Weekday;
import systems.intino.datamarts.subjectstore.SubjectStore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TemporalExpressionParser {
    private final SubjectStore subjectStore;

    public TemporalExpressionParser(SubjectStore subjectStore) {
        this.subjectStore = subjectStore;
    }


    public TemporalExpression parse(String string) {
        char[] charArray = string.toCharArray();
        List<TemporalOperator> predicates = new ArrayList<>();
        char previous;
        StringBuilder sb = null;
        for (int i = 0; i < charArray.length; i++) {
            previous = charArray[i!=0 ? i-1 : i];
            if (sb != null && charArray[i] != '>') sb.append(charArray[i]);
            if (previous == charArray[i] && charArray[i] == '>' || i == charArray.length - 1) {
                if (sb!=null) predicates.addAll(predicateFrom(sb.toString()));
                sb = new StringBuilder();
            }
        }
        return new TemporalExpression(context(string.substring(0, string.indexOf(">>"))), new CompositeTemporalOperator(predicates));
    }

    private TemporalTag context(String context) {
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
        if (context.startsWith("now")) return new TemporalTag(LocalDateTime.now(), LocalDateTime.now(), Period.Second, null);
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

    private List<TemporalOperator> predicateFrom(String predicate) {
        if (predicate.startsWith("start")) return List.of(new Early());
        if (predicate.startsWith("mid")) return List.of(new Mid());
        if (predicate.startsWith("end")) return List.of(new Late());
        if (predicate.startsWith("set")) return setFor(predicate);
        if (predicate.startsWith("add")) return addFor(predicate);
        if (predicate.startsWith("sub")) return subFor(predicate);
        if (predicate.startsWith("next")) return nextFor(predicate);
        if (predicate.startsWith("last")) return lastFor(predicate);
        throw new RuntimeException("Unknown operation " + predicate);
    }

    private List<TemporalOperator> setFor(String predicate) {
        String substring = predicate.substring(4);
        if (substring.startsWith("E")) return List.of(new SetEventTemporalOperator(substring.substring(1), subjectStore));
        List<TemporalOperator> result = new ArrayList<>();
        HashMap<String, Integer> arguments = argumentsFromBeginning(predicate.substring(4).toCharArray());
        if (arguments.containsKey("ML")) result.add(new SetMillenniumTemporalOperator(arguments.get("ML")));
        if (arguments.containsKey("C")) result.add(new SetCenturyTemporalOperator(arguments.get("C")));
        if (arguments.containsKey("D")) result.add(new SetDecadeTemporalOperator(arguments.get("D")));
        if (arguments.containsKey("Y")) result.add(new SetYearTemporalOperator(arguments.get("Y")));
        if (arguments.containsKey("M")) result.add(new SetMonthTemporalOperator(Month.values()[arguments.get("M")-1]));
        if (arguments.containsKey("W")) result.add(new SetWeekOperator(arguments.get("W")));
        if (arguments.containsKey("wd")) result.add(new SetWeekdayOperator(Weekday.values()[arguments.get("wd")-1]));
        if (arguments.containsKey("d")) result.add(new SetOrdinalDayTemporalOperator(arguments.get("d")));
        if (arguments.containsKey("dm")) result.add(new SetMonthDayTemporalOperator(arguments.get("dm")));
        if (arguments.containsKey("m")) result.add(new SetMinuteTemporalOperator(arguments.get("m")));
        if (arguments.containsKey("s")) result.add(new SetSecondTemporalOperator(arguments.get("s")));
        return result;
    }

    private List<TemporalOperator> addFor(String predicate) {
        String substring = predicate.substring(4);
        if (substring.endsWith("E")) {
            int idx = 0;
            while (idx < substring.length() && Character.isDigit(substring.charAt(idx))) idx++;
            return List.of(new AddEventTemporalOperator(Integer.parseInt(substring.substring(0, idx)), substring.substring(idx), subjectStore));
        }
        List<TemporalOperator> result = new ArrayList<>();
        HashMap<String, Integer> arguments = argumentsFromEnd(substring.toCharArray());
        if (arguments.containsKey("ML")) result.add(new AddMillenniumTemporalOperator(arguments.get("ML")));
        if (arguments.containsKey("C")) result.add(new AddCenturyTemporalOperator(arguments.get("C")));
        if (arguments.containsKey("D")) result.add(new AddDayTemporalOperator(arguments.get("D")));
        if (arguments.containsKey("L")) result.add(new AddLustrumTemporalOperator(arguments.get("L")));
        if (arguments.containsKey("Y")) result.add(new AddYearTemporalOperator(arguments.get("Y")));
        if (arguments.containsKey("M")) result.add(new AddMonthTemporalOperator(arguments.get("M")));
        if (arguments.containsKey("W")) result.add(new AddWeekTemporalOperator(arguments.get("W")));
        if (arguments.containsKey("WE")) result.add(new AddWeekendTemporalOperator(arguments.get("WE")));
        if (arguments.containsKey("d")) result.add(new AddDayTemporalOperator(arguments.get("d")));
        if (arguments.containsKey("h")) result.add(new AddHourTemporalOperator(arguments.get("h")));
        if (arguments.containsKey("m")) result.add(new AddMinuteTemporalOperator(arguments.get("m")));
        if (arguments.containsKey("s")) result.add(new AddSecondTemporalOperator(arguments.get("s")));
        return result;
    }

    private List<TemporalOperator> subFor(String predicate) {
        String substring = predicate.substring(4);
        if (substring.endsWith("E")) {
            int idx = 0;
            while (idx < substring.length() && Character.isDigit(substring.charAt(idx))) idx++;
            return List.of(new SubEventTemporalOperator(Integer.parseInt(substring.substring(0, idx)), substring.substring(idx), subjectStore));
        }
        List<TemporalOperator> result = new ArrayList<>();
        HashMap<String, Integer> arguments = argumentsFromEnd(substring.toCharArray());
        if (arguments.containsKey("ML")) result.add(new SubMillenniumTemporalOperator(arguments.get("ML")));
        if (arguments.containsKey("C")) result.add(new SubCenturyTemporalOperator(arguments.get("C")));
        if (arguments.containsKey("Y")) result.add(new SubYearTemporalOperator(arguments.get("Y")));
        if (arguments.containsKey("M")) result.add(new SubMonthTemporalOperator(arguments.get("M")));
        if (arguments.containsKey("W")) result.add(new SubWeekTemporalOperator(arguments.get("W")));
        if (arguments.containsKey("WE")) result.add(new SubWeekendTemporalOperator(arguments.get("WE")));
        if (arguments.containsKey("d")) result.add(new SubDayTemporalOperator(arguments.get("d")));
        if (arguments.containsKey("h")) result.add(new SubHourTemporalOperator(arguments.get("h")));
        if (arguments.containsKey("m")) result.add(new SubMinuteTemporalOperator(arguments.get("m")));
        if (arguments.containsKey("s")) result.add(new SubSecondTemporalOperator(arguments.get("s")));
        return result;
    }

    private List<TemporalOperator> nextFor(String predicate) {
        List<TemporalOperator> result = new ArrayList<>();
        HashMap<String, Integer> arguments = argumentsFromBeginning(predicate.substring(5).toCharArray());
        if (arguments.containsKey("M")) result.add(new NextMonthTemporalOperator(Month.values()[arguments.get("M")-1]));
        if (arguments.containsKey("wd")) result.add(new NextWeekdayOperator(Weekday.values()[arguments.get("wd")-1]));
        if (arguments.containsKey("d")) result.add(new NextDayTemporalOperator(arguments.get("d")));
        return result;
    }

    private List<TemporalOperator> lastFor(String predicate) {
        List<TemporalOperator> result = new ArrayList<>();
        HashMap<String, Integer> arguments = argumentsFromBeginning(predicate.substring(5).toCharArray());
        if (arguments.containsKey("M")) result.add(new LastMonthTemporalOperator(Month.values()[arguments.get("M")-1]));
        if (arguments.containsKey("wd")) result.add(new LastWeekdayOperator(Weekday.values()[arguments.get("wd")-1]));
        if (arguments.containsKey("d")) result.add(new LastDayTemporalOperator(arguments.get("d")));
        return result;
    }

    private HashMap<String, Integer> argumentsFromBeginning(char[] arguments) {
        HashMap<String, Integer> result = new HashMap<>();
        List<Character> chars = new LinkedList<>();
        StringBuilder nowArgument = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            if (Character.isDigit(arguments[i])) chars.add(arguments[i]);
            if (!Character.isDigit(arguments[i]) && !chars.isEmpty() || i == arguments.length - 1) {
                result.put(nowArgument.toString(), argumentOf(chars));
                chars = new ArrayList<>();
                nowArgument = new StringBuilder();
                nowArgument.append(arguments[i]);
                continue;
            }
            if (!Character.isDigit(arguments[i])) nowArgument.append(arguments[i]);
        }
        return result;
    }

    private HashMap<String, Integer> argumentsFromEnd(char[] arguments) {
        HashMap<String, Integer> result = new HashMap<>();
        List<Character> chars = new LinkedList<>();
        StringBuilder nowArgument = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            if (!Character.isDigit(arguments[i])) nowArgument.append(arguments[i]);
            if (Character.isDigit(arguments[i]) && !nowArgument.isEmpty() || i == arguments.length - 1) {
                result.put(nowArgument.toString(), argumentOf(chars));
                chars = new ArrayList<>();
                nowArgument = new StringBuilder();
                chars.add(arguments[i]);
                continue;
            }
            if (Character.isDigit(arguments[i])) chars.add(arguments[i]);
        }
        return result;
    }

    private int argumentOf(List<Character> chars) {
        return Integer.parseInt(chars.stream().map(String::valueOf).reduce((accum, c) -> accum + c).get());
    }
}
