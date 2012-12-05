package sim.component.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sim.component.VisiblePlayer;
import sim.component.agent.Agent;
import sim.tasks.Task;

/**
 * Wraps up the team's current state. This is for convenience to collect up the
 * information that should not be serialized (and hence SHOULD be logged) into one place.
 */
public class TeamState {

    /** Stores the agents within the team that are active */
    public List<Agent> activeAgents = new ArrayList<Agent>();

    /** Stores the players within the team that are active */
    public List<VisiblePlayer> activePlayers = new ArrayList<VisiblePlayer>();

    /** Stores the agents within the team that are safe */
    public List<VisiblePlayer> safeAgents = new ArrayList<VisiblePlayer>();

    /** List of agents visible to the team. */    
    public Set<VisiblePlayer> visibleOpponents = new HashSet<VisiblePlayer>(); // TODO (later) consider making this a map, adding a double representing the confidence of each opponent location

    /** List of assigned tasks. */
    public List<Task> tasks = new ArrayList<Task>();

    /** List of captured opponents. */
    public HashMap<Agent, Set<VisiblePlayer>> captures = new HashMap<Agent, Set<VisiblePlayer>>();


    void initialize(Team t) {        
        activeAgents = t.getAllAgents();
        activePlayers = t.getAllPlayers();
        safeAgents.clear();
        visibleOpponents.clear();
        tasks.clear();
        captures.clear();
    }

    void addCapture(Agent a, VisiblePlayer target) {
        if (! captures.containsKey(a) )
            captures.put(a, new HashSet<VisiblePlayer>());
        captures.get(a).add(target);
    }
}
