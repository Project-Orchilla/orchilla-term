package software.orchilla;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class EngineOutputDTO {
    private final String head;
    private final String end;
    private final Distribution distribution;
    private final int duration;
    private final String period;

    public EngineOutputDTO(String head, String end, Distribution distribution, int duration, String period) {
        this.head = head;
        this.end = end;
        this.distribution = distribution;
        this.duration = duration;
        this.period = period;
    }

    private LocalDateTime unformatString(String formatted) {
        formatted = formatted.split("\\.")[0];
        return LocalDateTime.parse(formatted, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm[:ss]")
                .withZone(ZoneId.systemDefault()));
    }

    public LocalDateTime head() {
        return unformatString(head);
    }

    public LocalDateTime end() {
        return unformatString(end);
    }

    public Distribution distribution() {
        return distribution;
    }

    public int duration() {
        return duration;
    }

    public String period() {
        return period;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EngineOutputDTO) obj;
        return Objects.equals(this.head, that.head) &&
                Objects.equals(this.end, that.end) &&
                Objects.equals(this.distribution, that.distribution) &&
                this.duration == that.duration &&
                Objects.equals(this.period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, end, distribution, duration, period);
    }

    @Override
    public String toString() {
        return "EngineOutputDTO[" +
                "head=" + head + ", " +
                "end=" + end + ", " +
                "distribution=" + distribution + ", " +
                "duration=" + duration + ", " +
                "period=" + period + ']';
    }


    public record Distribution(String name, int lowerBound, int upperBound) {

    }
}