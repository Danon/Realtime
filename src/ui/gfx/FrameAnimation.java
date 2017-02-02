package ui.gfx;

public class FrameAnimation {
    Spritesheet parentSpritesheet;
    private final String name;
    private final int animationSpeed;
    private final Frame[] frames;

    FrameAnimation(String name, int animationSpeed, Frame... frames) {
        this.name = name;
        this.animationSpeed = animationSpeed;
        this.frames = frames;
        for (Frame frame : this.frames) {
            frame.parentAnimation = this;
        }
    }

    public FrameAnimation(String name, Frame... frames) {
        this(name, FrameAnimationSpeed.Default, frames);
    }

    public int getFramesCount() {
        return frames.length;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    public String getName() {
        return this.name;
    }

    public Frame getFrame(int frameId) {
        assert (0 <= frameId && frameId < frames.length);
        return frames[frameId];
    }

    Frame getFrameIterate(int frameId) {
        assert frameId >= 0;
        return frames[frameId / animationSpeed % frames.length];
    }

    String getSpritesheetName() {
        return parentSpritesheet.getName();
    }

    public static class FrameAnimationSpeed {
        public final static int
                Default = 10,
                Materialization = Default,
                Idle = Default,
                MidAir = 10,
                MidAirGun = 10,
                RunStart = Default,
                Run = 8,
                RunGun = 8,
                Pushed = Default,
                Shooting = 11,
                Basic = 6,
                BasicStart = 6,
                BasicAir = 8,
                Goal = Default,
                ClimbingStart = Default,
                ClimbingEnd = Default,
                Climbig = 13;
    }
}
