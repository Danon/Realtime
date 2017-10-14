package ui.gfx;

import gameplay.*;
import gameplay.Point;
import gameplay.Rectangle;
import ui.gfx.resources.Resources;

import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;

public class Drawer {
    private final Graphics2D canvas;
    private Camera camera = new Camera();
    private boolean includeCamera = false;
    private final int windowWidth, windowHeight;

    public Drawer(Graphics2D canvas, int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.canvas = canvas;
    }

    public void useCamera(Camera camera) {
        this.camera = camera;
        this.includeCamera = true;
    }

    void useCamera(Point a) {
        this.camera = new Camera(a.getX(), a.getY());
        this.includeCamera = true;
    }

    public void useCamera() {
        this.includeCamera = true;
    }

    public void freeCamera() {
        this.includeCamera = false;
    }

    public Camera getCamera() {
        return camera;
    }

    private double serializeY(double y) {
        if (includeCamera) {
            y += camera.getY();
        }
        return y;
    }

    private double serializeX(double x) {
        if (includeCamera) {
            return x - camera.getY();
        }
        return x;
    }

    private Point serialized(Point p) {
        double x = p.x;
        double y = p.y;

        if (includeCamera) {
            x = x - camera.getX();
            y = y + camera.getY();
        }

        return new Point(x, y);
    }

    private Rectangle serialized(Rectangle r) {
        double x = r.x;
        double y = r.y;

        if (includeCamera) {
            x = r.x - camera.getX();
            y = r.y + camera.getY();
        }

        return new Rectangle(x, y, r.width, r.height);
    }

    private Oval serialized(Oval o) {
        double x = o.x;
        double y = o.y;

        if (includeCamera) {
            x = o.x - camera.getX();
            y = o.y + camera.getY();
        }
        return new Oval(new Point(x, y), o.getRadiusX(), o.getRadiusY());
    }

    public void text(String text, Point position) {
        Point pos = serialized(position);
        canvas.drawString(text, pos.getX(), pos.getY());
    }

    public void textFormat(String formatText, Point position, Object... args) {
        Point pos = serialized(position);
        canvas.drawString(String.format(formatText, args), pos.getX(), pos.getY());
    }

    public void line(Point a, Point b) {
        Point a2 = serialized(new Point(a)), b2 = serialized(new Point(b));
        canvas.drawLine(a2.getX(), a2.getY(), b2.getX(), b2.getY());
    }

    static class Bezier {
        static Point getControlPoint(Point start, Point crossing, Point end) {
            Point midPoint = new Point(
                    Math.min(start.x, end.x) + Math.abs(start.x - end.x) / 2,
                    Math.min(start.y, end.y) + Math.abs(start.y - end.y) / 2
            );

            return new Point(
                    crossing.x * 2 - midPoint.x,
                    crossing.y * 2 - midPoint.y
            );
        }
    }

    public void curveCross(Point a, Point b, Point c) {
        Point control = Bezier.getControlPoint(a, b, c);
        this.curve(a, control, c);
    }

    private void curve(Point a, Point b, Point c) {
        canvas.draw(new QuadCurve2D.Double(
                serializeX(a.x), serializeY(a.y),
                serializeX(b.x), serializeY(b.y),
                serializeX(c.x), serializeY(c.y)
        ));
    }

