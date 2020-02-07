package gameplay;

import lombok.EqualsAndHashCode;

import static java.lang.Math.max;
import static java.lang.Math.min;

@EqualsAndHashCode
public class Point {
    public final double x;
    public final double y;

    public Point() {
        this(0.0, 0.0);
    }

    public Point(Point point) {
        this(point.x, point.y);
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
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

    public Point sub(double x, double y) {
        return new Point(this.x - x, this.y - y);
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
}
