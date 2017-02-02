package ui.gfx;

import gameplay.Point;
import gameplay.Rectangle;

import static ui.gfx.Renderer.*;

public class Camera extends Point {
    public Camera() {
        super();
    }

    public Camera(Point point) {
        super(point);
    }

    public void cap(Rectangle borders, int windowWidth, int windowHeight) {
        this.minX(-CAMERA_SIDE_MARGIN);
        this.maxX(borders.width - windowWidth + CAMERA_SIDE_MARGIN);
        this.minY(-CAMERA_BOTTOM_MARGIN);
        this.maxY(borders.height - windowHeight + CAMERA_TOP_MARGIN);
    }

    public void moveX(double i) {
        this.x += i;
    }

    public void moveY(double i) {
        this.y += i;
    }
}
