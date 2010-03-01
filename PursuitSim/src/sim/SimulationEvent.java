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
    private Simulation source;
    private DistanceCache dc;
    private double simTime;
    private String message;

    public SimulationEvent(Simulation source, String message, DistanceCache dc, double simTime) {
        this.source = source;
        this.dc = dc;
        this.message = message;
        this.simTime = simTime;
    }

    public DistanceCache getDistances() {
        return dc;
    }

    public Simulation getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public double getSimTime() {
        return simTime;
    }

    public void changeTo(String message, DistanceCache dc, double simTime) {
        this.message = message;
        this.simTime = simTime;
        this.dc = dc;
    }
}
