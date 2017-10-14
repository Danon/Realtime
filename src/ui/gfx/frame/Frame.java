package ui.gfx.frame;

public class Frame {
    private Spritesheet parentSpritesheet;

    final public int x, y;
    final public int width, height;
    final public int offsetX, offsetY;

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

    public String getSpritesheetName() {
        return parentSpritesheet.getName();
    }

    void setSpritesheet(Spritesheet spritesheet) {
        this.parentSpritesheet = spritesheet;
    }
}
