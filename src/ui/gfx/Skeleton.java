package ui.gfx;

import gameplay.Angle;
import gameplay.Point;

/**
 * @author Danio
 */
public class Skeleton
{
    public Point position;
    public final static int BonesCount = 10;
    final public DirectionalVector
        body, head,  leftArm, rightArm, leftHand, rightHand, leftTigh, rightTigh, leftLeg, rightLeg
    ;
    
    final public DirectionalVector[] parts;
    
    public Skeleton (Point p)
    {
        position  = p;
        body      = new DirectionalVector(92, new Angle());      // 114
        head      = new DirectionalVector(58, new Angle());      // 63
        leftArm   = new DirectionalVector(55, new Angle(-135));  // 70
        rightArm  = new DirectionalVector(55, new Angle(135));   // 70
        leftHand  = new DirectionalVector(56, new Angle(-155));  // 80
        rightHand = new DirectionalVector(56, new Angle(155));   // 80
        leftTigh  = new DirectionalVector(48, new Angle(-145));  // 92
        rightTigh = new DirectionalVector(48, new Angle(145));   // 92
        leftLeg   = new DirectionalVector(66, new Angle(-165));  // 86
        rightLeg  = new DirectionalVector(66, new Angle(165));   // 86
        
        parts =  new DirectionalVector[] {
            body, head,  leftArm, rightArm, leftHand, rightHand, leftTigh, rightTigh, leftLeg, rightLeg 
        };
    }
    
    public Skeleton (Skeleton skeleton)
    {
        position  = skeleton.position.copy();
        body      = skeleton.body.copy();
        head      = skeleton.head.copy();
        leftArm   = skeleton.leftArm .copy();
        rightArm  = skeleton.rightArm.copy();
        leftHand  = skeleton.leftHand.copy();
        rightHand = skeleton.rightHand.copy();
        leftTigh  = skeleton.leftTigh.copy();
        rightTigh = skeleton.rightTigh.copy();
        leftLeg   = skeleton.leftLeg.copy();
        rightLeg  = skeleton.rightLeg.copy();
        
        parts =  new DirectionalVector[] {
            body, head,  leftArm, rightArm, leftHand, rightHand, leftTigh, rightTigh, leftLeg, rightLeg 
        };
    }
    
    public DirectionalVector getBoneById(int id) {
        switch (id) {
            case 0: return body; 
            case 1: return head;  
            case 2: return leftArm; 
            case 3: return rightArm; 
            case 4: return leftHand; 
            case 5: return rightHand; 
            case 6: return leftTigh; 
            case 7: return rightTigh; 
            case 8: return leftLeg; 
            case 9: return rightLeg;
            default:
                return null;
        }
    }
    public Vector getBoneJointPoint(int id) {
        switch (id) {
            case 0: return position.asVector(); 
            case 1: return body.asVector().add(position);
            case 2: return body.asVector().add(position);
            case 3: return body.asVector().add(position);
            case 4: return body.asVector().add(position).add(leftArm.asVector()).asVector();
            case 5: return body.asVector().add(position).add(rightArm.asVector()).asVector();
            case 6: return position.asVector(); 
            case 7: return position.asVector();
            case 8: return position.add(leftTigh.asVector()).asVector();
            case 9: return position.add(rightTigh.asVector()).asVector();
            default:
                return null;
        }
    }

    public VectorSkeleton toVectorSkeleton() {
        return new VectorSkeleton(this);
    }
    
    public Skeleton copy() {
        return new Skeleton(this);
    }
    
 
    public static class Transition 
    {
        /**
         * Creates a transition between two Animation Skeletons.
         * @param a Begining point of a transition.
         * @param b Ending point of a transition.
         * @param transitionValue Position of a transition return point (values 0.0 - 1.0).
         * @return Transition between Animation Skeletons.
         */           
        public static Skeleton linear(Skeleton a, Skeleton b, double transitionValue) 
        {
            Skeleton res = new Skeleton(Point.Transition.linear(a.position, b.position, transitionValue));

            for (int i = 0; i < Skeleton.BonesCount; i++) {
                res.parts[i].copy(DirectionalVector.Transition.linear(a.parts[i], b.parts[i], transitionValue));
            }

            return res;
        }

        /**
         * Creates a cosine transition between two Animation Skeletons.
         * @param a Begining point of a transition.
         * @param b Ending point of a transition.
         * @param transitionValue Position of a transition return point (values 0.0 - 1.0).
         * @return Transition between Animation Skeletons.
         */           
        public static Skeleton cosine(Skeleton a, Skeleton b, double transitionValue) 
        {
            Skeleton res = new Skeleton(Point.Transition.cosine(a.position, b.position, transitionValue));

            for (int i = 0; i < Skeleton.BonesCount; i++) {
                res.parts[i].copy(DirectionalVector.Transition.cosine(a.parts[i], b.parts[i], transitionValue));
            }

            return res;
        }
        
        /**
         * Creates a cubic transition between two Animation Skeletons.
         * @param begining Begining point of a transition.
         * @param end Ending point of a transition.
         * @param c First control point.
         * @param d First control point.
         * @param transitionValue Position of a transition return point (values 0.0 - 1.0).
         * @return Transition between Animation Skeletons.
         */           
        public static Skeleton cubic(Skeleton begining, Skeleton end, Skeleton c, Skeleton d, double transitionValue) 
        {
            Skeleton res = new Skeleton(Point.Transition.cubic(
                    begining.position, end.position, c.position, d.position, transitionValue));

            for (int i = 0; i < Skeleton.BonesCount; i++) {
                res.parts[i].copy(DirectionalVector.Transition.cubic(
                        begining.parts[i], end.parts[i], c.parts[i], d.parts[i], transitionValue));
            }

            return res;
        }
    } 
}
