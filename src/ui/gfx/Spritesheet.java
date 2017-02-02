package ui.gfx;

/**
 * @author Danio
 * @version 1.0 06/04/2015
 */
public class Spritesheet
{
    private final String name;
    final FrameAnimation[] animations;        
    
    Spritesheet (String name, FrameAnimation... animations)
    {  
        this.name = name;
        this.animations = animations;
        for (FrameAnimation animation : this.animations) {
            animation.parentSpritesheet = this;
        }
    }
    
    public String getName() {
        return name;
    }
    
    public FrameAnimation animation(String animationName) {
        for (FrameAnimation animation : animations) {
            if ( animation.getName().equals(animationName) ) {
                return animation;
            }
        }
        return null;
    }
    
    public FrameAnimation animation(int animationId) {
        assert (0 <= animationId && animationId < animations.length);
        return animations[animationId];
    }
}

















