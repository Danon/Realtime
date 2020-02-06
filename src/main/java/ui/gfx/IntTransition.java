package ui.gfx;

public class IntTransition {
    public static double linear(double a, double b, double transition) {
        return a * (1.0 - transition) + b * transition;
    }

    public static double cosine(double a, double b, double transition) {
        double cosineTransition = (1 - Math.cos(transition * Math.PI)) / 2;
        return a * (1 - cosineTransition) + b * cosineTransition;
    }

    public static double cubic(double a, double b, double c, double d, double transition) {
        double a0, a1, a2, a3, transition2;

        transition2 = transition * transition;
        a0 = d - c - a + b;
        a1 = a - b - a0;
        a2 = c - a;
        a3 = b;

        return (a0 * transition * transition2 + a1 * transition2 + a2 * transition + a3);
    }
}
