package gameplay.geometry;

import gameplay.Point;
import gameplay.Shape;

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
    public boolean contains(Point p) {
        return this.x <= p.x &&
                this.y <= p.y &&
                this.x + this.width >= p.x
                && this.y + this.height >= p.y;
    }
} 
