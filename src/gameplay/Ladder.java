package gameplay;

public class Ladder {
    private final int left, bottom, tiles;
    private final static int Width = 24;

    public Ladder(int left, int bottom, int tiles) {
        this.left = left;
        this.bottom = bottom;
        this.tiles = tiles;
    }

    public int getLeft() {
        return left;
    }

    int getCenter() {
        return left + Width / 2;
    }

    public int getRight() {
        return left + Width;
    }

    public int getBottom() {
        return bottom;
    }

    public int getHeightTiles() {
        return tiles;
    }

    public int getHeight() {
        return tiles * 32;
    }

    public int getPeek() {
        return bottom + tiles * 32;
    }
}
