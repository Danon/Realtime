package gameplay;

import java.awt.geom.Rectangle2D;

public class Floor {
    public final static int HEIGHT = 32;

    private final int left, top, tiles;

    public Floor(int left, int top, int tiles) {
        this.left = left;
        this.top = top;
        this.tiles = tiles;
    }

    public int getLeft() {
        return this.left;
    }

    public int getRight() {
        return this.left + tiles * 32;
    }

    public int getWidth() {
        return this.tiles * 32;
    }

    public int getHeight() {
        return Floor.HEIGHT;
    }

    public int getTop() {
        return this.top;
    }

    int getBottom() {
        return this.top - Floor.HEIGHT;
    }

    public int getTiles() {
        return this.tiles;
    }

    public Rectangle2D asShape() {
        return new Rectangle2D.Float(this.getLeft(), this.getTop(), this.getWidth(), this.getHeight());
    }
}
