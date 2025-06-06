package software.siani.orchilla.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class TemporalTag {
    private final LocalDateTime head;
    private final LocalDateTime tail;
    private final Period period;
    private final long duration;
    private final Distribution distribution;

    public TemporalTag(LocalDateTime head, LocalDateTime tail, Period period, Distribution distribution) {
        this.head = head;
        this.tail = tail;
        this.period = period;
        this.duration = Duration.from(head, tail).seconds();
        this.distribution = distribution;
    }

    public LocalDateTime head() {
        return head;
    }

    public LocalDateTime tail() {
        return tail;
    }

    public long duration() {
        return duration;
    }

    public Period period() {
        return period;
    }

    public Distribution distribution() {
        return distribution;
    }

    public void graph() {

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TemporalTag that = (TemporalTag) o;
        return duration == that.duration &&
                Objects.equals(head, that.head) &&
                Objects.equals(tail, that.tail) &&
                Objects.equals(period, that.period) &&
                Objects.equals(distribution, that.distribution);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(head);
        result = 31 * result + Objects.hashCode(tail);
        result = 31 * result + Long.hashCode(duration);
        result = 31 * result + Objects.hashCode(period);
        result = 31 * result + Objects.hashCode(distribution);
        return result;
    }

    @Override
    public String toString() {
        return "TemporalTag{" +
                "start=" + head +
                ", end=" + tail +
                ", period=" + period +
                ", duration=" + duration +
                ", distribution=" + distribution +
                '}';
    }
}
