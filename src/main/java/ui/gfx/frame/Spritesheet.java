package ui.gfx.frame;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class Spritesheet {
    private final Map<Animation, FrameAnimation> animations;

    public FrameAnimation animation(Animation animation) {
        return animations.get(animation);
    }
}
