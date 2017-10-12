package tools.mapDesigner;

import gameplay.Point;

class PointLine {
    final Point start, end;

    PointLine(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    double getLength() {
        return start.distanceTo(end);
    }

    Point crossPointWith(PointLine line) {
        return Geometry.lineIntersectionPoint(this, line);
    }

    boolean crosses(PointLine line) {
        return Geometry.linesCross(this.start, this.end, line.start, line.end);
    }
}
