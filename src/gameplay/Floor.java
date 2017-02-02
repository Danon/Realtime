package gameplay;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Immutable object containing information about particular floor.
 * 
 * @author Danio
 * @version 1.0 05/04/2015
 */
public class Floor
{
    private final int left, top, tiles;
    
    public final static int HEIGHT = 32;
    
    public Floor (int left, int top, int tiles)
    {
        this.left = left;
        this.top = top;
        this.tiles = tiles;
    }

    public int getLeft() {
        return this.left;
    }

    public int getRight() {
        return this.left + tiles*32;
    }

    public int getWidth() { return this.tiles*32; }

    public int getHeight() { return Floor.HEIGHT; }

    public int getTop() {
        return this.top;
    }
    
    public int getBottom() {
        return this.top - Floor.HEIGHT;
    }

    public int getTiles() {
        return this.tiles;
    }

    public Rectangle2D asShape() {
        return new Rectangle2D.Float(this.getLeft(), this.getTop(), this.getWidth(), this.getHeight());
    }

    public ArrayList<Point> getCorners() {
        ArrayList<Point> corners = new ArrayList<>(4);
        corners.add(new Point(this.getLeft(), this.getTop()));
        corners.add(new Point(this.getRight(), this.getTop()));
        corners.add(new Point(this.getRight(), this.getBottom()));
        corners.add(new Point(this.getLeft(), this.getBottom()));
        return corners;
    }
}
