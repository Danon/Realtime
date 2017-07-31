package ui.gfx;

public class UnsetCanvasException extends RuntimeException {
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
