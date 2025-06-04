package software.siani.orchilla.distributions;

import software.siani.orchilla.Distribution;
import software.siani.orchilla.Duration;

import java.time.LocalDateTime;

public record GeneralizedBetaDistribution(double upperBound, double lowerBound, double alpha, double beta) implements Distribution {

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
}
