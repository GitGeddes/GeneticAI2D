import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 *  This is the Actor class that represents a solution, in the form of a
 * Shooter at the bottom of the World. It has a few variables that change
 * the way the Shooter acts.
 * More will be added!
 * 
 * @author John Geddes
 * @version Nov 5, 2013 - Mar 4, 2014
 */
public class Shooter extends Actor {
    private int name;
    private int direction;
    private int moveSpeed;
    private int wantRangeLeft;
    private int wantRangeRight;
    private double[] scores;
    private double scoreMean;
    private double scoreTrue;
    private int shotsFired;
    private int shotsAllowed = 10;
    private String seed;
    private Projectile bullet;
    private java.util.ArrayList<Sensor> sensors = new java.util.ArrayList<Sensor>(6);
    
    private String seedHelp(int num, String type) {
        if (type.equals("name") || type.equals("move")) {
            if (num < 10) {
                return "0" + num;
            } else {
                return "" + num;
            }
        } else if (type.equals("dirc")) {
            if (num == -1) {
                return "" + num;
            } else {
                return "+" + num;
            }
        } else {
            if (num < 10) {
                return "00" + num;
            } else if (num < 100) {
                return "0" + num;
            } else {
                return "" + num;
            }
        }
    }
    
    public void addSensorsToList() {
        for (int i = 0; i <= 5; i++) {
            this.sensors.add(i, new Sensor(this.name, i, 0));
        }
    }
    
    public Shooter(int name, int direction, int moveSpeed, int wantRangeLeft, int wantRangeRight) {
        this.name = name;
        this.direction = direction;
 
        this.wantRangeLeft = wantRangeLeft;
        this.wantRangeRight = wantRangeRight;
        this.shotsFired = 1;
        this.seed = this.seedHelp(name, "name") + this.seedHelp(direction, "dirc") +
            this.seedHelp(moveSpeed, "move") + this.seedHelp(wantRangeLeft, "rnge") +
            this.seedHelp(wantRangeRight, "rnge");
        this.bullet = new Projectile(name);
        this.addSensorsToList();
        this.scores = new double[this.shotsAllowed];
    }
    
    public void act() {
        if (this.getX() <= this.wantRangeLeft) {
            this.direction = 1;
        } else if (this.getX() >= this.wantRangeRight) {
            this.direction = -1;
        }
        this.move(this.direction * this.moveSpeed);
        this.moveSensors();
        this.calcScores();
        this.interpretSensorData(this.sensors.get(0).currentDataFeed());
        if (this.bullet.isIntersecting(Target.class) || this.bullet.getY() <= 0) {
            this.shootNewProjectile();
        }
    }
    
    public void insertItems() {
        ((Background) this.getWorld()).addObject(this.bullet, this.getX(), 330);
        for (int i = 0; i <= 5; i++) {
            ((Background) this.getWorld()).addObject(this.sensors.get(i),
                this.getX() + (i % 2 == 0 ? -10 : 10),
                100 + 50 * (i / 2));
            this.sensors.get(i).addTarget();
            this.sensors.get(i).findMyPair();
        }
    }
    
    public void moveSensors() {
        for (int i = 0; i <= 5; i++) {
            this.sensors.get(i).setLocation(this.getX() + (i % 2 == 0 ? -10 : 10),
                100 + 50 * (i / 2));
        }
    }
    
    public void shootNewProjectile() {
        if (this.shotsFired < this.shotsAllowed) {
            this.bullet.setLocation(this.getX(), 330);
            this.shotsFired++;
        } else {
            this.scoreMean = this.calcScoreMean();
            this.scoreTrue = 25.0 - this.scoreMean;
        }
    }
    
    public void calcScores() {
        for (int i = 1; i <= this.shotsAllowed; i++) {
            if (this.shotsFired == i) {
                this.scores[i - 1] = this.bullet.getDistToTarget();
            }
        }
    }
    
    public double calcScoreMean() {
        double sum = 0.0;
        for (int i = 1; i <= this.shotsAllowed; i++) {
            sum += this.scores[i - 1];
        }
        return sum / this.shotsAllowed;
    }
    
    public boolean areAllShotsFired() {
        return this.scoreMean > 0;
    }
    
    public void setShotsFiredZero() {
        this.shotsFired = 0;
    }
    
    public void removeItemsFromWorld() {
        ((Background) this.getWorld()).removeObject(this.bullet);
        ((Background) this.getWorld()).removeObjects(this.sensors);
    }
    
    public Projectile getBullet() {
        return this.bullet;
    }
    
    public java.util.ArrayList<Sensor> getSensors() {
        return this.sensors;
    }
    
    public double getScoreTrue() {
        return this.scoreTrue;
    }
    
    public void interpretSensorData(double[] data) {
        // change movespeed and/or direction to catch up to the target
        // take in direction from sensors to target and distance
        if (this.moveSpeed >= 1 && this.moveSpeed <= 8) {
            if (data[0] >= data[1] - 5 && data[0] <= data[1] + 5) {
                this.moveSpeed -= 1;
                if (data[2] < 0.0) {
                    this.direction = 1;
                } else {
                    this.direction = -1;
                }
            } else if (data[0] < data[1] - 4 || data[0] > data[1] + 4) {
                this.moveSpeed += 1;
                if (data[2] < 0.0) {
                    this.direction = -1;
                } else {
                    this.direction = 1;
                }
            }
        } else if (this.moveSpeed < 1) {
            this.moveSpeed = 1;
        } else if (this.moveSpeed > 8) {
            this.moveSpeed = 8;
        }
        /*if (data[2] == 0.0) {
            this.direction = -1;
        } else {
            this.direction = 1;
        }*/
    }
    
    public int[] unpackSeed() {
        int[] retVal = new int[5];
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                retVal[i] = Integer.parseInt(this.seed.substring(0, 2));
            } else if (i == 1) {
                if (this.seed.substring(2, 4).equals("-1")) {
                    retVal[i] = -1;
                } else {
                    retVal[i] = 1;
                }
            } else if (i == 2) {
                retVal[i] = Integer.parseInt(this.seed.substring(4, 6));
            } else if (i == 3) {
                retVal[i] = Integer.parseInt(this.seed.substring(6, 9));
            } else {
                retVal[i] = Integer.parseInt(this.seed.substring(9, 12));
            }
        }
        return retVal;
    }
    
    public void remakeSeed(int[] unpacked) {
        this.seed = this.seedHelp(unpacked[0], "name") +
            this.seedHelp(unpacked[1], "dirc") +
            this.seedHelp(unpacked[2], "move") +
            this.seedHelp(unpacked[3], "rnge") +
            this.seedHelp(unpacked[4], "rnge");
    }
    
    public void readSeed(int[] unpacked) {
        this.name = unpacked[0];
        this.direction = unpacked[1];
        this.moveSpeed = unpacked[2];
        this.wantRangeLeft = unpacked[3];
        this.wantRangeRight = unpacked[4];
        this.remakeSeed(unpacked);
    }
}