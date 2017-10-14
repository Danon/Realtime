package gameplay;

import ui.gfx.IntTransition;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Point {
    public double x, y;

    public Point() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point a) {
        this.x = a.x;
        this.y = a.y;
    }

    public void copy(Point a) {
        this.x = a.x;
        this.y = a.y;
    }

    public Point copy() {
        return new Point(this.x, this.y);
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public double capX(double minValue, double maxValue) {
        return min(max(x, minValue), maxValue);
    }

    public double capY(double minValue, double maxValue) {
        return min(max(y, minValue), maxValue);
    }

    public Point invertY(int axis) {
        return new Point(this.x, axis - this.y);
    }

    public Point negY() {
        return new Point(this.x, -this.y);
    }

    public Point negX() {
        return new Point(-this.x, this.y);
    }

    public Point neg() {
        return new Point(-this.x, -this.y);
    }

    public Point subX(double x) {
        return new Point(this.x - x, this.y);
    }

    public Point subY(double y) {
        return new Point(this.x, this.y - y);
    }

    public Point sub(double x, double y) {
        return new Point(this.x - x, this.y - y);
    }

    public Point sub(Point a) {
        return new Point(this.x - a.x, this.y - a.y);
    }

    public Point addY(double y) {
        return new Point(this.x, this.y + y);
    }

    public Point add(double x, double y) {
        return new Point(this.x + x, this.y + y);
    }

    public Point add(Point p) {
        return new Point(this.x + p.x, this.y + p.y);
    }

    private double powDistanceTo(Point A) {
        return Math.pow(this.x - A.x, 2) + Math.pow(this.y - A.y, 2);
    }

    public double distanceTo(Point A) {
        return Math.sqrt(powDistanceTo(A));
    }

    public boolean distanceBiggerThan(Point A, double distance) {
        return this.powDistanceTo(A) > distance * distance;
    }

    public Angle angleOf(Point a) {
        if (this.equals(a)) {
            throw new IllegalArgumentException("Points cannot be equal.");
        }
        double angle = Math.acos((this.y - a.y) / this.distanceTo(a));
        return new Angle(Math.PI + ((a.x > this.x) ? -angle : angle));
    }

    public Point find(double distance, Angle angle) {
        return new Point(
                this.x + distance * Math.sin(angle.getValue()),
                this.y + distance * Math.cos(angle.getValue())
        );
    }

    public boolean isInside(Shape s) {
        return s.contains(this);
    }

    @Override
    public String toString() {
        return String.format(" Point: %d / %d", (int) this.x, (int) this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return (this.x == p.x && this.y == p.y);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    public static class Transition {
        public static Point linear(Point a, Point b, double transitionValue) {
            return new Point(
                    IntTransition.linear(a.x, b.x, transitionValue),
                    IntTransition.linear(a.y, b.y, transitionValue)
            );
        }

        public static Point cosine(Point a, Point b, double transitionValue) {
            return new Point(
                    IntTransition.cosine(a.x, b.x, transitionValue),
                    IntTransition.cosine(a.y, b.y, transitionValue)
            );
        }

        public static Point cubic(Point a, Point b, Point c, Point d, double transitionValue) {
            return new Point(
                    IntTransition.cubic(a.x, b.x, c.x, d.x, transitionValue),
                    IntTransition.cubic(a.y, b.y, c.y, d.y, transitionValue)
            );
        }
    }
}
