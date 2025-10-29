package software.siani.orchilla.model.distributions;

import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Duration;

import java.time.LocalDateTime;
import java.util.Objects;

public final class GeneralizedBetaDistribution implements Distribution {
    private String name;
    private final double upperBound;
    private final double lowerBound;
    private final double alpha;
    private final double beta;

    public GeneralizedBetaDistribution(double upperBound, double lowerBound, double alpha, double beta) {
        this.name = "beta";
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public Distribution between(LocalDateTime start, LocalDateTime end) {
        return new GeneralizedBetaDistribution(0, Duration.from(start, end).seconds(), alpha, beta);
    }

    @Override
    public String toString() {
        return "GeneralizedBetaDistribution{" +
                "upperBound=" + upperBound +
                ", lowerBound=" + lowerBound +
                ", alpha=" + alpha +
                ", beta=" + beta +
                '}';
    }

    public String name() {
        return name;
    }

    public double upperBound() {
        return upperBound;
    }

    public double lowerBound() {
        return lowerBound;
    }

    public double alpha() {
        return alpha;
    }

    public double beta() {
        return beta;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GeneralizedBetaDistribution) obj;
        return Double.doubleToLongBits(this.upperBound) == Double.doubleToLongBits(that.upperBound) &&
                Double.doubleToLongBits(this.lowerBound) == Double.doubleToLongBits(that.lowerBound) &&
                Double.doubleToLongBits(this.alpha) == Double.doubleToLongBits(that.alpha) &&
                Double.doubleToLongBits(this.beta) == Double.doubleToLongBits(that.beta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upperBound, lowerBound, alpha, beta);
    }

}
