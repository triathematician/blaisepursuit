/**
 * Sensors.java
 * Created on Jul 22, 2009
 */
package sim.comms;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Set;
import sim.DistanceCache;
import sim.component.VisiblePlayer;

/**
 * <p>
 *   <code>Sensors</code> contains various sensors which search for other agents in a simulation, for a given
 *   position and velocity.
 * </p>
 *
 * @author Elisha Peterson
 */
public abstract class Sensor {

    /**
     * Uses the table of distances to look for known opponents. Uses the result
     * to update the mates field.
     * @param dt table of distances between opponents
     * @param vp the player to sense around
     * @param found the place to put the agents that have been found
     * @return list of agents within view
     */
    abstract public void findAgents(DistanceCache dt, VisiblePlayer vp, Set<VisiblePlayer> found);
    /** Returns a name suitable for use with actions. */
    abstract public String getActionName();
    @Override public String toString() { return getActionName(); }



    //============================================//
    //                                            //
    //               FACTORY METHODS              //
    //                                            //
    //============================================//

    /** An enum encoding possible sensor values */
    public enum SensorEnum { NONE, ALL, RADIAL, WEDGE }

    /** Retrieves an instance of the sensor given an enum value */
    public static Sensor getInstance(SensorEnum value) {
        switch (value) {
            case ALL: return GLOBAL_SENSOR;
            case RADIAL: return new Radial();
            case WEDGE: return new Wedge();
            default: return NO_SENSOR;
        }
    }


    //============================================//
    //                                            //
    //               INNER CLASSES                //
    //                                            //
    //============================================//

    /** Unique instance of the null sensor (blind). */
    public static final Sensor NO_SENSOR = new Sensor() {
        /** @return empty list. */
        public void findAgents(DistanceCache dt, VisiblePlayer vp, Set<VisiblePlayer> found) {
            found.clear();
        }
        public String getActionName() { return "No Sensor"; }
    };

    /** Unique instance of the global sensor. */
    public static final Sensor GLOBAL_SENSOR = new  Sensor() {
        /** @return all agents in the simulation, regardless of position. */
        public void findAgents(DistanceCache dt, VisiblePlayer vp, Set<VisiblePlayer> found) {
            found.clear();
            found.addAll(dt.getAllAgents());
        }
        public String getActionName() { return "Global Sensor"; }
    };

    /** This sensor detects all agents within a specified radius. */
    public static class Radial extends Sensor {
        double radius = 50.0;
        public Radial() {}
        public Radial(double radius) { this.radius = radius; }
        public double getRadius() { return radius; }
        public void setRadius(double radius) { this.radius = radius; }
        public void findAgents(DistanceCache dt, VisiblePlayer vp, Set<VisiblePlayer> found) {
            found.clear();
            found.addAll(dt.getAgentsInRadius(vp, radius));
        }
        public String getActionName() { return "Radial Sensor"; }
        @Override public String toString() { return getActionName() + String.format(" [r=%.2f]", radius); }
    }  // INNER CLASS RadialSensor

    /** This sensor detects all agents within a specified radius, and angle relative to the agent's direction. */
    public static class Wedge extends Sensor {
        double radius = 50.0;
        double theta = Math.PI/2;
        public Wedge() {}
        public Wedge(double radius, double theta) { this.radius = radius; this.theta = theta; }
        public double getRadius() { return radius; }
        public void setRadius(double radius) { this.radius = radius; }
        public double getTheta() { return theta; }
        public void setTheta(double theta) { this.theta = theta; }
        public void findAgents(DistanceCache dt, VisiblePlayer vp, Set<VisiblePlayer> found) {
            found.clear();
            found.addAll(dt.getAgentsInWedge(vp, radius, theta));
        }
        public String getActionName() { return "Wedge Sensor"; }
        @Override public String toString() { return getActionName() + String.format(" [r=%.2f, theta=%.2f]", radius, theta); }
    }  // INNER CLASS RadialSensor
}
