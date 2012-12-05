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
import sim.SimulationEvent;
import sim.component.InitialPositionSetter;
import sim.component.agent.Agent;
import sim.component.team.Team;

/**
 * <p>
 *   <code>TeamLogger</code> stores agent position/time data for a specified team.
 * </p>
 *
 * @author Elisha Peterson
 */
public class TeamLogger extends SimulationLogger {

    /** The team to log */
    Team team;
    /** The agents on the team whose positions are being logged */
    List<Agent> agents;
    /** The initial positions for the team */
    List<InitialPositionSetter> init;
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
    public TeamLogger(Simulation sim, Team team) {
        super(sim);
        this.team = team;
        times = new ArrayList<Double>();
        pos = new ArrayList<List<Point2D.Double>>();
        vel = new ArrayList<List<Point2D.Double>>();
    }

    public Team getTeam() {
        return team;
    }

    /** @return list of agents stored by this logger. */
    public List<Agent> getAgents() {
        return agents;
    }

    /** @return array of values corresponding to the initial locations of the agents */
    public Point2D.Double[] getInitialPositions() {
        Point2D.Double[] result = new Point2D.Double[init.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = init.get(i).getInitialPosition();
        return result;
    }

    final static Point2D.Double[] DUMMY_ARRAY = new Point2D.Double[]{};

    /** @return path of corresponding agent. */
    public Point2D.Double[] getPath(Agent sa) {
        return pos.get(agents.indexOf(sa)).toArray(DUMMY_ARRAY);
    }

    /** @return path of the i'th agent. */
    public Point2D.Double[] getPath(int i) {
        return pos.get(i).toArray(DUMMY_ARRAY);
    }

    /** @return the i'th initial position. */
    public InitialPositionSetter getInitialPositionSetter(int idx) {
        return init.get(idx);
    }

    public void handleResetEvent(SimulationEvent e) {
        times.clear();
        pos.clear();
        vel.clear();
        agents = team.getComponentsByType(Agent.class);
        for (Agent a : agents) {
            pos.add(new ArrayList<Point2D.Double>());
            vel.add(new ArrayList<Point2D.Double>());
        }
        init = team.getComponentsByType(InitialPositionSetter.class);
    }

    public void logData(DistanceCache dt, double curTime) {
        times.add(curTime);
        for (int i = 0; i < agents.size(); i++) {
            pos.get(i).add(((Point2D.Double) agents.get(i).state.position.clone()));
            vel.get(i).add(agents.get(i).getVelocity() == null
                    ? new Point2D.Double() 
                    : (Point2D.Double) agents.get(i).state.velocity.clone());
        }
    }

    public void printData() {
        System.out.println("Logged Team Positions @ Times " + times.toString());
        for(List<Point2D.Double> path : pos) {
            System.out.println("  " + path);
        }
    }
}
