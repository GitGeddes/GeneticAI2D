import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 *  This is the Actor that represents a moving projectile fired from one of
 * the Shooters.
 * 
 * @author John Geddes
 * @version Nov 5, 2013 - Mar 5, 2014
 */
public class Projectile extends Actor {
    private int name;
    private double distToTarget;
    
    public Projectile(int name) {
        this.name = name;
        this.setRotation(270);
    }
    
    public void act() {
        this.move(4);
        if (this.getY() == 50) {
            this.distToTarget = this.getCurrDistToTarget();
        } else if (this.isTouching(Target.class)) {
            this.distToTarget = 0.0;
        }
    }
    
    public boolean isIntersecting(java.lang.Class cls) {
        return this.isTouching(cls);
    }
    
    public double getDistToTarget() {
        return this.distToTarget;
    }
    
    public double getCurrDistToTarget() {
        return Math.sqrt((Math.abs((this.getX() -
            ((Background) this.getWorld()).getTarget().getX()) ^ 2 +
            (this.getY() - ((Background) this.getWorld()).getTarget().getX()) ^ 2)));
    }
}