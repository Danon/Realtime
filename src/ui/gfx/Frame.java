package ui.gfx;

public class Frame {
    private Spritesheet parentSpritesheet;

    final int x;
    final int y;
    final int width;
    final int height;
    final int offsetX;
    final int offsetY;

    public Frame(int x, int y, int width, int height, int offset, int offsetY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.offsetX = offset;
        this.offsetY = offsetY;
    }

    public Frame(int x, int y, int width, int height, int offset) {
        this(x, y, width, height, offset, 0);
    }

    public Frame(int x, int y, int width, int height) {
        this(x, y, width, height, 0, 0);
    }

    String getSpritesheetName() {
        return parentSpritesheet.getName();
    }

    void setSpritesheet(Spritesheet spritesheet) {
        this.parentSpritesheet = spritesheet;
    }
}
