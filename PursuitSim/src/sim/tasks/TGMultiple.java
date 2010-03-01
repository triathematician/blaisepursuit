package sim.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import sim.DistanceCache;
import sim.agent.SimulationAgent;
import sim.agent.SimulationTeam;
import sim.agent.AgentSensorProxy;

class TGMultiple extends Tasker {

    ArrayList<Tasker> generators;
    double defaultPriority = 1.0;

    TGMultiple(Tasker... tgs) {
        super();
        generators = new ArrayList<Tasker>();
        for (Tasker t : tgs)
            generators.add(t);
    }

    void add(Tasker newTG) {
        generators.add(newTG);
    }

    public List<Task> generateGiven(DistanceCache dt, SimulationTeam owner, Collection<? extends AgentSensorProxy> visibleOpponents) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (Tasker tg : generators) {
            tasks.addAll(tg.generateGiven(dt, owner, visibleOpponents));
        }
        return tasks;
    }

    public List<Task> generateGiven(DistanceCache dt, SimulationAgent owner, Collection<? extends AgentSensorProxy> visibleOpponents, Collection<SimulationAgent> teammates) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (Tasker tg : generators) {
            tasks.addAll(tg.generateGiven(dt, owner, visibleOpponents, teammates));
        }
        return tasks;
    }

    public Set<? extends AgentSensorProxy> getTargetAgents() {
        return Collections.emptySet();
    }

    public void setTargetAgents(Set<? extends AgentSensorProxy> newTarget) {
        if (newTarget.size() > 0) {
            throw new IllegalArgumentException("Attempt to set target agents for a multiple task generator instance: " + newTarget.size());
        }
    }

    public double getDefaultPriority() {
        return defaultPriority;
    }

    public void setDefaultPriority(double newValue) {
        if (newValue < 0 || newValue > 1) {
            throw new IllegalArgumentException("Priority outside the [0,1] range: " + newValue);
        }
        this.defaultPriority = newValue;
    }

    public Task.Type getTaskType() {
        return Task.Type.NONE;
    }

    @Override
    public String toString() {
        return super.toString() + "multiple";
    }
}
