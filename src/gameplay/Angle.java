package gameplay;

import ui.gfx.IntTransition;

public final class Angle {
    private double angle;

    public Angle() {
        this(0);
    }

    public Angle(double value) {
        this.angle = value;
    }

    public Angle(int Value) {
        setDegreeValue(Value);
    }

    public void setValue(double value) {
        angle = value;
    }

    private void setDegreeValue(double value) {
        angle = value / 180 * Math.PI;
    }

    public double getValue() {
        return angle;
    }

    private double getDegreeValue() {
        return angle * 180.0 / Math.PI;
    }

    public double getDecValue() {
        return angle / (2 * Math.PI);
    }

    public void incValue(double value) {
        angle += value;
    }

    public void decValue(double value) {
        angle -= value;
    }

    public void incDegreeValue(double value) {
        angle += value / 180 * Math.PI;
    }

    public Angle copy() {
        return new Angle(angle);
    }

    public Angle add(Angle a) {
        return new Angle(this.angle + a.angle);
    }

    public Angle sub(Angle a) {
        return new Angle(this.angle - a.angle);
    }

    public static class Transition {
        public static Angle linear(Angle beginning, Angle end, double transitionValue) {
            double a = beginning.getValue(),  // 350
                    b = end.getValue();       // 0

            if (b - a > Math.PI) a += Math.PI * 2;       // 370
            if (a - b > Math.PI) b += Math.PI * 2;

            return new Angle(IntTransition.linear(a, b, transitionValue));
        }

        public static Angle cosine(Angle beginning, Angle end, double transitionValue) {
            double a = beginning.getValue(),  // 350
                    b = end.getValue();       // 0

            if (b - a > Math.PI) a += Math.PI * 2;       // 370
            if (a - b > Math.PI) b += Math.PI * 2;

            return new Angle(IntTransition.cosine(a, b, transitionValue));
        }

        public static Angle cubic(Angle beginning, Angle end, Angle a, Angle b, double transitionValue) {
            double[] values = {beginning.getValue(), end.getValue(), a.getValue(), b.getValue()};
            for (int i = 1; i < values.length; i++) {
                if (values[i - 1] - values[i] > Math.PI) {
                    for (int j = i; j < values.length; j++) {
                        values[j] += 2 * Math.PI;
                    }
                } else if (values[i] - values[i - 1] > Math.PI) {
                    for (int j = 0; j < i; j++) {
                        values[j] += 2 * Math.PI;
                    }
                }
            }

            return new Angle(IntTransition.cubic(values[0], values[1], values[2], values[3], transitionValue));
        }
    }

    @Override
    public String toString() {
        return "Angle: " + String.format("%.2f", getDegreeValue()) + " degrees";
    }
}
