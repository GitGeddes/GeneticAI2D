import greenfoot.*;

/**
 * The object the Shooters are trying to aim at and hit.
 * 
 * @author John Geddes
 * @version Nov 5, 2013 - Mar 5, 2014
 */
public class Target extends Actor {
    private int wantRangeLeft;
    private int wantRangeRight;
    
    public Target() {
        this.wantRangeLeft = (int) (Math.random() * 20 + 20);
        this.wantRangeRight = (int) (Math.random() * 20 + 560);
    }
    
    public void act() {
        if (this.getX() <= this.wantRangeLeft || this.getX() >= this.wantRangeRight) {
            this.turn(180);
        }
        this.move(2);
        if (((Background) this.getWorld()).areAllShotsFired()) {
            ((Background) this.getWorld()).makeNewGeneration();
            // this.remakeWantRanges();
        }
    }
    
    /*public void remakeWantRanges() {
        boolean randBool = (int) (Math.random()) == 1 ? true : false;
        int randIntLeft = (int) (Math.random());
    }*/
}