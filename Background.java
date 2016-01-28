import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 *  This is the World of this project. This is more than just a background.
 * This stores all of the current instances of the genetic program and this
 * is called upon when a new generation needs to be made.
 * 
 * @author John Geddes
 * @version Nov 5, 2013 - Mar 3, 2014
 */
public class Background extends World {
    private Target brick = new Target();
    private int generation = 0;
    private java.util.ArrayList<Shooter> shooters = new java.util.ArrayList<Shooter>(11);
    private java.util.ArrayList<Shooter> newGen = new java.util.ArrayList<Shooter>(11);
    private java.util.ArrayList<Shooter> copy = new java.util.ArrayList<Shooter>(11);
    
    public int makeRandShooterRandDir() {
        if ((int) Math.random() == 1) {
            return 1;
        } else {
            return -1;
        }
    }
    
    public Shooter makeRandShooter(int num) {
        return new Shooter(num,
            this.makeRandShooterRandDir(),
            (int) (Math.random() * 6 + 1),
            (int) (Math.random() * 290 + 10),
            (int) (Math.random() * 290 + 300),
            (int) (Math.random() * 90 + 10));
    }
    
    public void addShootersToList() {
        for (int i = 0; i <= 10; i++) {
            this.shooters.add(i, this.makeRandShooter(i));
        }
    }
    
