package ui.gfx.frame;

import lombok.Getter;

public class Spritesheet {
    @Getter
    private final String name;
    private final FrameAnimation[] animations;

    public Spritesheet(String name, FrameAnimation... animations) {
        this.name = name;
        this.animations = animations;
    }

    public FrameAnimation animation(String animationName) {
        for (FrameAnimation animation : animations) {
            if (animation.getName().equals(animationName)) {
                return animation;
            }
        }
        return null;
    }
}
