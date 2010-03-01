/**
 * TaskGenerators.java
 * Created on Jul 22, 2009
 */
package sim.tasks;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import sim.DistanceCache;
import sim.agent.SimulationAgent;
import sim.agent.SimulationTeam;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *   <code>TaskGenerators</code> collects all the task generators currently implemented.
 * </p>
 *
 * @author Elisha Peterson
 */
public class Taskers {

    //
    // FACTORY METHODS
    //

    /**
     * Factory method for a generator that encloses multiple task generators.
     * @param generators the generators to enclose within the multiple
     * @return instance of a TaskGenerator that wraps up the specified generators
     */
    public static final Tasker multipleTaskerInstance(Tasker... generators) {
        return new TGMultiple(generators);
    }

    /**
     * Factory method for a generator that follows a single agent, i.e. only generates a single task.
     * @param targetAgents the object of the task generator
     * @param taskType the type of the tasks generated
     * @return instance of a generator
     */
    public static final Tasker followInstance(AgentSensorProxy targetAgent, Task.Type taskType) {
        return new TGFollow(targetAgent, taskType);
    }

    /**
     * Factory method for a generator that assigns agents to the closest competitors.
     * @param targetAgents the object of the task generator
     * @param taskType the type of the tasks generated
     * @return instance of a generator
     */
    public static final Tasker findClosestInstance(Collection<? extends AgentSensorProxy> targetAgents, Task.Type taskType) {
        return new TGClosest(targetAgents, taskType);
    }

    /**
     * Factory method to return a generator that creates tasks based on following
     * a gradient to minimize/maximize distance to the target team.
     * @param targetAgents the object of the task generator
     * @param taskType the type of the tasks generated
     * @return instance of a generator
     */
    public static Tasker gradientInstance(Collection<? extends AgentSensorProxy> targetAgents, Task.Type taskType) {
        return new TGGradient(targetAgents, taskType);
    }

    /**
     * Factory method for a generator that assigns agents to the closest competitors.
     * @param targetAgents the object of the task generator
     * @param taskType the type of the tasks generated
     * @return instance of a generator
     */
    public static final Tasker centerOfMassInstance(Collection<? extends AgentSensorProxy> targetAgents, Task.Type taskType) {
        return new TGCenterOfMass(targetAgents, taskType);
    }

    /**
     * Factory method for a generator that assigns agents to the closest competitors.
     * @param targetAgents the object of the task generator
     * @param taskType the type of the tasks generated
     * @return instance of a generator
     */
    public static final Tasker controlClosestInstance(Collection<? extends AgentSensorProxy> targetAgents, Task.Type taskType) {
        return new TGControlClosest(targetAgents, taskType);
    }

    /**
     * Factory method for a generator that assigns agents to the closest competitors.
     * @param targetAgents the object of the task generator
     * @param taskType the type of the tasks generated
     * @return instance of a generator
     */
    public static final Tasker averageHeadingInstance(Collection<? extends AgentSensorProxy> targetAgents, Task.Type taskType) {
        return new TGAverageHeading(targetAgents, taskType);
    }

    /**
     * Factory method for a generator that assigns agents to the closest competitors.
     * @param targetAgents the object of the task generator
     * @param taskType the type of the tasks generated
     * @return instance of a generator
     */
    public static final Tasker weightedAverageHeadingInstance(Collection<? extends AgentSensorProxy> targetAgents, Task.Type taskType) {
        return new TGWeightedAverageHeading(targetAgents, taskType);
    }


}
