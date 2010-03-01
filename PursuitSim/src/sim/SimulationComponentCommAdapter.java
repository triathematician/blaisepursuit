/**
 * SimulationComponentAdapter.java
 * Created on Jul 17, 2009
 */
package sim;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.TreeSet;
import sim.comms.CommEvent;
import sim.comms.CommEventBroadcaster;
import sim.comms.CommEventReceiver;
import sim.comms.PositionCommEvent;
import sim.comms.TaskCommEvent;

/**
 * <p>
 *   <code>SimulationComponentAdapter</code> is a default implementation of <code>SimulationComponent</code>
 *   whose methods are entirely empty or return blanks.
 * </p>
 *
 * @author Elisha Peterson
 */
public class SimulationComponentCommAdapter extends SimulationComponent implements CommEventBroadcaster, CommEventReceiver {

    public SimulationComponentCommAdapter() {
        availablePosEvents = new ArrayList<PositionCommEvent>();
        availableTaskEvents = new ArrayList<TaskCommEvent>();
        inEvents = new TreeSet<CommEvent>();
    }

    /** Default behavior does nothing. */
    public void initStateVariables() {
        inEvents = new TreeSet<CommEvent>();
        availablePosEvents = new ArrayList<PositionCommEvent>();
        availableTaskEvents = new ArrayList<TaskCommEvent>();
    }

    /** Default behavior does nothing. */
    public void gatherSensoryData(DistanceCache dt) {
    }

    /** Default behavior does nothing. */
    public void developPointOfView() {
    }

    /** Default behavior does nothing. */
    public void generateTasks(DistanceCache dt) {
    }

    /** Default behavior does nothing. */
    public void setControlVariables(double simTime, double timePerStep) {
    }

    /** Default behavior does nothing. */
    public void adjustState(double timePerStep) {
    }

    /** Default behavior does nothing. */
    public void handleMajorEvents(DistanceCache dc, double curTime) {
    }

    /** Default behavior does nothing. */
    public void checkVictory(DistanceCache dc, double curTime) throws SimulationTerminatedException {
    }

    /** Default behavior does nothing. */
    public void sendAllCommEvents(double simTime) {
    }

    //
    // CommEventReceiver METHODS
    //
    
    protected ArrayList<PositionCommEvent> availablePosEvents;
    protected ArrayList<TaskCommEvent> availableTaskEvents;
    protected TreeSet<CommEvent> inEvents;

    /** Adds incoming event to the queue. */
    public void acceptEvent(CommEvent evt) {
        inEvents.add(evt);
    }

    /** Default behavior does nothing. */
    public void processIncomingCommEvents(double curTime) {
        availablePosEvents.clear();
        availableTaskEvents.clear();
        for (CommEvent nextEvent : inEvents) {
            if (nextEvent.getTimeAvailableToReceiver() > curTime) {
                continue;
            }
            if (nextEvent instanceof PositionCommEvent) {
                availablePosEvents.add((PositionCommEvent) nextEvent);
            } else if (nextEvent instanceof TaskCommEvent) {
                availableTaskEvents.add((TaskCommEvent) nextEvent);
            }
        }
        inEvents.removeAll(availablePosEvents);
        inEvents.removeAll(availableTaskEvents);
        processAvailablePosEvents();
        processAvailableTaskEvents();
    }

    /** Default behavior does nothing. */
    public void processAvailablePosEvents() {
    }

    /** Default behavior does nothing. */
    public void processAvailableTaskEvents() {
    }
}
