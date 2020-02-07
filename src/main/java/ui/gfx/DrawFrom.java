package ui.gfx;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DrawFrom {
    final double x, y;

    public static final DrawFrom RightTop = new DrawFrom(0.5, 0.0);

    public static final DrawFrom MiddleTop = new DrawFrom(0.0, 0.0);
    public static final DrawFrom MiddleBottom = new DrawFrom(0.0, 1.0);
}
