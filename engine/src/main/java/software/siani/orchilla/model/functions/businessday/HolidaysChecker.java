package software.siani.orchilla.model.functions.businessday;

import java.io.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HolidaysChecker {
    private static final Set<LocalDate> holidays = loadHolidays();
    private static final File holidaysFile = new File("holidays.txt");

    private static Set<LocalDate> loadHolidays() {
        try {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(holidaysFile)));
            HashSet<LocalDate> result = new HashSet<>();
            while (!bufferedReader.ready()) result.add(parse(bufferedReader.readLine()));
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static LocalDate parse(String s) {
        s = s.trim();
        int year = Integer.parseInt(s.substring(0, 4));
        int month = Integer.parseInt(s.substring(5, 7));
        int day = Integer.parseInt(s.substring(8, 10));
        return LocalDate.of(year, month, day);
    }

    public static boolean isHoliday(LocalDate date) {
        return holidays.contains(date);
    }
}
