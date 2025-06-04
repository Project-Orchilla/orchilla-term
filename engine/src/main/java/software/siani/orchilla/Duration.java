package software.siani.orchilla;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static software.siani.orchilla.Duration.DurationUnit.*;

public class Duration {
    private final long seconds;

    public Duration(long seconds) {
        this.seconds = seconds;
    }

    public static Duration from(LocalDateTime start, LocalDateTime end) {
        return new Duration(ChronoUnit.SECONDS.between(start, end));
    }

    public DurationUnit unit() {
        if (seconds < 60) return Second;
        if (seconds < 60 * 60) return Minute;
        if (seconds < 60 * 60 * 24) return Hour;
        if (seconds < 60 * 60 * 24 * 30) return Day;
        if (seconds < 60 * 60 * 24 * 30 * 12) return Month;
        return Year;
    }

    public long seconds() {
        return seconds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Duration duration = (Duration) o;
        return seconds == duration.seconds;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(seconds);
    }

    @Override
    public String toString() {
        return "Duration{" +
                "seconds=" + seconds +
                '}';
    }

    public enum DurationUnit {
        Second, Minute, Hour, Day, Week, Month, Year
    }
}
