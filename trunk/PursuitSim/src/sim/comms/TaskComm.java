/**
 * TaskCommEvent.java
 * Created on Jul 20, 2009
 */

package sim.comms;

import java.util.Collection;
import sim.SimComponent;
import sim.tasks.Task;

/**
 * <p>
 *   <code>TaskCommEvent</code> ...
 * </p>
 *
 * @author Elisha Peterson
 */
public class TaskComm extends Comm {
    private Collection<Task> tasks;

    public TaskComm(SimComponent source, double timeOfCreation, double timeAvailable, Collection<Task> tasks) {
        super(source, timeOfCreation, timeAvailable);
        this.tasks = tasks;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }
}
