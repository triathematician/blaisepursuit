/**
 * Task2.java
 * Created on Jul 17, 2009
 */

package sim.tasks;

import java.awt.geom.Point2D;
import sim.component.agent.Agent;
import sim.component.VisiblePlayer;

/**
 * <p>
 *   <code>Task2</code> represents a task owned by an agent at a particular
 *   point in a simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class Task {
    public Agent owner;
    public Point2D.Double ownerLoc;
    public VisiblePlayer target;
    public Point2D.Double targetLoc;
    public double priority = 1.0;
    public Type type = Type.SEEK;

    /** Constructs the task. */
    public Task(Agent owner, VisiblePlayer target, double priority, Type taskType) {
        this.owner = owner;
        this.ownerLoc = owner.state.position;
        this.target = target;
        this.targetLoc = target.getPosition();
        this.priority = priority;
        this.type = taskType;
        if (ownerLoc.x == targetLoc.x && ownerLoc.y == targetLoc.y)
            throw new IllegalArgumentException("Tasks should not be created with targets the same as owners!");
    }

    @Override
    public String toString() {
        return "Task."+type+"["+owner+String.format("@(%.2f,%.2f)", owner.getPosition().x, owner.getPosition().y)
                + " -> "+target+String.format("@(%.2f,%.2f)", target.getPosition().x, target.getPosition().y)
                + ", priority="+priority+"]";
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
        public int multiplier;
        Type(String name, int multiplier) { this.name = name; this.multiplier = multiplier; }
        @Override public String toString() { return name; }
    }

}
