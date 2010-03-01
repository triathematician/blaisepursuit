package sim.agent;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sim.comms.PositionCommEvent;
import sim.comms.TaskCommEvent;
import sim.tasks.Task;

/**
 * Wraps up an agent's current state. This contains all the information that is adjusted and changed
 * throughout a simulation's run.
 */
public class AgentState implements AgentSensorProxy {

    /** Position. */
    Point2D.Double position = null;
    /** Heading. */
    Point2D.Double heading = new Point2D.Double(0,0);
    /** Velocity vector. */
    Point2D.Double velocity = new Point2D.Double(0,0);
    /** Whether the agent is current active. */
    boolean active = true;
    
    /** List of agents visible to this player. */
    // TODO (later) consider making this a map, adding a double representing the confidence of each opponent location
    Set<AgentSensorProxy> visibleOpponents;
    /** List of agents "understood" to exist. */
    Set<AgentSensorProxy> povOpponents;
    /** List of teammates. */
    List<SimulationAgent> availableMates;
    /** List of assigned tasks. */
    List<Task> tasks;
    /** List of positional comm events. */
    List<PositionCommEvent> positionComms;
    /** List of task comm events. */
    List<TaskCommEvent> taskComms;

    /** Sets up the agent state with default values. */
    public AgentState() {
        visibleOpponents = new HashSet<AgentSensorProxy>();
        povOpponents = new HashSet<AgentSensorProxy>();
        availableMates = new ArrayList<SimulationAgent>();
        tasks = new ArrayList<Task>();
        positionComms = new ArrayList<PositionCommEvent>();
        taskComms = new ArrayList<TaskCommEvent>();
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public Point2D.Double getVelocity() {
        return velocity;
    }

    public boolean isActive() {
        return active;
    }
}
