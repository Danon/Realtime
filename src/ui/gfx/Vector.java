package ui.gfx;

import gameplay.Point;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class Vector extends Point {
    public Vector(double x, double y) {
        super(x, y);
    }

    Vector(DirectionalVector vector) {
        this(
                vector.length * Math.sin(vector.getDirection()),
                vector.length * Math.cos(vector.getDirection())
        );
    }

    Vector(Point point) {
        super(point);
    }

    public Vector sub(Point p) {
        return new Vector(this.x - p.x, this.y - p.y);
    }

    public Vector add(double x, double y) {
        return new Vector(this.x + x, this.y + y);
    }

    @Override
    public Vector add(Point a) {
        return new Vector(this.x + a.x, this.y + a.y);
    }

    public static Vector transition(Vector a, Vector b, double transitionValue) {
        Vector diff = b.sub(a);
        return a.add(diff.x * transitionValue, diff.y * transitionValue);
    }
}
