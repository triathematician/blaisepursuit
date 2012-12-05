/**
 * SimulationComponentAdapter.java
 * Created on Jul 17, 2009
 */
package sim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import sim.comms.Comm;
import sim.comms.CommBroadcaster;
import sim.comms.CommReceiver;
import sim.comms.SensorComm;
import sim.comms.TaskComm;

/**
 * <p>
 *   <code>SimulationComponentAdapter</code> is a default implementation of <code>SimulationComponent</code>
 *   whose methods are entirely empty or return blanks.
 * </p>
 *
 * @author Elisha Peterson
 */
public abstract class SimCommunicator extends SimComponent implements CommBroadcaster, CommReceiver {

    public SimCommunicator() {
        inComms = new HashSet<Comm>();
        sensorComms = new ArrayList<SensorComm>();
        taskComms = new ArrayList<TaskComm>();
    }

    /** Default behavior does nothing. */
    public void initStateVariables() {
        inComms = new HashSet<Comm>();
        sensorComms = new ArrayList<SensorComm>();
        taskComms = new ArrayList<TaskComm>();
    }

    //
    // CommEventReceiver METHODS
    //

    /** Events are stored here before they are processed, which may not be for a set amount of simulation time. */
    protected HashSet<Comm> inComms;

    /** This stores the incoming position events that are ready to be processed. */
    protected ArrayList<SensorComm> sensorComms;
    /** This stores the incoming task events that are ready to be processed. */
    protected ArrayList<TaskComm> taskComms;

    /** Adds incoming event to the queue. */
    public void acceptEvent(Comm evt) {
        inComms.add(evt);
    }

    /** Default behavior does nothing. */
    public void processIncomingCommEvents(double curTime) {
        for (Comm nextEvent : inComms) {
            if (nextEvent.getTimeAvailableToReceiver() > curTime)
                continue;
            if (nextEvent instanceof SensorComm)
                sensorComms.add((SensorComm) nextEvent);
            else if (nextEvent instanceof TaskComm)
                taskComms.add((TaskComm) nextEvent);
        }
        inComms.removeAll(sensorComms);
        inComms.removeAll(taskComms);
        processAvailablePosEvents();
        processAvailableTaskEvents();
    }

    /** Look through the available sensor events and handle them. */
    abstract public void processAvailablePosEvents();

    /** Look through the available task events and handle them. */
    abstract public void processAvailableTaskEvents();
}
