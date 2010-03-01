/**
 * Simulation.java
 * Created on Jul 14, 2009
 */
package sim;

import java.io.Serializable;
import javax.swing.event.EventListenerList;

/**
 * <p>
 *   <code>Simulation</code> is a general purpose simulator.
 * </p>
 *
 * @author Elisha Peterson
 */
public class Simulation extends SimulationComposite implements Runnable, Serializable {

    //
    // PARAMETERS
    //

    /** Name */
    String name;
    /** Amount of time between iterations. */
    double timePerStep = .1;
    /** Time to halt simulation. */
    double maxTime = 100;

    //
    // STATE VARIABLES
    //

    /** The current step of the simulation. */
    transient int curStep;
    /** Cache of information about player locations. */
    transient DistanceCache dt;

    //
    // CONSTRUCTORS
    //

    public Simulation() {
        super();
    }

    public Simulation(String name) {
        this.name = name;
    }

    public Simulation(String name, SimulationComponent... components) {
        this.name = name;
        for (SimulationComponent sc : components) {
            addComponent(sc);
        }
    }

    //
    // GETTERS & SETTERS
    //

    /** @return max running time of the simulation */
    public double getMaxTime() {
        return maxTime;
    }

    /** @param newValue new running time of the simulation */
    public void setMaxTime(double newValue) {
        if (newValue < 0) {
            throw new IllegalArgumentException("Max Time < 0: " + newValue);
        }
        if (this.maxTime != newValue) {
            this.maxTime = newValue;
        }
    }

    /** @return time per step in the simulation */
    public double getTimePerStep() {
        return timePerStep;
    }

    /** @param newValue new time between steps */
    public void setTimePerStep(double newValue) {
        if (newValue <= 0) {
            throw new IllegalArgumentException("Time per step <= 0: " + newValue);
        }
        if (this.timePerStep != newValue) {
            this.timePerStep = newValue;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    

    //
    // RUN ALGORITHMS
    //

    /** Attempts to run the simulation in a background thread. Subclasses may not override this method! */
    public final void runInBackground() {
        // TODO (later) add listening code here to check for when the run is complete!
        new Thread(this).start();
    }

    /** The template algorithm for running the simulation. Subclasses may not override this method! */
    public final void run() {
        preRun();
        try {
            while (!isFinished()) {
                iterate();
            }
        } catch (SimulationTerminatedException ex) {
            System.out.println("Victory achieved by one of the teams! ("+ex.explanation+")");
        }
        postRun();
    }

    /** Called at the beginning of a simulation. This serves as a "hook" for sub-classes. */
    public void preRun() {
        initStateVariables();
    }

    /** Decides if the simulation is over. Subclasses are encouraged to override this method.
     * @return true if the simulation is over, otherwise false. */
    public boolean isFinished() {
        return curStep * timePerStep > maxTime;
    }

    /** The iteration step for the simulation. Subclasses are strongly encouraged to leave this
     * method as is, but some implementations may require overriding. */
    public void iterate() throws SimulationTerminatedException {
        double curTime = curStep * timePerStep;
//        System.out.println(getName() + ": time=" + curTime);

        dt.recalculate(curTime);                                    // Step 0. Recalculate the table of distances
        super.handleMajorEvents(dt, curTime);                       // Step 1. Handles any major events that arise after recalculation
        super.checkVictory(dt, curTime);                            // Step 2. Checks for and handles victory elements

        fireIterationEvent(curTime);                                // Step X. Notify listeners of change in simulation state

        super.processIncomingCommEvents(curTime);                   // Step 3. Process incoming comm events
        super.gatherSensoryData(dt);                                // Step 4. Sense environment
        super.developPointOfView();                                 // Step 5. Consolidate information to form opinion about the environment
        super.generateTasks(dt);                                    // Step 6. Generate tasks based on point-of-view
        super.setControlVariables(curTime, timePerStep);            // Step 7. Initialize control variables for this step
        super.adjustState(timePerStep);                             // Step 8. Adjust state based on the control variables
        super.sendAllCommEvents(curTime);                           // Step 9. Broadcast sensory/tasking events to sub-components

        curStep++;
    }

    /** Called when the simulation terminates. Does nothing by default. This serves as a "hook" for sub-classes. */
    public void postRun() {
        System.out.println(name + ": completed");
    }

    @Override
    public void initStateVariables() {
        super.initStateVariables();
        curStep = 0;
        dt = new DistanceCache(this);
        dt.recalculate(0);
        fireSimulationEvent("Reset", dt, 0.0);
    }

    /** Notifies listeners of a change in the simulation. */
    private void fireIterationEvent(double curTime) {
        fireSimulationEvent("Iteration", dt, curTime);
    }
    /** Event handling code */
    transient protected SimulationEvent simEvent = null;
    protected EventListenerList listenerList = new EventListenerList();

    public void addSimulationEventListener(SimulationEventListener l) {
        listenerList.add(SimulationEventListener.class, l);
    }

    public void removeChangeListener(SimulationEventListener l) {
        listenerList.remove(SimulationEventListener.class, l);
    }

    public void removeAllChangeListeners() {
        listenerList = new EventListenerList();
    }

    protected void fireSimulationEvent(String message, DistanceCache dc, double curTime) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SimulationEventListener.class) {
                if (simEvent == null) {
                    simEvent = new SimulationEvent(this, message, dc, curTime);
                } else {
                    simEvent.changeTo(message, dc, curTime);
                }
                ((SimulationEventListener) listeners[i + 1]).handleSimulationEvent(simEvent);
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
