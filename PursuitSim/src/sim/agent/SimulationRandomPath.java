/**
 * RandomPathFollowingAgent.java
 * Created on Jul 17, 2009
 */
package sim.agent;

import java.awt.geom.Point2D;
import sim.SimulationComponent;

// TODO (low priority) add option to choose between uniform and normal angle distributions

/**
 * <p>
 *   <code>RandomPathFollowingAgent</code> represents an agent who moves with random changes to his position over time
 * </p>
 *
 * @author Elisha Peterson
 */
public class SimulationRandomPath extends SimulationComponent
        implements AgentSensorProxy, InitialPositionSetter {
    
    //
    // BASIC PARAMETERS
    //

    /** Starting position. */
    Point2D.Double init = new Point2D.Double();
    /** The accepted rate of change of the angle. */
    double maxAngleChange = Math.PI / 6;
    /** The top speed of the random agent. */
    double topSpeed = 1.0;

    //
    // STATE VARIABLES
    //

    /** Location of the agent. */
    transient Point2D.Double position = new Point2D.Double();
    /** The angle of the agent. */
    transient double angle = 0.0;

    //
    // CONSTRUCTOR
    //

    public SimulationRandomPath() {
    }

    @Override
    public String toString() {
        return String.format("SimulationRandomPath @ (%.2f, %.2f) [%.2f ; %.2f]", init.x, init.y, maxAngleChange, topSpeed);
    }

    //
    // GETTERS & SETTERS
    //

    public Point2D.Double getInitialPosition() {
        return init;
    }

    public void setInitialPosition(Point2D.Double init) {
        this.init = init;
    }

    public double getThetaChange() {
        return maxAngleChange;
    }

    public void setThetaChange(double thetaChange) {
        this.maxAngleChange = thetaChange;
    }

    public double getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(double topSpeed) {
        this.topSpeed = topSpeed;
    }

    //
    // AgentSensorProxy METHODS: **getters ONLY**
    //

    public Point2D.Double getPosition() {
        return position;
    }

    public Point2D.Double getVelocity() {
        return new Point2D.Double(topSpeed * Math.cos(angle), topSpeed * Math.sin(angle));
    }

    public boolean isActive() {
        return true;
    }

    //
    // SimulationComponent METHODS
    //

    /** Resets position of agent to the origin, and generates a random starting angle. */
    @Override
    public void initStateVariables() {
        position = (Point2D.Double) init.clone();
        angle = 2 * Math.PI * Math.random();
    }

    @Override
    public void setControlVariables(double curTime, double timePerStep) {
        angle += maxAngleChange * (2 * Math.random() - 1) * timePerStep;
    }

    @Override
    public void adjustState(double timePerStep) {
        position.x += topSpeed * Math.cos(angle) * timePerStep;
        position.y += topSpeed * Math.sin(angle) * timePerStep;
    }

}
