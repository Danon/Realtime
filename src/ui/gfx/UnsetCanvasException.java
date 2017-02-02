package ui.gfx;

/**
 * Thrown when {@link ui.gfx.Drawer#setCanvas} hasn't been called before drawing.
 * 
 * @author Danio
 * @see ui.gfx.Drawer#setCanvas(java.awt.Graphics2D) 
 * @version 1.0 20/04/2015
 */
public class UnsetCanvasException extends RuntimeException
{
    public UnsetCanvasException(String message) {
        super(message);
    }
    
    public UnsetCanvasException(Throwable cause) {
        super(cause);
    }
    
    public UnsetCanvasException(String message, Throwable cause) {
        super(message, cause);
    }
}
