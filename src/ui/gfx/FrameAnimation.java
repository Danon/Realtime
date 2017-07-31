package ui.gfx;

public class FrameAnimation {
    private final String name;
    private final int animationSpeed;
    private final Frame[] frames;

    FrameAnimation(String name, int animationSpeed, Frame... frames) {
        this.name = name;
        this.animationSpeed = animationSpeed;
        this.frames = frames;
    }

    void setSpritesheet(Spritesheet spritesheet) {
        for (Frame frame : this.frames) {
            frame.setSpritesheet(spritesheet);
        }
    }

    public String getName() {
        return this.name;
    }

    Frame getFrameIterate(int frameId) {
        assert frameId >= 0;
        return frames[frameId / animationSpeed % frames.length];
    }

    public static class Speed {
        final static int Default = 10;
        final static int Materialization = Default;
        final static int Idle = Default;
        public final static int MidAir = 10;
        final static int MidAirGun = 10;
        final static int RunStart = Default;
        public final static int Run = 8;
        final static int RunGun = 8;
        final static int Pushed = Default;
        public final static int Shooting = 11;
        public final static int Basic = 6;
        final static int BasicStart = 6;
        final static int BasicAir = 8;
        final static int Goal = Default;
        final static int ClimbingStart = Default;
        final static int ClimbingEnd = Default;
        final static int Climbing = 13;
    }
}
