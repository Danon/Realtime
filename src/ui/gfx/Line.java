package ui.gfx;

import gameplay.Point;
import gameplay.Rectangle;

import java.awt.geom.Line2D;

public class Line {
    public static boolean intersects(Point a, Point b, Rectangle rect) {
        if (rect.contains(a)) {
            return true;
        }
        if (rect.contains(b)) {
            return true;
        }

        Line2D.Float line = new Line2D.Float(a.getX(), a.getY(), b.getX(), b.getY());

        return line.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
}
