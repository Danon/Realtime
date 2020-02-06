package gameplay;

public class Oval implements Shape {
    public double x, y;
    private double radiusX, radiusY;

    public Oval(Oval o) {
        this.x = o.x;
        this.y = o.y;
        this.radiusX = o.radiusX;
        this.radiusY = o.radiusY;
    }

    public Oval(Point pos, int radius) {
        this(pos, radius, radius);
    }

    public Oval(Point pos, int radiusX, int radiusY) {
        this.x = pos.x;
        this.y = pos.y;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getRadiusX() {
        return (int) radiusX;
    }

    public int getRadiusY() {
        return (int) radiusY;
    }

    @Override
    public boolean contains(Point p) {
        return Math.pow(p.x - this.x, 2) / Math.pow(this.radiusX, 2) +
                Math.pow(p.y - this.y, 2) / Math.pow(this.radiusY, 2) <= 1;
    }
}
