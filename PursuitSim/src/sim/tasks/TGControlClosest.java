/**
 * TGControlClosest.java
 * Created on Jul 24, 2009
 */
package sim.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import sim.DistanceCache;
import sim.agent.SimulationAgent;
import sim.agent.SimulationTeam;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *   <code>TGControlClosest</code> ...
 * </p>
 *
 * @author Elisha Peterson
 */
public class TGControlClosest extends TaskerSupport {

    public TGControlClosest(){}

    /**
     * Constructs a generator that assigns the closets of the visible targets to the task's owner.
     * @param targetAgents list of targets
     */
    public TGControlClosest(Collection<? extends AgentSensorProxy> targetAgents, Task.Type type) {
        super(targetAgents, type);
    }

    /** Returns empty list. */
    public List<Task> generateGiven(DistanceCache dt, SimulationAgent owner, Collection<? extends AgentSensorProxy> visibleOpponents, Collection<SimulationAgent> teammates) {
        return Collections.emptyList();
    }

    /**
     * Assigns tasks (prey) to n/k pursuers. The algorithm uses the complete table
     * of distances between pursuers and evaders to map the closest
     * pursuer-prey pair. This process is repeated until there are no more
     * pursuers or prey available. If there are leftover pursuers, they are
     * assigned to the closest prey.
     * @param owner pursuing team
     */
    @Override
    public List<Task> generateGiven(DistanceCache dt, SimulationTeam owner, Collection<? extends AgentSensorProxy> visibleOpponents) {

        // reduce to visible opponents only
        HashSet<AgentSensorProxy> visOpps = new HashSet<AgentSensorProxy>(visibleOpponents);
        visOpps.retainAll(targetAgents);
        if (visOpps.size() == 0) {
            return Collections.emptyList();
        }

        Map<AgentSensorProxy, AgentSensorProxy> assignments = dt.getAssignmentMapByClosestFirst(owner.getActiveAgents(), visOpps);
        ArrayList<Task> tasks = new ArrayList<Task>(assignments.size());

        for (Entry<AgentSensorProxy, AgentSensorProxy> e : assignments.entrySet()) {
            tasks.add(new Task((SimulationAgent) e.getKey(), e.getValue(), defaultPriority, type));
        }

        return tasks;
    }

    @Override
    public String toString() {
        return super.toString() + "control closest";
    }
}
