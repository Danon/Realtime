package ui.gfx;

/**
 * @author Danio
 * @version 1.0 20/04/2015
 */
public class RotatePoint extends RatioPoint
{
    public RotatePoint (double x, double y)
    {
        super(x,y);
    }
    
    public static RotatePoint Center = new RotatePoint(0.0, 0.0);
}
