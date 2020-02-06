package ui.gfx.shadows;

import ui.gfx.IntersectionIterable;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ui.gfx.shadows.ShadowGeometry.Intersection.intersectLineLine;
import static ui.gfx.shadows.ShadowGeometry.Points.byAngleComparator;
import static ui.gfx.shadows.ShadowGeometry.Shapes.computeLineSegments;

public class MasterOfShadows {
    private final List<List<Line2D>> lineSegments = new ArrayList<>();
    private final Map<List<Line2D>, Shape> skip = new HashMap<>();

    public MasterOfShadows(int x, int y, int width, int height) {
        addLineSegment(makeBounds(x, y, width, height));
    }

    private static List<Line2D> makeBounds(int x, int y, int width, int height) {
        List<Line2D> borders = new ArrayList<>(4);

        borders.add(new Line2D.Double(x, y, width, y));
        borders.add(new Line2D.Double(width, y, width, height));
        borders.add(new Line2D.Double(width, height, x, height));
        borders.add(new Line2D.Double(x, height, x, y));

        return borders;
    }

    public void addObstacle(Shape shape) {
        List<Line2D> lineSegments = computeLineSegments(shape, 1.0);
        this.addLineSegment(lineSegments);
        skip.put(lineSegments, shape);
    }

    private void addLineSegment(List<Line2D> lineSegments) {
        this.lineSegments.add(lineSegments);
    }

    public void getFieldOfView(gameplay.Point light, IntersectionIterable iterable) {
        java.awt.Point pos = new java.awt.Point(light.getX(), light.getY());

        List<Point2D> closestIntersections = this.computeClosestIntersections(this.createRays(pos), pos);

        closestIntersections.sort(byAngleComparator(pos));
        iterateLightShape(closestIntersections, iterable);
    }

    private List<Line2D> createRays(Point2D lightPosition) {
        final double deltaRad = 0.0001;
        List<Line2D> rays = new ArrayList<>();
        for (List<Line2D> shapeLineSegments : lineSegments) {
            if (shouldSkip(lightPosition, shapeLineSegments)) continue;

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

    private boolean shouldSkip(Point2D lightPosition, List<Line2D> shapeLineSegments) {
        Shape s = skip.get(shapeLineSegments);
        return s != null && s.contains(lightPosition);
    }

    private List<Point2D> computeClosestIntersections(List<Line2D> rays, Point2D lightPosition) {
        List<Point2D> intersections = new ArrayList<>();
        for (Line2D ray : rays) {
            Point2D closestIntersection = computeClosestIntersection(ray, lightPosition);
            if (closestIntersection != null) {
                intersections.add(closestIntersection);
            }
        }
        return intersections;
    }

    private Point2D computeClosestIntersection(Line2D ray, Point2D lightPosition) {
        final double EPSILON = 1e-6;
        Point2D relativeLocation = new Point2D.Double();
        Point2D absoluteLocation = new Point2D.Double();
        Point2D closestIntersection = null;
        double minRelativeDistance = Double.MAX_VALUE;

        for (List<Line2D> lineSegments : this.lineSegments) {
            if (shouldSkip(lightPosition, lineSegments)) continue;

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

    private static void iterateLightShape(List<Point2D> closestIntersections, IntersectionIterable iterable) {
        for (int i = 0; i < closestIntersections.size(); i++) {
            Point2D point = closestIntersections.get(i);
            if (i == 0) {
                iterable.iterateFirst(point.getX(), point.getY());
            } else {
                iterable.iterateNext(point.getX(), point.getY());
            }
        }
        iterable.onFinish();
    }
}
