package ui.gfx;

public class DrawFrom extends RatioPoint {
    private DrawFrom(double x, double y) {
        super(x, y);
    }

    final static DrawFrom MiddleTop = new DrawFrom(0.0, -0.5);
    final public static DrawFrom Center = new DrawFrom(0.0, 0.0);
    final public static DrawFrom MiddleBottom = new DrawFrom(0.0, 0.5);

    final static DrawFrom RightTop = new DrawFrom(-0.5, -0.5);
    final public static DrawFrom RightMiddle = new DrawFrom(-0.5, 0.0);
    final static DrawFrom RightBottom = new DrawFrom(-0.5, 0.5);
}
