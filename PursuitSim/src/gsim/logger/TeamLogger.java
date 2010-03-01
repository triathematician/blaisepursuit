/**
 * TeamLogger.java
 * Created on Jul 28, 2009
 */

package gsim.logger;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sim.DistanceCache;
import sim.Simulation;
import sim.agent.SimulationAgent;
import sim.agent.SimulationTeam;

/**
 * <p>
 *   <code>TeamLogger</code> stores agent position/time data for a specified team.
 * </p>
 *
 * @author Elisha Peterson
 */
public class TeamLogger extends AbstractSimulationLogger {

    /** The team to log */
    SimulationTeam team;
    /** The agents on the team whose positions are being logged */
    List<SimulationAgent> agents;
    /** The times at which points are logged */
    List<Double> times;
    /** The positions of the agents */
    ArrayList<List<Point2D.Double>> pos;
    /** The velocities of the agents */
    ArrayList<List<Point2D.Double>> vel;

    /**
     * Construct with a simulation and a team. These should not change
     * during the lifetime of this object. If they do, just create a new logger.
     * @param sim
     * @param team
     */
    public TeamLogger(Simulation sim, SimulationTeam team) {
        super(sim);
        this.team = team;
        this.agents = team.getInitiallyActiveAgents();
        times = new ArrayList<Double>();
        pos = new ArrayList<List<Point2D.Double>>();
        vel = new ArrayList<List<Point2D.Double>>();
        reset();
    }

    public SimulationTeam getTeam() {
        return team;
    }

    /** @return list of agents stored by this logger. */
    public List<SimulationAgent> getAgents() {
        return agents;
    }

    /** @return array of values corresponding to the initial locations of the agents */
    public Point2D.Double[] getInitialPositions() {
        Point2D.Double[] result = new Point2D.Double[agents.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = agents.get(i).getParameters().getInitialPosition();
        }
        return result;
    }

    final static Point2D.Double[] DUMMY_ARRAY = new Point2D.Double[]{};

    /** @return path of corresponding agent. */
    public Point2D.Double[] getPath(SimulationAgent sa) {
        return pos.get(agents.indexOf(sa)).toArray(DUMMY_ARRAY);
    }

    /** @return path of the i'th agent. */
    public Point2D.Double[] getPath(int i) {
        return pos.get(i).toArray(DUMMY_ARRAY);
    }

    public void reset() {
        times.clear();
        pos.clear();
        vel.clear();
        agents = team.getInitiallyActiveAgents();
        for (SimulationAgent a : agents) {
            pos.add(new ArrayList<Point2D.Double>());
            vel.add(new ArrayList<Point2D.Double>());
        }
    }

    public void logData(DistanceCache dt, double curTime) {
        times.add(curTime);
        for (int i = 0; i < agents.size(); i++) {
            pos.get(i).add(((Point2D.Double) agents.get(i).getPosition().clone()));
            vel.get(i).add(((Point2D.Double) agents.get(i).getVelocity().clone()));
        }
    }

    public void printData() {
        System.out.println("Logged Team Positions @ Times " + times.toString());
        for(List<Point2D.Double> path : pos) {
            System.out.println("  " + path);
        }
    }
}
