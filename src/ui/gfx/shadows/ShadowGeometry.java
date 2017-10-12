package ui.gfx.shadows;

import java.awt.*;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ShadowGeometry {
    static class Points {
        static Comparator<Point2D> byAngleComparator(final Point2D center) {
            return (p0, p1) -> {
                double dx0 = p0.getX() - center.getX(),
                        dy0 = p0.getY() - center.getY(),
                        dx1 = p1.getX() - center.getX(),
                        dy1 = p1.getY() - center.getY();
                double
                        angle0 = Math.atan2(dy0, dx0),
                        angle1 = Math.atan2(dy1, dx1);
                return Double.compare(angle0, angle1);
            };
        }
    }


    static class Lines {
        static Line2D rotate(Line2D line, double angleRad) {
            double x0 = line.getX1();
            double y0 = line.getY1();
            double x1 = line.getX2();
            double y1 = line.getY2();
            double dx = x1 - x0;

            double dy = y1 - y0;
            double sa = Math.sin(angleRad);
            double ca = Math.cos(angleRad);
            double nx = ca * dx - sa * dy;
            double ny = sa * dx + ca * dy;

            return new Line2D.Double(x0, y0, x0 + nx, y0 + ny);
        }
    }

    static class Intersection {
        private static final double EPSILON = 1e-6;


        /**
         * Computes the intersection of the given lines.
         *
         * @param line0            The first line
         * @param line1            The second line
         * @param relativeLocation Optional location that stores the
         *                         relative location of the intersection point on
         *                         the given line segments
         * @param absoluteLocation Optional location that stores the
         *                         absolute location of the intersection point
         * @return Whether the lines intersect
         */
        static boolean intersectLineLine(
                Line2D line0, Line2D line1,
                Point2D relativeLocation,
                Point2D absoluteLocation) {
            return intersectLineLine(
                    line0.getX1(), line0.getY1(),
                    line0.getX2(), line0.getY2(),
                    line1.getX1(), line1.getY1(),
                    line1.getX2(), line1.getY2(),
                    relativeLocation, absoluteLocation);
        }

        /**
         * Computes the intersection of the specified lines.
         * <p>
         * Ported from
         * http://www.geometrictools.com/LibMathematics/Intersection/
         * Wm5IntrSegment2Segment2.cpp
         *
         * @param s0x0             x-coordinate of point 0 of line segment 0
         * @param s0y0             y-coordinate of point 0 of line segment 0
         * @param s0x1             x-coordinate of point 1 of line segment 0
         * @param s0y1             y-coordinate of point 1 of line segment 0
         * @param s1x0             x-coordinate of point 0 of line segment 1
         * @param s1y0             y-coordinate of point 0 of line segment 1
         * @param s1x1             x-coordinate of point 1 of line segment 1
         * @param s1y1             y-coordinate of point 1 of line segment 1
         * @param relativeLocation Optional location that stores the
         *                         relative location of the intersection point on
         *                         the given line segments
         * @param absoluteLocation Optional location that stores the
         *                         absolute location of the intersection point
         * @return Whether the lines intersect
         */
        static boolean intersectLineLine(
                double s0x0, double s0y0,
                double s0x1, double s0y1,
                double s1x0, double s1y0,
                double s1x1, double s1y1,
                Point2D relativeLocation,
                Point2D absoluteLocation) {
            double dx0 = s0x1 - s0x0;
            double dy0 = s0y1 - s0y0;
            double dx1 = s1x1 - s1x0;
            double dy1 = s1y1 - s1y0;

            double invLen0 = 1.0 / Math.sqrt(dx0 * dx0 + dy0 * dy0);
            double invLen1 = 1.0 / Math.sqrt(dx1 * dx1 + dy1 * dy1);

            double dir0x = dx0 * invLen0;
            double dir0y = dy0 * invLen0;
            double dir1x = dx1 * invLen1;
            double dir1y = dy1 * invLen1;

            double c0x = s0x0 + dx0 * 0.5;
            double c0y = s0y0 + dy0 * 0.5;
            double c1x = s1x0 + dx1 * 0.5;
            double c1y = s1y0 + dy1 * 0.5;

            double cdx = c1x - c0x;
            double cdy = c1y - c0y;

            double dot = perpendicularDotProduct(dir0x, dir0y, dir1x, dir1y);
            if (Math.abs(dot) > EPSILON) {
                if (relativeLocation != null || absoluteLocation != null) {
                    double dot0 = perpendicularDotProduct(cdx, cdy, dir0x, dir0y);
                    double dot1 = perpendicularDotProduct(cdx, cdy, dir1x, dir1y);
                    double invDot = 1.0 / dot;
                    double s0 = dot1 * invDot;
                    double s1 = dot0 * invDot;
                    if (relativeLocation != null) {
                        double n0 = (s0 * invLen0) + 0.5;
                        double n1 = (s1 * invLen1) + 0.5;
                        relativeLocation.setLocation(n0, n1);
                    }
                    if (absoluteLocation != null) {
                        double x = c0x + s0 * dir0x;
                        double y = c0y + s0 * dir0y;
                        absoluteLocation.setLocation(x, y);
                    }
                }
                return true;
            }
            return false;
        }

        private static double perpendicularDotProduct(double x0, double y0, double x1, double y1) {
            return x0 * y1 - y0 * x1;
        }
    }

    static class Shapes {
        static List<Line2D> computeLineSegments(Shape shape, double flatness) {
            List<Line2D> result = new ArrayList<>();
            PathIterator pi = new FlatteningPathIterator(shape.getPathIterator(null), flatness);
            double[] coordinates = new double[6];
            double previous[] = new double[2];
            double first[] = new double[2];
            while (!pi.isDone()) {
                int segment = pi.currentSegment(coordinates);
                switch (segment) {
                    case PathIterator.SEG_MOVETO:
                        previous[0] = coordinates[0];
                        previous[1] = coordinates[1];
                        first[0] = coordinates[0];
                        first[1] = coordinates[1];
                        break;

                    case PathIterator.SEG_CLOSE:
                        result.add(new Line2D.Double(
                                previous[0], previous[1],
                                first[0], first[1]));
                        previous[0] = first[0];
                        previous[1] = first[1];
                        break;

                    case PathIterator.SEG_LINETO:
                        result.add(new Line2D.Double(previous[0], previous[1], coordinates[0], coordinates[1]));
                        previous[0] = coordinates[0];
                        previous[1] = coordinates[1];
                        break;

                    case PathIterator.SEG_QUADTO:  // Should never occur
                        throw new AssertionError("SEG_QUADTO in flattened path!");

                    case PathIterator.SEG_CUBICTO: // Should never occur
                        throw new AssertionError("SEG_CUBICTO in flattened path!");
                }
                pi.next();
            }
            return result;
        }
    }
}
