package ui.gfx;

import gameplay.Point;
import java.io.IOException;
import util.save.Savable;
import util.save.SaveInput;
import util.save.SaveOutput;

public class SkeletonAnimation implements Savable
{
    String name;
    Skeleton[] frames;
    
    public SkeletonAnimation (String name, int framesCount, Point position)
    {
        this.name = name;
        this.frames = new Skeleton[framesCount];
        for (int i = 0; i < this.frames.length; i++) {
            frames[i] = new Skeleton( position );
        }
    }
    public SkeletonAnimation (String name, int framesCount) {
        this(name, framesCount, new Point(250, 250));
    }
    
    public SkeletonAnimation (String name) {
        this(name, 10);
    }

    public String getName() {
        return name;
    }
    
    public Skeleton[] getSkeletons() {
        return frames;
    }
    
    public int getSkeletonsCount() {
        return frames.length;
    }
    
    public Skeleton getSkeleton(int index) {
        return frames[index];
    }
    
    public Skeleton getLastSkeleton() {
        return frames[frames.length-1];
    }
    
    public void setSkeleton(int index, Skeleton newSkeleton) {
        frames[index] = newSkeleton;
    }
    
    @Override
    public void restoreState(SaveInput input) throws IOException 
    {
        // Animation Name
        this.name =  input.readString();
        
        // Number of animation frames (skeletons)
        int skeletonsCount = input.readInt();
        this.frames = new Skeleton[skeletonsCount];
                
        // Load skeletons
        for (int i = 0; i < skeletonsCount; i++) 
        {
            // Load skeleton position
            double x = input.readDouble();
            double y = input.readDouble();
            
            Skeleton skeleton = new Skeleton(new Point(x, y));

            
            // Load skeleton parts
            for (DirectionalVector part : skeleton.parts) 
            {    
                part.length = input.readDouble();
                part.direction.setValue( input.readDouble() );
            }
      
            // Put skeleton to the animation
            this.setSkeleton(i, skeleton);
        }    
    }

    @Override
    public void storeState(SaveOutput output) throws IOException 
    {
        // Animation Name
        output.writeString(this.getName());

        // Number of animation frames (skeletons)
        output.writeInt(this.getSkeletons().length);
        
        // Save skeletons
        for (Skeleton skeleton : this.getSkeletons()) 
        {
            // Save skeleton position
            output.writeDouble(skeleton.position.x);
            output.writeDouble(skeleton.position.y);

            // Save skeleton parts
            for (DirectionalVector part : skeleton.parts) {
                output.writeDouble(part.length);
                output.writeDouble(part.direction.getValue());
            }
        }        
    }
}
