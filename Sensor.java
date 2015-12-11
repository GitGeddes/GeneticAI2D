import greenfoot.*;

/**
 *  This is the Actor that WILL BE ABLE to detect its immediate surroundings,
 * including the LOCATION OF THE TARGET, and possibly CROSS-WINDS and/or
 * OBSTACLES.
 *  There WILL BE multiple Sensors for every Shooter, and they all WILL act
 * as a team, giving the Shooter the information the Sensors pick up.
 *  The Sensors also have a little bit of randomness built into their
 * detection range.
 * 
 *  The Spots are explained here in a crude ASCII graphic:
 * | 0 | 1 |
 * | 2 | 3 |
 * | 4 | 5 |
 *    /^\    <-- Shooter
 *     |
 * 
 * @author John Geddes
 * @version Nov 30, 2013 - Mar 5, 2014
 */
public class Sensor extends Actor {
    private int name;
    private int spot; //See crude ASCII diagram
    private int type; // 0 is Target detector, 1 is Wind detector, 3 is Obstacle detector
    private boolean shown = true;
    private int detectRange;
    private String seed;
    private double detectDist;
    private Target targ;
    private Sensor myPair;
    private boolean isNearTarget;
    private String dirToTarget;
    
    public String seedHelp(int num) {
        if (num < 10) {
            return "0" + num;
        } else {
            return "" + num;
        }
    }
    
    public Sensor(int name, int spot, int type) {
        this.name = name;
        this.spot = spot;
        this.type = type;
        // this.detectRange = (int) (Math.random() * 25 + 5);
        this.detectRange = 25;
        // this.shown = false;
        this.seed = this.seedHelp(name) + spot + type + this.seedHelp(this.detectRange);
    }
    
    public void act() {
        if (shown) {
            this.setImage("Sensor.png");
        } else {
            this.setImage("empty5x5.png");
        }
        this.detectDist = this.getDetectDistance();
        this.talkToOtherSensors();
    }
    
    public void addTarget() {
        if (this.type == 0) {
            this.targ = ((Background) this.getWorld()).getTarget();
        } else if (type != 0) {
            this.targ = null;
        }
    }
    
    public double getDetectDistance() {
        double dist = Math.sqrt(((this.targ.getX() - this.getX()) ^ 2) +
            ((this.targ.getY() - this.getY()) ^ 2));
        if (this.type == 0 && dist < this.detectRange) {
            return dist;
        } else {
            return this.detectRange;
        }
    }
    
    public void findMyPair() {
        if (this.spot % 2 == 0) {
            this.myPair = ((Background)
                this.getWorld()).getShooters().get(this.name).getSensors().get(this.spot + 1);
        } else {
            this.myPair = ((Background)
                this.getWorld()).getShooters().get(this.name).getSensors().get(this.spot - 1);
        }
    }
    
    public double[] currentDataFeed() {
        double[] feed = new double[3];
        feed[0] = this.detectDist;
        feed[1] = this.myPair.getDetectDistance();
        if (this.dirToTarget.equals("left")) {
            feed[2] = -1.0;
        } else {
            feed[2] = 1.0;
        }
        return feed;
    }
    
    public void talkToOtherSensors() {
        if (this.type == 0) {
            this.isNearTarget = this.getDetectDistance() <= this.myPair.getDetectDistance();
            if (this.isNearTarget) {
                if (this.spot % 2 == 0) {
                    this.dirToTarget = "left";
                } else {
                    this.dirToTarget = "right";
                }
            } else {
                if (this.spot % 2 == 0) {
                    this.dirToTarget = "right";
                } else {
                    this.dirToTarget = "left";
                }
            }
        }
    }
}