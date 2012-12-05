/**
 * TaskPrioritizer.java
 * Created on Jul 17, 2009
 */

package sim.tasks;

import java.awt.geom.Point2D;
import java.util.List;
import scio.coordinate.utils.PlanarMathUtils;
import sim.component.agent.Agent;
import sim.component.PhantomAgent;

/**
 * <p>
 *   <code>TaskPrioritizer</code> takes in several tasks and returns a single task
 *   that represents either a hybrid or a selection of a top task.
 * </p>
 *
 * @author Elisha Peterson
 */
public abstract class TaskChooser {

    /** Returns the top-priority task, or creates a hybrid task.
     * @param tasks a list of tasks.
     * @return one of the input tasks, or a hybrid task
     */
    abstract public Task chooseTask(List<? extends Task> tasks);



    //============================================//
    //                                            //
    //               FACTORY METHODS              //
    //                                            //
    //============================================//

    public enum ChooserEnum { MAXIMUM, GRADIENT }

    /** @return instance of a TaskPrioritizer of the given type */
    public static TaskChooser getInstance(ChooserEnum value) {
        switch (value) {
            case MAXIMUM: return MAX_CHOOSER;
            case GRADIENT: return GRADIENT_CHOOSER;
        }
        return null;
    }

    //============================================//
    //                                            //
    //               INNER CLASSES                //
    //                                            //
    //============================================//


    /** A prioritizer that simply chooses the highest priority task. */
    public static final TaskChooser MAX_CHOOSER = new TaskChooser() {
            public Task chooseTask(List<? extends Task> tasks) {
                if (tasks.size() == 0)
                    return null;
                else if (tasks.size() == 1)
                    return tasks.get(0);

                Task highest = null;
                for (Task t : tasks)
                    if (highest == null || t.priority > highest.priority)
                        highest = t;
                return highest;
            }
            @Override
            public String toString() { return "Task Chooser - Maximum"; }
        };


    /** 
     * A prioritizer that uses a gradient maximization.
     */
    public static final TaskChooser GRADIENT_CHOOSER = new TaskChooser() {
        public Task chooseTask(List<? extends Task> tasks) {
            if (tasks.size() == 0)
                return null;
            else if (tasks.size() == 1)
                return tasks.get(0);

            // minimize sum of distances
            int POWER = -1;
            Point2D.Double loc = null;
            Agent owner = null;
            Point2D.Double dir = new Point2D.Double();
            for (Task t : tasks) {
                if (loc == null) {
                    loc = t.owner.state.position;
                    owner = t.owner;
                }
                double multiplier = t.type.multiplier * t.priority;
                if ( ! (t.target instanceof PhantomAgent) )
                    multiplier *= Math.pow(loc.distance(t.targetLoc), POWER - 1);
                dir.x += (t.targetLoc.x - loc.x) * multiplier;
                dir.y += (t.targetLoc.y - loc.y) * multiplier;
            }
            Point2D.Double tLoc = new Point2D.Double(loc.x + dir.x, loc.y + dir.y);

//            System.out.println("Gradient Chooser, tasks = " + tasks
//                    + "\n  phantom task = " + tLoc);

            return new Task(owner, new PhantomAgent(tLoc, PlanarMathUtils.ZERO), 1.0, Task.Type.SEEK);
        }
        
        @Override
        public String toString() { return "Task Chooser - Gradient"; }
    };
}
