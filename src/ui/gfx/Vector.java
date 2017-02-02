package ui.gfx;

import gameplay.Point;

/**
 * Vector.java
 * 
 * @author Danio
 * @version 1.0 11/04/2015
 */
public class Vector extends Point
{
    public Vector ()
    {
        super();
    }
    
    public Vector(double x, double y) {
        super(x, y);
    }
    
    public Vector(Point p) {
        super(p.x, p.y);
    }
    
    public Vector(DirectionalVector vector) {
        this.x = vector.length * Math.sin(vector.direction.getValue());
        this.y = vector.length * Math.cos(vector.direction.getValue());
    }

    public Vector sub(Point p) {
        return new Vector(this.x-p.x, this.y-p.y);
    }
    
    public Vector add(double x, double y) {
        return new Vector(this.x + x, this.y + y);
    }
    
    /**
     * Increases vector value.
     * @param a Value of which vector will be increased.
     * @return New vector with increased value.
     */
    @Override
    public Vector add(Point a) {
        return new Vector(this.x + a.x, this.y + a.y);
    }
    
    /**
     * Creates a transition between two Vectors.
     * @param a Begining point of a transition.
     * @param b Ending point of a transition.
     * @param transitionValue Position of a transition return point (values 0.0 - 1.0)
     * @return Transition between vectors.
     */
    public static Vector transition(Vector a, Vector b, double transitionValue) 
    {
        Vector diff = b.sub(a);
        return a.add(diff.x*transitionValue, diff.y*transitionValue);
    }
}
