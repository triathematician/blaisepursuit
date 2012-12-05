/**
 * SimulationEvent.java
 * Created on Jul 28, 2009
 */

package sim;

/**
 * <p>
 *   <code>SimulationEvent</code> is a significant simulation event that may be passed
 *   on to interested listeners.
 * </p>
 *
 * @author Elisha Peterson
 */
public class SimulationEvent {
    public Simulation source;
    public DistanceCache distances;
    public double time;
    public String message;

    public SimulationEvent(Simulation source, String message, DistanceCache dc, double simTime) {
        this.source = source;
        this.distances = dc;
        this.message = message;
        this.time = simTime;
    }

    public DistanceCache getDistances() {
        return distances;
    }

    public Simulation getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public double getSimTime() {
        return time;
    }

    public void update(DistanceCache dc, double simTime) {
        this.time = simTime;
        this.distances = dc;
    }
}
