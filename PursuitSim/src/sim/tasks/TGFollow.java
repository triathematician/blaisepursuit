/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sim.DistanceCache;
import sim.SimulationComponent;
import sim.agent.SimulationAgent;
import sim.agent.SimulationTeam;
import sim.agent.AgentSensorProxy;

/**
 * Represents a task where the agent "follows" the first opponent agent.
 * 
 * @author ae3263
 */
public class TGFollow extends Tasker {

    AgentSensorProxy target;
    Task.Type type = Task.Type.SEEK;
    double priority = 1.0;

    public TGFollow(){}

    /**
     * Constructs a generator where the agent "follows" the first opponent agent.
     * @param targetAgents list of targets
     */
    public TGFollow(AgentSensorProxy target, Task.Type type) {
        this.target = target;
        this.type = type;
    }

    public Set<? extends AgentSensorProxy> getTargetAgents() {
        HashSet<AgentSensorProxy> result = new HashSet<AgentSensorProxy>();
        result.add(target);
        return result;
    }

    public void setTargetAgents(Set<? extends AgentSensorProxy> newTarget) {
        for (AgentSensorProxy asp : newTarget) {
            target = asp;
            return;
        }
    }

    public double getDefaultPriority() {
        return priority;
    }

    public void setDefaultPriority(double newValue) {
        this.priority = newValue;
    }

    public Task.Type getTaskType() {
        return type;
    }

    public List<Task> generateGiven(DistanceCache dt, SimulationTeam owner, Collection<? extends AgentSensorProxy> visibleOpponents) {
        ArrayList<Task> result = new ArrayList<Task>();
        if (visibleOpponents.contains(target)) {
            for (SimulationComponent sc : owner.getActiveAgents()) {
                if (sc instanceof SimulationAgent)
                    result.add(new Task((SimulationAgent) sc, target, priority, type));
            }
        }
        return result;
    }

    public List<Task> generateGiven(DistanceCache dt, SimulationAgent owner, Collection<? extends AgentSensorProxy> visibleOpponents, Collection<SimulationAgent> teammates) {
        if (visibleOpponents.contains(target)) {
            return Arrays.asList(new Task(owner, target, priority, type));
        } else {
            return Arrays.asList();
        }
    }

    @Override
    public String toString() {
        return String.format("Tasker [ priority = %.2f ] - ", this.priority) + "follow";
    }

}
