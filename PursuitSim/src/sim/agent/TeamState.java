package sim.agent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sim.tasks.Task;

/**
 * Wraps up the team's current state. This is for convenience to collect up the
 * information that should not be serialized (and hence SHOULD be logged) into one place.
 */
class TeamState {

    /** List of agents visible to the team. */
    // TODO (later) consider making this a map, adding a double representing the confidence of each opponent location
    Set<AgentSensorProxy> visibleOpponents;
    /** List of assigned tasks. */
    List<Task> tasks;

    public TeamState() {
        visibleOpponents = new HashSet<AgentSensorProxy>();
        tasks = new ArrayList<Task>();
    }
}
