package gameplay;

public class Oval extends Shape {
    public double x, y;
    private double radiusX, radiusY;

    public Oval(Oval o) {
        this.x = o.x;
        this.y = o.y;
        this.radiusX = o.radiusX;
        this.radiusY = o.radiusY;
    }

    public Oval(Point pos, int radiusX, int radiusY) {
        this.x = pos.x;
        this.y = pos.y;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }

    public Oval(Point pos, int radius) {
        this(pos, radius, radius);
    }

    public void shrink(double count) {
        radiusX -= count;
        radiusY -= count;
    }

    public void expand(double count) {
        radiusX += count;
        radiusY += count;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public int getRadiusX() {
        return (int) radiusX;
    }

    public int getRadiusY() {
        return (int) radiusY;
    }

    @Override
    boolean contains(Point p) {
        return Math.pow(p.x - this.x, 2) / Math.pow(this.radiusX, 2) +
                Math.pow(p.y - this.y, 2) / Math.pow(this.radiusY, 2) <= 1;
    }

}