    public void borders(Rectangle r) {
        Rectangle rect = serialized(new Rectangle(r.x, r.y, r.width, r.height));
        canvas.drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void fill(Rectangle r) {
        Rectangle rect = serialized(new Rectangle(r.x, r.y, r.width, r.height));
        canvas.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void borders(Oval oval) {
        Oval o = serialized(new Oval(oval));
        canvas.drawOval(o.getX() - o.getRadiusX(), o.getY() - o.getRadiusY(), o.getRadiusX() * 2, o.getRadiusY() * 2);
    }

    public void fill(Oval oval) {
        Oval o = serialized(new Oval(oval));
        canvas.fillOval(o.getX() - o.getRadiusX(), o.getY() - o.getRadiusY(), o.getRadiusX() * 2, o.getRadiusY() * 2);
    }

    public void floor(Floor floor) {
        if (floor.getTiles() == 0) {
            return;
        }

        if (floor.getTiles() == 1) {
            image("ground0.png", new Point(floor.getLeft(), windowHeight - floor.getTop()), DrawFrom.RightTop);
        } else {
            image("ground1.png", new Point(floor.getLeft(), windowHeight - floor.getTop()), DrawFrom.RightTop);
            for (int i = 1; i < floor.getTiles() - 1; i++) {
                image("ground2.png", new Point(
                                floor.getLeft() + i * 32,
                                windowHeight - floor.getTop()),
                        DrawFrom.RightTop
                );
            }
            image("ground3.png", new Point(
                            floor.getLeft() + (floor.getTiles() - 1) * 32,
                            windowHeight - floor.getTop()),
                    DrawFrom.RightTop
            );
        }
    }

    public void ladder(Ladder ladder) {
        for (int i = 0; i < ladder.getHeightTiles(); i++) {
            image("ladder.png", new Point(
                            ladder.getLeft() - 4,
                            windowHeight - ladder.getBottom() - i * 32 - 10 + 11),
                    DrawFrom.RightBottom
            );
        }
    }

    public void background(java.awt.Color color) {
        canvas.setColor(color);
        canvas.fillRect(0, 0, windowWidth, windowHeight);
    }

    public void image(String name, Point position, DrawFrom drawFrom) {
        drawSimpleImage(Resources.getImageByName(name), new Point(position), drawFrom, Flip.None);
    }

    public void frame(ui.gfx.frame.Frame frame, Point position, DrawFrom drawFrom, Flip flip) {
        drawImageFrame(frame, new Point(position), drawFrom, flip);
    }

    private void drawImageFrame(ui.gfx.frame.Frame frame, Point pos, DrawFrom drawFrom, Flip flip) {
        if (canvas == null) {
            throw new UnsetCanvasException("No canvas has been specified.");
        }
        Point position = serialized(pos);
        BufferedImage img = Resources.getImageByName(frame.getSpritesheetName());
        int
                x = position.getX(),
                y = position.getY(),
                w1 = 0, w2 = frame.width,
                h1 = 0, h2 = frame.height;

        if (flip == Flip.Horizontally || flip == Flip.Both) {
            w1 = w2;
            w2 = 0;
            x -= frame.width;
            x += frame.offsetX;
        } else {
            x -= frame.offsetX;
        }
        if (flip == Flip.Vertically || flip == Flip.Both) {
            h1 = h2;
            h2 = 0;

            y -= frame.height;
            y += frame.offsetY;
        } else {
            y -= frame.offsetY;
        }

        y -= (drawFrom.y + 0.5) * frame.height;
        x -= (drawFrom.x + 0.5) * frame.width;

        canvas.drawImage(img,
                x + w1, y + h1,
                x + w2, y + h2,
                frame.x, frame.y,
                frame.x + frame.width, frame.y + frame.height,
                null);
        canvas.setColor(Color.black);
    }

    private void drawSimpleImage(BufferedImage img, Point pos, DrawFrom drawFrom, Flip flip) {
        if (canvas == null) {
            throw new UnsetCanvasException("No canvas has been specified.");
        }
        Point position = serialized(pos);
        int width = img.getWidth();
        int height = img.getHeight();
        int x = position.getX();
        int y = position.getY();

        x -= (drawFrom.x + 0.5) * width;
        y -= (drawFrom.y + 0.5) * height;

        switch (flip) {
            case Horizontally:
                x += width;
                width = -width;
                break;
            case Vertically:
                y += height;
                height = -height;
                break;
        }
        canvas.drawImage(img, x, y, width, height, null);
    }
}
