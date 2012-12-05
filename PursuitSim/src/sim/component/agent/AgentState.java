package sim.component.agent;

import sim.component.VisiblePlayer;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sim.tasks.Task;

/**
 * Wraps up an agent's current state. This contains all the information that is adjusted and changed
 * throughout a simulation's run.
 */
public class AgentState implements VisiblePlayer {

    /** Position. */
    public Point2D.Double position = new Point2D.Double();
    /** Velocity vector. */
    public Point2D.Double velocity = null;

    /** Whether the agent is current active */
    public boolean active = true;
    /** Whether the agent is currently "safe" */
    boolean safe = false;
    
    /** List of agents visible to this player. */
    // TODO (later) consider making this a map, adding a double representing the confidence of each opponent location
    HashSet<VisiblePlayer> visiblePlayers = new HashSet<VisiblePlayer>();
    /** Communicated player locations. */
    HashSet<VisiblePlayer> commPlayers = new HashSet<VisiblePlayer>();
    /** List of agents "understood" to exist. */
    HashSet<VisiblePlayer> povPlayers = new HashSet<VisiblePlayer>();

    /** List of teammates. */
    ArrayList<Agent> commTeam = new ArrayList<Agent>();

    /** List of assigned tasks. */
    ArrayList<Task> tasks = new ArrayList<Task>();;
    /** List of task comm events. */
    ArrayList<Task> commTasks = new ArrayList<Task>();

    public Point2D.Double getPosition() {
        return position;
    }

    public Point2D.Double getVelocity() {
        return velocity;
    }

    public boolean isActive() {
        return active;
    }

    void initialize(double x, double y) {
        position.x = x;
        position.y = y;
        velocity = null;
        active = true;
        safe = false;
        visiblePlayers.clear();
        commPlayers.clear();
        povPlayers.clear();
        commTeam.clear();
        tasks.clear();
        commTasks.clear();
    }
}
