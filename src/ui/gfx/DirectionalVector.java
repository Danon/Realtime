package ui.gfx;

import gameplay.Angle;
import gameplay.Point;

public class DirectionalVector {
    public double length;
    public Angle direction;

    private DirectionalVector() {
        this.direction = new Angle();
        this.length = 0;
    }

    DirectionalVector(double length, Angle angle) {
        this.direction = new Angle(angle);
        this.length = length;
    }

    public DirectionalVector(DirectionalVector v) {
        this.length = v.length;
        this.direction = new Angle(v.direction);
    }

    public DirectionalVector(Point p) {
        this.length = p.distanceTo(new Point());
        this.direction = new Point(0, 0).angleOf(p);
    }

    public int getLength() {
        return (int) length;
    }

    public int getDirection() {
        return (int) direction.getValue();
    }

    public DirectionalVector add(DirectionalVector a) {
        return new DirectionalVector(
                this.length + a.length,
                this.direction.add(a.direction)
        );
    }

    public Vector asVector() {
        return new Vector(this);
    }

    static class Transition {
        /**
         * Creates a linear transition between two Directional Vectors.
         *
         * @param a               Begining point of a transition.
         * @param b               Ending point of a transition.
         * @param transitionValue Position of a transition return vector (values 0.0 - 1.0).
         * @return Transition between Directional Vectors.
         */
        static DirectionalVector linear(DirectionalVector a, DirectionalVector b, double transitionValue) {
            DirectionalVector res = new DirectionalVector();
            res.length = IntTransition.linear(a.length, b.length, transitionValue);
            res.direction = Angle.Transition.linear(a.direction, b.direction, transitionValue);
            return res;
        }

        /**
         * Creates a cosine transition between two Directional Vectors.
         *
         * @param a               Begining point of a transition.
         * @param b               Ending point of a transition.
         * @param transitionValue Position of a transition return vector (values 0.0 - 1.0).
         * @return Transition between Directional Vectors.
         */
        static DirectionalVector cosine(DirectionalVector a, DirectionalVector b, double transitionValue) {
            DirectionalVector res = new DirectionalVector();
            res.length = IntTransition.cosine(a.length, b.length, transitionValue);
            res.direction = Angle.Transition.cosine(a.direction, b.direction, transitionValue);
            return res;
        }

        /**
         * Creates a cubic transition between two Directional Vectors.
         *
         * @param a               Begining point of a transition.
         * @param b               Ending point of a transition.
         * @param c               First control point.
         * @param d               First control point.
         * @param transitionValue Position of a transition return vector (values 0.0 - 1.0).
         * @return Transition between Directional Vectors.
         */
        static DirectionalVector cubic(DirectionalVector a, DirectionalVector b,
                                       DirectionalVector c, DirectionalVector d, double transitionValue) {
            DirectionalVector res = new DirectionalVector();
            res.length = IntTransition.cubic(a.length, b.length, c.length, d.length, transitionValue);
            res.direction = Angle.Transition.cubic(
                    a.direction, b.direction, c.direction, d.direction, transitionValue);
            return res;
        }
    }
}
