package ui.gfx.frame;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Frame {
    @Getter
    private final String name;

    public final int x, y;
    public final int width, height;
    public final int offsetX, offsetY;

    public Frame(String name, int x, int y, int width, int height, int offsetX) {
        this(name, x, y, width, height, offsetX, 0);
    }

    public Frame(String name, int x, int y, int width, int height) {
        this(name, x, y, width, height, 0, 0);
    }
}
