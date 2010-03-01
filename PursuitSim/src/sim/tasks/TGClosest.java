/*
 * TGClosest.java
 * Created on Sep 4, 2007, 2:17:30 PM
 */
package sim.tasks;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import sim.DistanceCache;
import sim.agent.SimulationAgent;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *  Player only considers the closest enemy.
 * </p>
 * @author Elisha Peterson
 */
public class TGClosest extends TaskerSupport {

    public TGClosest() {
    }

    /**
     * Constructs a generator that assigns the closets of the visible targets to the task's owner.
     * @param targetAgents list of targets
     */
    public TGClosest(Collection<? extends AgentSensorProxy> targetAgents, Task.Type type) {
        super(targetAgents, type);
    }

    public List<Task> generateGiven(DistanceCache dt, SimulationAgent owner, Collection<? extends AgentSensorProxy> visibleOpponents, Collection<SimulationAgent> teammates) {
        assert visibleOpponents != null;
        assert teammates != null;

        // reduce to visible opponents only
        HashSet<AgentSensorProxy> visOpps = new HashSet<AgentSensorProxy>(visibleOpponents);
        visOpps.retainAll(targetAgents);
        if (visOpps.size() == 0) {
            return Collections.emptyList();
        }

        // find the closest target
        AgentSensorProxy aspTarget = dt.getClosestAgent(owner, visOpps);

        // return task
        if (aspTarget == null) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(new Task(owner, aspTarget, defaultPriority, type));
        }
    }

    @Override
    public String toString() {
        return super.toString() + "closest agent";
    }
}
