package gameplay;

public class Ladder {
    private final static int WIDTH = 24;

    private final int left, bottom, tiles;

    public Ladder(int left, int bottom, int tiles) {
        this.left = left;
        this.bottom = bottom;
        this.tiles = tiles;
    }

    public int getLeft() {
        return left;
    }

    int getCenter() {
        return left + WIDTH / 2;
    }

    public int getRight() {
        return left + WIDTH;
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
