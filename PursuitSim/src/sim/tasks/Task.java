/**
 * Task2.java
 * Created on Jul 17, 2009
 */

package sim.tasks;

import java.awt.geom.Point2D;
import sim.agent.SimulationAgent;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *   <code>Task2</code> represents a task owned by an agent at a particular
 *   point in a simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class Task {
    private SimulationAgent owner;
    private AgentSensorProxy target;
    private double priority = 1.0;
    private Type type = Type.SEEK;

    /** Constructs the task. */
    public Task(SimulationAgent owner, AgentSensorProxy target, double priority, Type taskType) {
        this.owner = owner;
        this.target = target;
        this.priority = priority;
        this.type = taskType;
    }

    @Override
    public String toString() {
        return "Task(target="+target+", priority="+priority+", seekType="+type+")";
    }


    /** @return agent/sim component responsible for implementing or refusing this task. */
    public SimulationAgent getOwner() {
        return owner;
    }

    /** @return position of the owner */
    public Point2D.Double getOwnerPosition() {
        return owner.getSensorProxy().getPosition();
    }

    /** @return top speed of the owner */
    public double getOwnerSpeed() {
        return owner.getParameters().getTopSpeed();
    }

    /** @return target element of the task */
    public AgentSensorProxy getTarget() {
        return target;
    }

    /** @return position of the target */
    public Point2D.Double getTargetPosition() {
        return target.getPosition();
    }

    /** @return velocity of the target */
    public Point2D.Double getTargetVelocity() {
        return target.getVelocity();
    }

    /** @return priority of the task. */
    public double getPriority(){
        return priority;
    }

    /** @return true if this task is a "seeking" type task, false if it is an "evading" type task */
    public Type getTaskType() {
        return type;
    }

    public enum Type {
        NONE("None", 0),            // for a task that does nothing
        SEEK("Seek", 1),            // for a seek task
        CAPTURE("Capture", 1),      // for a capture task
        FLEE("Flee", -1);           // for a flee task
        /** Stores a name for the task. */
        private String name;
        /** Stores an int multiplier for the task, determining whether getting close is a good thing (1)
         * or a bad thing (-1). */
        private int multiplier;
        Type(String name, int multiplier) {
            this.name = name;
            this.multiplier = multiplier;
        }
        public String getName() {
            return name;
        }
        public int getMultiplier() {
            return multiplier;
        }
    }

}