    public Background() {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1);
        this.setPaintOrder(Target.class, Projectile.class, Shooter.class, Sensor.class);
        prepare();
    }
    
    public void addShootersToWorld() {
        for (int i = 0; i < this.shooters.size(); i++) {
            this.addObject(this.shooters.get(i), 50 + 50 * i, 350);
        }
    }
    
    public void tellShootersAddItems() {
        for (int i = 0; i < this.shooters.size(); i++) {
            this.shooters.get(i).insertItems();
        }
    }
    
    public Shooter getHigherScoringShooter(Shooter thing1, Shooter thing2) {
        if (thing1.getScoreTrue() >= thing2.getScoreTrue()) {
            return thing1;
        } else {
            return thing2;
        }
    }
    
    public Shooter getHighestScoringShooter(java.util.ArrayList<Shooter> list) {
        Shooter highest = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            highest = getHigherScoringShooter(highest, list.get(i)); //EDITED
        }
        return highest;
    }
    
    public Shooter getShooterFromRank(java.util.ArrayList<Shooter> list, int rank) {
        if (rank == 1) {
            return this.getHighestScoringShooter(list);
        } else {
            list.remove(this.getHighestScoringShooter(list));
            return this.getShooterFromRank(list, rank - 1);
        }
    }
    
    public Shooter getShooterFromRank(int rank) {
        java.util.ArrayList<Shooter> shootersCopy = this.copyShootersFrom(this.shooters);
        if (rank == 1) {
            return this.getHighestScoringShooter(shootersCopy);
        } else {
            shootersCopy.remove(this.getHighestScoringShooter(shootersCopy));
            return this.getShooterFromRank(shootersCopy, rank - 1);
        }
    }
    
    public Target getTarget() {
        return this.brick;
    }
    
    public int getGeneration() {
        return this.generation;
    }
    
    public java.util.ArrayList<Shooter> getShooters() {
        return this.shooters;
    }
    
    public boolean areAllShotsFired() {
        int checkVal = 0;
        for (int i = 0; i < this.shooters.size(); i++) {
            if (this.shooters.get(i).areAllShotsFired()) {
                checkVal++;
            }
        }
        return checkVal == this.shooters.size();
    }
    
    public void eraseCopy() {
        for (int i = 0; i < this.shooters.size(); i++) {
            this.copy.add(null);
        }
    }
    
    public void eraseNewGen() {
        for (int i = 0; i < this.shooters.size(); i++) {
            this.newGen.add(null);
        }
    }
    
    public java.util.ArrayList<Shooter> copyShootersFrom(java.util.ArrayList<Shooter> list) {
        java.util.ArrayList<Shooter> tempList = new java.util.ArrayList<Shooter>(11);
        for (int i = 0; i < list.size(); i++) {
            Shooter tempShooter = this.makeRandShooter(i);
            tempShooter.readSeed(list.get(i).unpackSeed());
            tempList.add(i, tempShooter);
        }
        return tempList;
    }
    
    public void removeShootersFromWorld() {
        for (int i = 0; i < this.shooters.size(); i++) {
            this.shooters.get(i).removeItemsFromWorld();
        }
        this.removeObjects(this.shooters);
    }
    
    public void makeNewGeneration() {
        this.copy = this.copyShootersFrom(this.shooters);
        for (int i = 0; i < this.shooters.size(); i++) {
            int randInt = (int) (Math.random() * 3 + 1);
            if (randInt != 3 || i == this.shooters.size() - 1) {
                Shooter randShooter = this.shooters.get(i);
                if (randInt == 1) {
                    this.newGen.set(i, this.reproduction(randShooter));
                } else {
                    this.newGen.set(i, this.mutation(randShooter));
                }
                int[] unpackedSeed = this.newGen.get(i).unpackSeed();
                unpackedSeed[0] = i;
                this.newGen.get(i).readSeed(unpackedSeed);
                this.newGen.get(i).setShotsFiredZero();
            } else {
                Shooter randShooter1 = this.shooters.get(i);
                Shooter randShooter2 = this.shooters.get(i + 1);
                Shooter[] tempVal = this.crossover(randShooter1, randShooter2);
                this.newGen.set(i, tempVal[0]);
                this.newGen.set(i + 1, tempVal[1]);
                int[] unpackedSeed1 = this.newGen.get(i).unpackSeed();
                int[] unpackedSeed2 = this.newGen.get(i + 1).unpackSeed();
                unpackedSeed1[0] = i;
                unpackedSeed2[0] = i + 1;
                this.newGen.get(i).readSeed(unpackedSeed1);
                this.newGen.get(i + 1).readSeed(unpackedSeed2);
                this.newGen.get(i).setShotsFiredZero();
                this.newGen.get(i + 1).setShotsFiredZero();
            }
        }
        for (int j = 0; j < this.newGen.size(); j++) {
            if (this.newGen.get(j) == null) {
                this.newGen.set(j, this.makeRandShooter(j));
            }
        }
        this.removeShootersFromWorld();
        this.shooters = this.copyShootersFrom(this.newGen);
        this.eraseCopy();
        this.addShootersToWorld();
        this.tellShootersAddItems();
        this.generation++;
    }
    
    public Shooter reproduction(Shooter obj) {
        int randInt = (int) (Math.random() * 4 + 1);
        return this.getShooterFromRank(randInt);
    }
    
    public Shooter mutation(Shooter obj) {
        int randInt = (int) (Math.random() * 3);
        boolean randBool = (int) (Math.random()) == 1 ? true : false;
        Shooter copiedShooter = this.makeRandShooter(0);
        int[] unpackedSeed = obj.unpackSeed();
        if (randInt == 0) {
            if (randBool) {
                unpackedSeed[1] = 1;
            } else {
                unpackedSeed[1] = -1;
            }
        } else if (randInt == 1) {
            int newRandInt = (int) (Math.random() * 3 + 1);
            if (randBool && unpackedSeed[2] + newRandInt <= 10) {
                unpackedSeed[2] += newRandInt;
            } else if (randBool && unpackedSeed[2] + newRandInt > 10) {
                unpackedSeed[2] = 10;
            } else if (unpackedSeed[2] - newRandInt >= 1) {
                unpackedSeed[2] -= newRandInt;
            } else {
                unpackedSeed[2] = 1;
            }
        } else if (randInt == 2) {
            int newRandInt = (int) (Math.random() * 40 + 20);
            if (randBool && unpackedSeed[3] + newRandInt < 300) {
                unpackedSeed[3] += newRandInt;
            } else if (randBool && unpackedSeed[3] + newRandInt >= 300) {
                unpackedSeed[3] = 299;
            } else if (unpackedSeed[3] - newRandInt >= 10) {
                unpackedSeed[3] -= newRandInt;
            } else {
                unpackedSeed[3] = 10;
            }
        } else {
            int newRandInt = (int) (Math.random() * 40 + 20);
            if (randBool && unpackedSeed[4] + newRandInt <= 590) {
                unpackedSeed[4] += newRandInt;
            } else if (randBool && unpackedSeed[4] + newRandInt > 590) {
                unpackedSeed[4] = 299;
            } else if (unpackedSeed[4] - newRandInt >= 300) {
                unpackedSeed[4] -= newRandInt;
            } else {
                unpackedSeed[4] = 299; //EDITED
            }
        }
        copiedShooter.readSeed(unpackedSeed);
        return copiedShooter;
    }
    
    public Shooter[] crossover(Shooter obj1, Shooter obj2) {
        Shooter[] retVal = new Shooter[2];
        int randInt = (int) (Math.random() * 3 + 1);
        Shooter copy1 = obj1;
        Shooter copy2 = obj2;
        int[] unpackedSeed1 = obj1.unpackSeed();
        int[] unpackedSeed2 = obj2.unpackSeed();
        int tempVal = unpackedSeed1[randInt];
        unpackedSeed1[randInt] = unpackedSeed2[randInt]; //EDITED
        unpackedSeed1[randInt] = tempVal;                //EDITED
        copy1.readSeed(unpackedSeed1);
        copy2.readSeed(unpackedSeed2);
        retVal[0] = copy1;
        retVal[1] = copy2;
        return retVal;
    }
    
    /*public double calcRawFitness() {
    /*    
    }*/
    
    /**
     *  Prepare the World for the start of the program. That is: create the
     * initial objects and add them to the World.
     */
    private void prepare() {
        this.addObject(brick, 300, 50);
        this.addShootersToList();
        this.addShootersToWorld();
        this.tellShootersAddItems();
        this.eraseCopy();
        this.eraseNewGen();
    }
}