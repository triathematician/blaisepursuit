/**
 * AbstractTaskGenerator.java
 * Created on Jul 24, 2009
 */
package sim.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sim.DistanceCache;
import sim.agent.SimulationAgent;
import sim.agent.SimulationTeam;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *   <code>AbstractTaskGenerator</code> ...
 * </p>
 *
 * @author Elisha Peterson
 */
public abstract class TaskerSupport extends Tasker {

    double defaultPriority = 1.0;
    Set<? extends AgentSensorProxy> targetAgents;
    Task.Type type = Task.Type.SEEK;

    public TaskerSupport() {
        targetAgents = new HashSet<AgentSensorProxy>();
    }

    public TaskerSupport(Collection<? extends AgentSensorProxy> targetAgents, Task.Type type) {
        if (targetAgents instanceof Set) {
            this.targetAgents = (Set) targetAgents;
        } else {
            this.targetAgents = new HashSet<AgentSensorProxy>(targetAgents);
        }
        this.type = type;
    }

    public double getDefaultPriority() {
        return defaultPriority;
    }

    public void setDefaultPriority(double newValue) {
        if (newValue < 0) {
            throw new IllegalArgumentException("priority < 0: " + newValue);
        }
        this.defaultPriority = newValue;
    }

    public Task.Type getTaskType() {
        return type;
    }

    public void setTaskType(Task.Type type) {
        this.type = type;
    }

    public String getTaskTypeByName() {
        return type==null ? "" : type.name();
    }

    public void setTaskTypeByName(String name) {
        try {
            type = Task.Type.valueOf(name);
        } catch (IllegalArgumentException ex) {
        }
    }

    public Set<? extends AgentSensorProxy> getTargetAgents() {
        return targetAgents;
    }

    public void setTargetAgents(Set<? extends AgentSensorProxy> newTarget) {
        this.targetAgents = newTarget;
    }

    /**
     * Convenience method. Generates a list of tasks for sub-agents within the team, by
     * calling the task generator on each agent within the team. If the behavior of the
     * task generator is independent of the team's structure, they may wish to use this
     * method to obtain the list of tasks.
     * @param tg the task generator to use
     * @param dt the cache of distances between agents
     * @param owner the team generating the tasks
     * @param visibleOpponents the opponents visible to the team
     * @return list of tasks for the entire team
     */
    public List<Task> generateGiven(DistanceCache dt, SimulationTeam owner, Collection<? extends AgentSensorProxy> visibleOpponents) {
        ArrayList<Task> result = new ArrayList<Task>();
        List<SimulationAgent> teammates = owner.getActiveAgents();
        for (SimulationAgent a : owner.getActiveAgents()) {
            result.addAll(generateGiven(dt, a, visibleOpponents, teammates));
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("Tasker [ priority = %.2f ] - ", this.defaultPriority);
    }
}
