package ui.gfx.frame;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class Frame {
    final private String spritesheetName;

    final public int x, y;
    final public int width, height;
    final public int offsetX, offsetY;

    public Frame(String spritesheetName, int x, int y, int width, int height, int offsetX, int offsetY) {
        this.spritesheetName = spritesheetName;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Frame(String spritesheetName, int x, int y, int width, int height, int offsetX) {
        this(spritesheetName, x, y, width, height, offsetX, 0);
    }

    public Frame(String spritesheetName, int x, int y, int width, int height) {
        this(spritesheetName, x, y, width, height, 0, 0);
    }

    public String getSpritesheetName() {
        return spritesheetName;
    }
}
