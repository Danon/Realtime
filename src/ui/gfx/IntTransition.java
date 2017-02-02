package ui.gfx;

/**
 * Allows for interpolation between two values.
 * 
 * @author Danio
 * @version 1.0 19/04/2015
 */
public class IntTransition
{
    /**
     * Linear interpolation.
     * @param a Begining value.
     * @param b Ending value.
     * @param transition Position of new value (in range @{code 0.0 - 0.1}
     * @return Interpolated value between begining and ending values.
     */
    public static double linear(double a, double b, double transition) 
    {
        return a*(1.0-transition) + b * transition;
    }
    /**
     * Cosine interpolation.
     * @param a Begining value.
     * @param b Ending value.
     * @param transition Position of new value (in range @{code 0.0 - 0.1}
     * @return Interpolated value between begining and ending values.
     */
    public static double cosine(double a, double b, double transition) 
    {
        double cosineTransition = (1 - Math.cos(transition * Math.PI)) / 2;
        return a*(1-cosineTransition) + b * cosineTransition;
    }
    /**
     * Cubic interpolation.
     * @param a Begining value.
     * @param b Ending value.
     * @param c First ontrol point.
     * @param d Second control point.
     * @param transition Position of new value (in range @{code 0.0 - 0.1}
     * @return Interpolated value between begining and ending values.
     */
    public static double cubic(double a,double b,double c,double d,double transition) 
    {
        double a0, a1, a2, a3, transition2;

        transition2 = transition*transition;
        a0 = d - c - a + b;
        a1 = a - b - a0;
        a2 = c - a;
        a3 = b;

        return(a0*transition*transition2 + a1*transition2 + a2*transition + a3);
    }
}
