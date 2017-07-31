package ui.gfx;

import gameplay.*;
import gameplay.Point;
import gameplay.Rectangle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;

public class Drawer {
    public static boolean invertPlane = false;
    public static int invertValue = 500;

    private Graphics2D canvas;

    private Camera camera;
    private boolean includeCamera = false;

    private final int windowWidth;
    private final int windowHeight;

    public Drawer(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.camera = new Camera();
    }

    public void setCanvas(Graphics2D canvas) {
        this.canvas = canvas;
    }

    public void useCamera(Camera camera) {
        this.camera = camera;
        this.includeCamera = true;
    }

    void useCamera(Point a) {
        useCamera(a.getX(), a.getY());
    }

    private void useCamera(int x, int y) {
        this.camera.set(x, y);
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
        if (invertPlane) {
            y = invertValue - y;
        }
        return y;
    }

    private double serializeX(double x) {
        if (includeCamera) {
            return x - camera.getY();
        }
        return x;
    }

    private void serialize(Point p) {
        if (includeCamera) {
            p.x -= camera.getX();
            p.y += camera.getY();
        }
        if (invertPlane) {
            p.y = invertValue - p.y;
        }
    }

    private void serialize(Rectangle r) {
        if (includeCamera) {
            r.x -= camera.getX();
            r.y += camera.getY();
        }
        if (invertPlane) {
            r.y = invertValue - r.y;
        }
    }

    private void serialize(Oval o) {
        if (includeCamera) {
            o.x -= camera.getX();
            o.y += camera.getY();
        }
        if (invertPlane) {
            o.y = invertValue - o.y;
        }
    }


    /**
     * Draws a text on the canvas.
     * If getCamera is setValues, position will be changed accordingly.
     *
     * @param text     Text to be drawn
     * @param position Postion of the text.
     */
    public void text(String text, Point position) {
        serialize(position);
        canvas.drawString(text, position.getX(), position.getY());
    }

    /**
     * Draws a formatted text on the canvas (using {@link java.lang.String#format(java.lang.String, java.lang.Object...) ormat()}.
     * If getCamera is setValues, position will be changed accordingly.
     *
     * @param formatText Format text to be drawn
     * @param position   Postion of the text.
     * @param args       Arguments to be formatted.
     */
    public void textFormat(String formatText, Point position, Object... args) {
        serialize(position);
        canvas.drawString(String.format(formatText, args), position.getX(), position.getY());
    }

    /*
     * Draws a line.
     * If getCamera is setValues, position will be changed accordingly.
     */
    public void line(Point a, Point b) {
        Point a2 = new Point(a), b2 = new Point(b);
        serialize(a2);
        serialize(b2);
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
        this.curve(a.x, a.y, control.x, control.y, c.x, c.y);
    }

    public void curve(Point a, Point b, Point c) {
        canvas.draw(new QuadCurve2D.Double(
                serializeX(a.x), serializeY(a.y),
                serializeX(b.x), serializeY(b.y),
                serializeX(c.x), serializeY(c.y)
        ));
    }

    private void curve(double x1, double y1, double x2, double y2, double x3, double y3) {
        canvas.draw(new QuadCurve2D.Double(
                serializeX(x1), serializeY(y1),
                serializeX(x2), serializeY(y2),
                serializeX(x3), serializeY(y3)
        ));
    }

    /**
     * Draws a rectangle.
     * If getCamera is setValues, position will be changed accordingly.
     *
     * @param r Rectangle to be drawn.
     */
    public void borders(Rectangle r) {
        Rectangle r2 = new Rectangle(r.x, r.y, r.width, r.height);
        serialize(r2);
        canvas.drawRect(r2.getX(), r2.getY(), r2.getWidth(), r2.getHeight());
    }

    /**
     * Fills a rectangle.
     * If getCamera is setValues, position will be changed accordingly.
     *
     * @param r Rectangle to be drawn.
     */
    public void fill(Rectangle r) {
        Rectangle r2 = new Rectangle(r.x, r.y, r.width, r.height);
        serialize(r2);
        canvas.fillRect(r2.getX(), r2.getY(), r2.getWidth(), r2.getHeight());
    }

    /**
     * Draws an oval.
     * If getCamera is setValues, position will be changed accordingly.
     *
     * @param oval Oval to be drawn.
     */
    public void borders(Oval oval) {
        Oval o = new Oval(oval);
        serialize(o);
        canvas.drawOval(o.getX() - o.getRadiusX(), o.getY() - o.getRadiusY(), o.getRadiusX() * 2, o.getRadiusY() * 2);
    }

