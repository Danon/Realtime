package ui.gfx;

class RotatePoint extends RatioPoint {
    private RotatePoint(double x, double y) {
        super(x, y);
    }

    static RotatePoint Center = new RotatePoint(0.0, 0.0);
}
