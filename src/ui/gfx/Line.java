package ui.gfx;

import gameplay.Point;

import java.awt.geom.Line2D;

/**
 * @author Danio
 * @since 1.0
 */
public class Line
{
    public static boolean intersects(Point a, Point b, gameplay.Rectangle rect)
    {
        if (rect.contains(a)) { return true; }
        if (rect.contains(b)) { return true; }


        return new Line2D.Float(a.getX(), a.getY(), b.getX(), b.getY()).intersects(
                rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()
        );
    }
}
