package software.siani.orchilla.model.distributions;

import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Duration;

import java.time.LocalDateTime;

public record ConstantDistribution(long lowerBound, long upperBound) implements Distribution {

    @Override
    public Distribution between(LocalDateTime start, LocalDateTime end) {
        return new ConstantDistribution(0, Duration.from(start, end).seconds());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ConstantDistribution that = (ConstantDistribution) o;
        return upperBound() == that.upperBound() && lowerBound() == that.lowerBound();
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(upperBound());
        result = 31 * result + Long.hashCode(lowerBound());
        return result;
    }

    @Override
    public String toString() {
        return "ConstantDistribution{" +
                "lowerBound=" + lowerBound +
                ", upperBound=" + upperBound +
                '}';
    }
}
