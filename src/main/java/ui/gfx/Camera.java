package ui.gfx;

import gameplay.Point;
import gameplay.Rectangle;

import static ui.gfx.Renderer.*;

public class Camera {
    private Point point;

    public Camera() {
        this.point = new Point();
    }

    public Camera(int x, int y) {
        this.point = new Point(x, y);
    }

    public void cap(Rectangle borders, int windowWidth, int windowHeight) {
        this.point = new Point(
                point.capX(-CAMERA_SIDE_MARGIN, borders.width - windowWidth + CAMERA_SIDE_MARGIN),
                point.capY(-CAMERA_BOTTOM_MARGIN, borders.height - windowHeight + CAMERA_TOP_MARGIN)
        );
    }

    public int getX() {
        return this.point.getX();
    }

    public int getY() {
        return this.point.getY();
    }
}
