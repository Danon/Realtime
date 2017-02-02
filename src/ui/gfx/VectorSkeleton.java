package ui.gfx;

import gameplay.Oval;
import gameplay.Point;

/**
 * VectorSkeleton.java
 * 
 * @author Danio
 * @version 1.0 13/04/2015
 */
final public class VectorSkeleton
{
    final Vector[] vectors;
    
    public VectorSkeleton (Skeleton skeleton)
    {
        vectors = new Vector[Skeleton.BonesCount];
        for (int i = 0; i < Skeleton.BonesCount; i++) {
            vectors[i] = skeleton.getBoneJointPoint(i).add( skeleton.getBoneById(i).asVector() );
        }
    }
    
    public int getVectorIdUnder(Point point, int tolerance) {
        for (int i = 0; i < vectors.length; i++) {
            if (point.isInside(new Oval(vectors[i], tolerance))) {
                return i;
            }
        }
        return -1;
    }
    
    public int getVectorsCount() {
        return vectors.length;
    }
    
    public Vector[] getVectors() {
        return vectors;
    }
    
    public Vector vect(int index) {
        return vectors[index];
    }
}