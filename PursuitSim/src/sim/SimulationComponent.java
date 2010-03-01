/**
 * SimulationComponent.java
 * Created on Jul 14, 2009
 */
package sim;

/**
 * <p>
 *   <code>SimulationComponent</code> represents an element of a simulation that evolves over
 *   time. Registers methods that are called each step of the simulation. Examples are agents and teams.
 *   This class essentially uses the <i>Template Pattern</i> in conjunction with the <code>Simulation</code>
 *   class.
 * </p>
 *
 * @author Elisha Peterson
 */
public abstract class SimulationComponent {

    public SimulationComponent() {
    }

    //
    // INITIALIZERS
    //

    /**
     * Initializes the component's state variables for a new simulation.
     * This is always called immediately prior to running the simulation.
     */
    abstract public void initStateVariables();


    //
    // ITERATION METHODS
    //

    /** Gather sensory data within this component based on the current table of distances.
     * Sub-classes, particularly <code>Team</code> may implement this by enforcing
     * some kind of communication scheme.
     * @param dt a table of distances between agents, used for computational efficiency */
    public void gatherSensoryData(DistanceCache dt) {};

    /** Uses sensory data and communicated data to generate an "opinion" about what's out there.
     * Components may choose to generate comm events using the specified broadcaster. */
    public void developPointOfView() {};

    /** Generates tasks based on the current point-of-view.
     * Components may choose to generate comm events using the specified broadcaster.
     * @param dt a table of distances between agents, used for computational efficiency */
    public void generateTasks(DistanceCache dt) {};

    /** Sets control variables based on the self-generated and incoming tasks.
     * @param curTime the current simulation time
     * @param timePerStep the time between iterations of the simulation */
    abstract public void setControlVariables(double curTime, double timePerStep);

    /** Adjusts the state based on the control variables. */
    abstract public void adjustState(double timePerStep);

    /** Checks for any major adjustments that need to be made. */
    public void handleMajorEvents(DistanceCache dc, double curTime) {};

    /** Checks for victory that is achieved. */
    public void checkVictory(DistanceCache dc, double curTime) throws SimulationTerminatedException {};
}
