package tools.mapDesigner;


import gameplay.Point;

class Geometry {
    public static int relativeCCW(Point one, Point two, Point p) {
        return relativeCCW(one.x, one.y, two.x, two.y, p.x, p.y);
    }

    private static int relativeCCW(double x1, double y1, double x2, double y2, double xp, double yp) {
        x2 -= x1;
        y2 -= y1;
        xp -= x1;
        yp -= y1;

        double ccw = xp * y2 - yp * x2;

        if (ccw == 0.0) {
            ccw = xp * x2 + yp * y2;
            if (ccw > 0.0) {
                xp -= x2;
                yp -= y2;
                ccw = xp * x2 + yp * y2;
                if (ccw < 0.0) {
                    ccw = 0.0;
                }
            }
        }
        return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
    }

    static boolean linesCross(Point start1, Point end1, Point start2, Point end2) {
        int relative1 = relativeCCW(start1.x, start1.y, end1.x, end1.y, start2.x, start2.y);
        int relative2 = relativeCCW(start1.x, start1.y, end1.x, end1.y, start2.x, end2.y);
        int relative3 = relativeCCW(start2.x, start2.y, end2.x, end2.y, start1.x, start1.y);
        int relative4 = relativeCCW(start2.x, start2.y, end2.x, end2.y, end1.x, end1.y);
        return (
                (relative1 * relative2 <= 0) && (relative3 * relative4 <= 0)
        );
    }

    static Point lineIntersectionPoint(PointLine line1, PointLine line2) {
        double
                s1_x = line1.end.x - line1.start.x,
                s1_y = line1.end.y - line1.start.y,

                s2_x = line2.end.x - line2.start.x,
                s2_y = line2.end.y - line2.start.y,

                s = (-s1_y * (line1.start.x - line2.start.x) + s1_x * (line1.start.y - line2.start.y)) / (-s2_x * s1_y + s1_x * s2_y),
                t = (s2_x * (line1.start.y - line2.start.y) - s2_y * (line1.start.x - line2.start.x)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            return new Point(
                    line1.start.x + (t * s1_x),
                    line1.start.y + (t * s1_y));
        }

        return null;
    }
}