    /**
     * Fills an oval.
     * If getCamera is setValues, position will be changed accordingly.
     *
     * @param oval Oval to be drawn.
     */
    public void fill(Oval oval) {
        Oval o = new Oval(oval);
        serialize(o);
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


    /**
     * Draws an image on the canvas.
     *
     * @param name     Name of the image that will be drawn.
     * @param position Position of an image on the screen.
     */
    public void image(String name, Point position) {
        drawSimpleImage(Resources.getImageByName(name), position.copy(), DrawFrom.Center, Flip.None);
    }

    /**
     * Draws an image on the canvas and rotates it.
     *
     * @param name     Name of the image that will be drawn.
     * @param position Position of an image on the screen.
     * @param angle    Angle of rotation.
     */
    public void image(String name, Point position, Angle angle) {
        drawRotatedImage(Resources.getImageByName(name), position.copy(), DrawFrom.Center, RotatePoint.Center, angle);
    }

    /**
     * Draws an image on the canvas and rotates it.
     *
     * @param name     Name of the image that will be drawn.
     * @param position Position of an image on the screen.
     * @param angle    Angle of rotation.
     * @param rotate   Point around which rotation will occur.
     */
    public void image(String name, Point position, Angle angle, RotatePoint rotate) {
        drawRotatedImage(Resources.getImageByName(name), position.copy(), DrawFrom.Center, rotate, angle);
    }

    /**
     * Draws an image on the canvas.
     *
     * @param name     Name of the image that will be drawn.
     * @param position Position of an image on the screen.
     * @param drawFrom Point in the image that will be placed exactly where @{code position}
     *                 points to.
     */
    public void image(String name, Point position, DrawFrom drawFrom) {
        drawSimpleImage(Resources.getImageByName(name), position.copy(), drawFrom, Flip.None);
    }

    /**
     * Draws an image on the canvas and rotates it.
     *
     * @param name     Name of the image that will be drawn.
     * @param position Position of an image on the screen.
     * @param drawFrom Point in the image that will be placed exactly where @{code position}
     *                 points to.
     * @param angle    Angle of rotation.
     */
    public void image(String name, Point position, DrawFrom drawFrom, Angle angle) {
        drawRotatedImage(Resources.getImageByName(name), position.copy(), drawFrom, RotatePoint.Center, angle);
    }

    /**
     * Draws an image on the canvas and rotates it.
     *
     * @param name     Name of the image that will be drawn.
     * @param position Position of an image on the screen.
     * @param drawFrom Point in the image that will be placed exactly where @{code position}
     *                 points to.
     * @param angle    Angle of rotation.
     * @param rotate   Point around which rotation will occur.
     */
    public void image(String name, Point position, DrawFrom drawFrom, Angle angle, RotatePoint rotate) {
        drawRotatedImage(Resources.getImageByName(name), position.copy(), drawFrom, rotate, angle);
    }

    /**
     * Draws an image from a spritesheet onto the canvas, by selecting proper part
     * of the spritesheet and drawing it onto the canvas. <br><br>
     * Spritesheet image name is retrieved from {@link ui.gfx.Frame} object.
     *
     * @param frame    Object informing about a frame to displaySize
     * @param position Position of an image on the screen.
     * @param drawFrom Specifies the vertical anchor of drawing an image.
     * @param flip     Specifies the flip of an image (Horizontally, Vertically, Both, None)
     * @see ui.gfx.Frame
     * @see ui.gfx.DrawFrom
     * @see ui.gfx.Flip
     */
    public void frame(Frame frame, Point position, DrawFrom drawFrom, Flip flip) {
        drawImageFrame(frame, position.copy(), drawFrom, flip);
    }


    // Private functions

    private void drawRotatedImage(BufferedImage img, Point position, DrawFrom drawFrom, RotatePoint rotate, Angle angle) {
        if (canvas == null) {
            throw new UnsetCanvasException("No canvas has been specified.");
        }
        serialize(position);
        canvas.drawImage(img, affine(position, angle, drawFrom, rotate, img.getWidth(), img.getHeight()), null);
    }

    private void drawImageFrame(Frame frame, Point position, DrawFrom drawFrom, Flip flip) {
        if (canvas == null) {
            throw new UnsetCanvasException("No canvas has been specified.");
        }
        serialize(position);
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
        // canvas.drawRect(x, y, frame.width, frame.height);
    }

    private AffineTransform affine(Point position, Angle angle, DrawFrom drawFrom, RotatePoint rotate, int width, int height) {
        AffineTransform at = new AffineTransform();
        at.translate(
                position.getX() + (int) (width * drawFrom.x),
                position.getY() + (int) (height * drawFrom.y)
        );
        at.rotate(angle.getValue());
        at.translate(
                -width * (rotate.x + 0.5),
                -height * (rotate.y + 0.5)
        );
        return at;
    }


    private void drawSimpleImage(BufferedImage img, Point position, DrawFrom drawFrom, Flip flip) {
        if (canvas == null) {
            throw new UnsetCanvasException("No canvas has been specified.");
        }
        serialize(position);
        int width = img.getWidth();
        int height = img.getHeight();
        int x = position.getX();
        int y = position.getY();

        y -= (drawFrom.y + 0.5) * height;
        x -= (drawFrom.x + 0.5) * width;

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
