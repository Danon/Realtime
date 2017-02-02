package ui.gfx;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ui.gfx.ShadowGeometry.Intersection.intersectLineLine;


//  zed.setBounds(0, 0, getWidth(), getHeight());

// zed.getObstacles().forEach(g::draw);
// Shape lightShape = zed.getFieldOfView(lightPosition);

public class MasterOfShadows {
    private final List<Shape> obstacles = new ArrayList<>();
    private List<List<Line2D>> lineSegments = new ArrayList<>();
    private final List<Line2D> borders = new ArrayList<>();
    //keeping references to borders so we can easily change the bounds size

    MasterOfShadows() {
        lineSegments.add(borders);
    }

    public void addLineSegment(List<Line2D> lineSegments) {
        this.lineSegments.add(lineSegments);
    }

    public void addObstacle(Shape shape) {
        obstacles.add(shape);
        this.addLineSegment(ShadowGeometry.Shapes.computeLineSegments(shape, 1.0));
    }

    public List<Shape> getObstacles() {
        return this.obstacles;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.borders.clear();
        this.borders.add(new Line2D.Double(x, y, width, y));
        this.borders.add(new Line2D.Double(width, y, width, height));
        this.borders.add(new Line2D.Double(width, height, x, height));
        this.borders.add(new Line2D.Double(x, height, x, y));
    }

    public void setCustomBounds(List<Line2D> borders) {
        this.borders.clear();
        this.borders.addAll(borders);
    }

    private static void createLightShape(List<Point2D> closestIntersections, IntersectionIterable iterable) {
        for (int i = 0; i < closestIntersections.size(); i++) {
            Point2D p = closestIntersections.get(i);
            if (i == 0) {
                iterable.iterateFirst(p.getX(), p.getY());
            } else {
                iterable.iterateNext(p.getX(), p.getY());
            }
        }
    }

    public void getFieldOfView(gameplay.Point lightPosition, IntersectionIterable iterable) {
        java.awt.Point pos = new java.awt.Point(lightPosition.getX(), lightPosition.getY());
        List<Line2D> rays = this.createRays(pos);
        List<Point2D> closestIntersections = this.computeClosestIntersections(rays);
        Collections.sort(closestIntersections, ShadowGeometry.Points.byAngleComparator(pos));
        MasterOfShadows.createLightShape(closestIntersections, iterable);
    }

    private List<Point2D> computeClosestIntersections(List<Line2D> rays) {
        List<Point2D> closestIntersections = new ArrayList<>();
        for (Line2D ray : rays) {
            Point2D closestIntersection = computeClosestIntersection(ray);
            if (closestIntersection != null) {
                closestIntersections.add(closestIntersection);
            }
        }
        return closestIntersections;
    }


    private List<Line2D> createRays(Point2D lightPosition) {
        final double deltaRad = 0.0001;
        List<Line2D> rays = new ArrayList<>();
        for (List<Line2D> shapeLineSegments : lineSegments) {
            for (Line2D line : shapeLineSegments) {
                Line2D ray0 = new Line2D.Double(lightPosition, line.getP1());
                Line2D ray1 = new Line2D.Double(lightPosition, line.getP2());
                rays.add(ray0);
                rays.add(ray1);

                rays.add(ShadowGeometry.Lines.rotate(ray0, +deltaRad, null));
                rays.add(ShadowGeometry.Lines.rotate(ray0, -deltaRad, null));
                rays.add(ShadowGeometry.Lines.rotate(ray1, +deltaRad, null));
                rays.add(ShadowGeometry.Lines.rotate(ray1, -deltaRad, null));
            }
        }
        return rays;
    }


    private Point2D computeClosestIntersection(Line2D ray) {
        final double EPSILON = 1e-6;
        Point2D relativeLocation = new Point2D.Double();
        Point2D absoluteLocation = new Point2D.Double();
        Point2D closestIntersection = null;
        double minRelativeDistance = Double.MAX_VALUE;
        for (List<Line2D> lineSegments : this.lineSegments) {
            for (Line2D lineSegment : lineSegments) {
                if (intersectLineLine(ray, lineSegment, relativeLocation, absoluteLocation)) {
                    if (relativeLocation.getY() >= -EPSILON && relativeLocation.getY() <= 1 + EPSILON) {
                        if (relativeLocation.getX() >= -EPSILON && relativeLocation.getX() < minRelativeDistance) {
                            minRelativeDistance = relativeLocation.getX();
                            closestIntersection = new Point2D.Double(
                                    absoluteLocation.getX(),
                                    absoluteLocation.getY());
                        }
                    }
                }
            }
        }
        return closestIntersection;
    }

}