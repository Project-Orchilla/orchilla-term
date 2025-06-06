package software.siani.orchilla.model.distributions;

import software.siani.orchilla.model.Distribution;
import software.siani.orchilla.model.Duration;

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
