/**
 * TeamLogger.java
 * Created on Jul 28, 2009
 */

package gsim.logger;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import sim.DistanceCache;
import sim.Simulation;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *   <code>TeamLogger</code> stores agent position/time data for a specified team.
 * </p>
 *
 * @author Elisha Peterson
 */
public class AgentLogger extends AbstractSimulationLogger {

    /** The agent whose position is being logged */
    AgentSensorProxy agent;
    /** The times at which points are logged */
    List<Double> times;
    /** List of positions */
    List<Point2D.Double> pos;
    /** List of velocities */
    List<Point2D.Double> vel;

    /**
     * Construct with a simulation and a team. These should not change
     * during the lifetime of this object. If they do, just create a new logger.
     * @param sim
     * @param team
     */
    public AgentLogger(Simulation sim, AgentSensorProxy agent) {
        super(sim);
        this.agent = agent;
        times = new ArrayList<Double>();
        pos = new ArrayList<Point2D.Double>();
        vel = new ArrayList<Point2D.Double>();
        reset();
    }

    /** @return list of agents stored by this logger. */
    public AgentSensorProxy getAgent() {
        return agent;
    }

    /** @return first location. */
    public Point2D.Double getInitialPosition() {
        if (pos == null || pos.size() == 0)
            return new Point2D.Double();
        else
            return pos.get(0);
    }

    final static Point2D.Double[] DUMMY_ARRAY = new Point2D.Double[]{};

    /** @return path of corresponding agent. */
    public Point2D.Double[] getPath() {
        return pos.toArray(DUMMY_ARRAY);
    }

    public void reset() {
        times.clear();
        pos.clear();
        vel.clear();
    }

    public void logData(DistanceCache dt, double curTime) {
        times.add(curTime);
        if (agent.isActive()) {
            pos.add((Point2D.Double) agent.getPosition().clone());
            vel.add((Point2D.Double) agent.getVelocity().clone());
        }
    }

    @Override
    public void printData() {
        System.out.println("Logged Positions @ Times " + times.toString());
        System.out.println("  " + pos);
    }
}
