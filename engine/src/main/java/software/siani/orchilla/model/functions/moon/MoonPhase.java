package software.siani.orchilla.model.functions.moon;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public enum MoonPhase {
    NewMoon,
    WaxingCrescent,
    FirstQuarter,
    WaxingGibbous,
    FullMoon,
    WaningGibbous,
    LastQuarter,
    WaningCrescent;

    private static final LocalDate KnownNewMoon = LocalDate.of(2000, 1, 6); // Reference date
    private static final double SynodicMonth = 29.530588; // days

    public static List<Integer> getDaysForMoonPhase(int year, int month, MoonPhase phase) {
        List<Integer> matchingDays = new ArrayList<>();
        LocalDate start = LocalDate.of(year, month, 1);
        for (int day = 1; day <= start.lengthOfMonth(); day++) {
            LocalDate current = LocalDate.of(year, month, day);
            double age = calculateMoonAge(current);
            MoonPhase currentPhase = getMoonPhaseFromAge(age);
            if (currentPhase == phase) matchingDays.add(day);
        }
        return matchingDays;
    }

    private static double calculateMoonAge(LocalDate date) {
        return (ChronoUnit.DAYS.between(KnownNewMoon, date) % SynodicMonth + SynodicMonth) % SynodicMonth;
    }

    private static MoonPhase getMoonPhaseFromAge(double age) {
        if (age < 1.84566) return MoonPhase.NewMoon;
        else if (age < 5.53699) return MoonPhase.WaxingCrescent;
        else if (age < 9.22831) return MoonPhase.FirstQuarter;
        else if (age < 12.91963) return MoonPhase.WaxingGibbous;
        else if (age < 16.61096) return MoonPhase.FullMoon;
        else if (age < 20.30228) return MoonPhase.WaningGibbous;
        else if (age < 23.99361) return MoonPhase.LastQuarter;
        else if (age < 27.68493) return MoonPhase.WaningCrescent;
        else return MoonPhase.NewMoon;
    }
}