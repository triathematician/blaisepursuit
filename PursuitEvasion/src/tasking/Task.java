/*
 * Task.java 
 * Created on Aug 28, 2007, 10:26:52 AM
 */

// TODO Add originator of the task
// TODO Add time scale of the task
package tasking;

import scio.coordinate.R2;
import scio.coordinate.V2;

/**
 * <p>
 * Contains a task, whose object is some target agent. The target be a stationary goal, a moving
 * player, or some other kind of point (e.g. center of mass of a team). The task also has
 * a priority level (between 0 and 1), and a boolean describing whether the player should
 * seek or flee the target. End behaviors will usually arise based on a combination of different
 * tasks with different priority levels. Tasks will be assigned by either a single player, a team
 * control element, or by communication between players. These are done using the Autonomous class,
 * the Control class, and the Cooperation task, respectively.
 * </p>
 * <p>
 * Eventually, this class will also implement <i>cooperative tasks</i>. As an example, two players
 * might want to work together to surround a second player. As there is still a single target player,
 * however, this can currently be implemented with identical tasks between the players and an agreement
 * to carry out a "Surround" behavior.
 * </p>
 * @author Elisha Peterson
 */
public class Task {

    // PROPERTIES
    /** The originator of the task. */
    TaskGenerator source;
    /** The targeting point of the task. */
    V2 target;
    /** Type of goal... seek, flee, or capture. */
    int taskType;
    /** The priority weight of the task. */
    double weight;
    /** The metric threshold of the task. */
    double threshold;
    /** The exponent of the task, giving the relative priority. */
    double exponent;


    // CONSTRUCTORS    
    /**
     * Constructs given several settings. Threshold defaults to 0 and the exponent to 1.
     * @param target the object of the task
     * @param type 0 if trivial, 1 if pursue, 2 if evade
     * @param priority priority of the task, between 0 and 1
     */
    public Task(TaskGenerator source, V2 target, int type, double weight) {
        this(source, target, type, 0.0, 1.0, weight);
    }

    /** 
     * Constructs given several settings.
     * @param target the object of the task
     * @param type 0 if trivial, 1 if pursue, 2 if evade
     * @param threshold the cutoff of the task
     * @param exponent the exponent of the task (determines weighting based on distance from threshold)
     * @param priority priority of the task, between 0 and 1
     */
    public Task(TaskGenerator source, V2 target, int type, double threshold, double exponent, double weight) {
        this.source = source;
        this.target = target;
        this.taskType = type;
        this.threshold = threshold;
        this.exponent = exponent;
        setWeight(weight);
    }

    // META-BEANS

    /** Returns the equivalent "utility" of the task, from current location, given the current configuration.
     * The function is \pm priority * (||target-loc||-threshold)^exponent
     * @param loc location of the agent applying the task
     * @return value of the utility
     */
    public double getUtility(R2 loc) {
        double ddist = loc.distanceTo(target) - threshold;
        return (ddist <= 0) ? 0.0 :
            (taskType == Tasking.FLEE ? -1.0 : 1.0) * weight * Math.pow(ddist, exponent);
    }

    /** Returns the gradient based on this utility.
     * The function is \pm priority * exponent * (||target-loc||-threshold)^(exponent-1) * (target-loc)/||target-loc||
     * @param loc location of the agent applying the task
     * @return vector in direction of increasing utility
     */
    public R2 getGradient(R2 loc) {
        double ddist = loc.distanceTo(target) - threshold;
        return (ddist <= 0) ? new R2() :
            target.minus(loc).multipliedBy(
                (taskType == Tasking.FLEE ? -1.0 : 1.0) * weight * exponent * Math.pow(ddist, exponent-1) / loc.distanceTo(target)
            );
    }

    // BEAN PATTERNS: GETTERS & SETTERS

    /** Returns generator of the task. */
    public TaskGenerator getGenerator() {
        return source;
    }

    /** Returns the type of the task: seek, flee, or capture. */
    public int getTaskType() {
        return taskType;
    }

    /** Returns exponent. */
    public double getExponent() {
        return exponent;
    }

    /** Returns threshold. */
    public double getThreshold() {
        return threshold;
    }

    /** Returns target
     * @return object of the task */
    public V2 getTarget() {
        return target;
    }

    /** Sets target
     * @param target the new target */
    public void setTarget(V2 target) {
        this.target = target;
    }

    /** Returns priority of the target
     * @return priority as a double between 0 and 1 */
    public double getWeight() {
        return weight;
    }

    /** Sets priority. Makes sure value is between 0 and 1.
     * @param priority a double between 0 and 1. */
    public void setWeight(double weight) {
        this.weight = (weight < 0) ? 0 : (weight > 1) ? 1 : weight;
    }

    // OTHER METHODS
    /** Override string method. */
    @Override
    public String toString() {
        return "Task target=" + target + ", type=" + taskType 
                + ", priority=" + weight + ", exponent=" + exponent + ", threshold=" + threshold
                + " (orginated by " + source + ")";
    }
}
