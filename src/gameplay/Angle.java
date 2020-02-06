package gameplay;

import lombok.RequiredArgsConstructor;

import static java.lang.Math.toDegrees;

@RequiredArgsConstructor
public final class Angle {
    private final double angle;

    public double getValue() {
        return angle;
    }

    private double getDegreeValue() {
        return toDegrees(angle);
    }

    public Angle add(Angle a) {
        return new Angle(this.angle + a.angle);
    }

    @Override
    public String toString() {
        return "Angle: " + String.format("%.2f", getDegreeValue()) + " degrees";
    }
}
