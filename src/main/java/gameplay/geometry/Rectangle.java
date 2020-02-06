package gameplay.geometry;

import gameplay.Point;

public class Rectangle implements Shape {
    public double x, y;
    public double width, height;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Point p, double width, double height) {
        this(p.x, p.y, width, height);
    }

    public int getWidth() {
        return (int) width;
    }

    public int getHeight() {
        return (int) height;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    @Override
    public boolean contains(Point point) {
        return this.x <= point.x &&
                this.y <= point.y &&
                this.x + this.width >= point.x &&
                this.y + this.height >= point.y;
    }
} 
