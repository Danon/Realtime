package ui.gfx;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static ui.gfx.ShadowGeometry.Intersection.intersectLineLine;
import static ui.gfx.ShadowGeometry.Points.byAngleComparator;
import static ui.gfx.ShadowGeometry.Shapes.computeLineSegments;

public class MasterOfShadows {
    private final List<List<Line2D>> lineSegments = new ArrayList<>();
    private final List<Line2D> borders = new ArrayList<>();

    MasterOfShadows() {
        lineSegments.add(borders);
    }

    void addObstacle(Shape shape) {
        this.addLineSegment(computeLineSegments(shape, 1.0));
    }

    private void addLineSegment(List<Line2D> lineSegments) {
        this.lineSegments.add(lineSegments);
    }

    void setBounds(int x, int y, int width, int height) {
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

    void getFieldOfView(gameplay.Point lightPosition, IntersectionIterable iterable) {
        java.awt.Point pos = new java.awt.Point(lightPosition.getX(), lightPosition.getY());
        List<Line2D> rays = this.createRays(pos);
        List<Point2D> closestIntersections = this.computeClosestIntersections(rays);
        closestIntersections.sort(byAngleComparator(pos));
        MasterOfShadows.createLightShape(closestIntersections, iterable);
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

                rays.add(ShadowGeometry.Lines.rotate(ray0, +deltaRad));
                rays.add(ShadowGeometry.Lines.rotate(ray0, -deltaRad));
                rays.add(ShadowGeometry.Lines.rotate(ray1, +deltaRad));
                rays.add(ShadowGeometry.Lines.rotate(ray1, -deltaRad));
            }
        }
        return rays;
    }

    private List<Point2D> computeClosestIntersections(List<Line2D> rays) {
        List<Point2D> intersections = new ArrayList<>();
        for (Line2D ray : rays) {
            Point2D closestIntersection = computeClosestIntersection(ray);
            if (closestIntersection != null) {
                intersections.add(closestIntersection);
            }
        }
        return intersections;
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

    private static void createLightShape(List<Point2D> closestIntersections, IntersectionIterable iterable) {
        for (int i = 0; i < closestIntersections.size(); i++) {
            Point2D point = closestIntersections.get(i);
            if (i == 0) {
                iterable.iterateFirst(point.getX(), point.getY());
            } else {
                iterable.iterateNext(point.getX(), point.getY());
            }
        }
    }
}
