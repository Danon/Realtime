package ui.gfx.frame;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class FrameAnimation {
    private final String name;
    private final int animationSpeed;
    private final Frame[] frames;

    public FrameAnimation(String name, int animationSpeed, Frame... frames) {
        this.name = name;
        this.animationSpeed = animationSpeed;
        this.frames = frames;
    }

    public String getName() {
        return this.name;
    }

    public Frame getFrameIterate(int frameId) {
        assert frameId >= 0;
        return frames[frameId / animationSpeed % frames.length];
    }

    public static class Speed {
        final static int Default = 10;
        public final static int Materialization = Default;
        public final static int Idle = Default;
        public final static int MidAir = 10;
        public final static int MidAirGun = 10;
        public final static int RunStart = Default;
        public final static int Run = 8;
        public final static int RunGun = 8;
        public final static int Pushed = Default;
        public final static int Shooting = 11;
        public final static int Basic = 6;
        public final static int BasicAir = 8;
        public final static int Goal = Default;
        public final static int ClimbingStart = Default;
        public final static int ClimbingEnd = Default;
        public final static int Climbing = 13;
    }
}
