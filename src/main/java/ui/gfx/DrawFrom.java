package ui.gfx;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DrawFrom {
    final double x, y;

    public static final DrawFrom MiddleTop = new DrawFrom(0.0, -0.5);
    public static final DrawFrom Center = new DrawFrom(0.0, 0.0);
    public static final DrawFrom MiddleBottom = new DrawFrom(0.0, 0.5);
    public static final DrawFrom RightTop = new DrawFrom(-0.5, -0.5);
    public static final DrawFrom RightMiddle = new DrawFrom(-0.5, 0.0);
    public static final DrawFrom RightBottom = new DrawFrom(-0.5, 0.5);
}
