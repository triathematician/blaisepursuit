/**
 * Simulation.java
 * Created on Jul 14, 2009
 */
package sim;

import gsim.logger.SimulationLogger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * <p>
 *   <code>Simulation</code> is a general purpose simulator.
 * </p>
 *
 * @author Elisha Peterson
 */
public class Simulation extends SimComposite implements Runnable, Serializable {

    /** Global variable to store the "active" simulation. */
    public static Simulation ACTIVE_SIM = null;

    //
    // PARAMETERS
    //

    /** Name */
    String name = "Simulation";
    /** Amount of time between iterations. */
    double timePerStep = .1;
    /** Time to halt simulation. */
    double maxTime = 30;

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
    }

    public Simulation(String name) {
        this.name = name;
    }

    public Simulation(String name, SimComponent... components) {
        this.name = name;
        for (SimComponent sc : components)
            addComponent(sc);
    }

    @Override
    public String toString() {
        return name;
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
        if (newValue < 0)
            throw new IllegalArgumentException("Max Time < 0: " + newValue);

        if (this.maxTime != newValue) {
            this.maxTime = newValue;
            fireStateChanged();
        }
    }

    /** @return time per step in the simulation */
    public double getTimePerStep() {
        return timePerStep;
    }

    /** @param newValue new time between steps */
    public void setTimePerStep(double newValue) {
        if (newValue <= 0)
            throw new IllegalArgumentException("Time per step <= 0: " + newValue);
        
        if (this.timePerStep != newValue) {
            this.timePerStep = newValue;
            fireStateChanged();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if ( ! this.name.equals(name)) {
            this.name = name;
            fireStateChanged();
        }
    }

    //
    // COMPOSITIONAL
    //

    @Override
    public boolean addComponent(SimComponent o) {
        if (super.addComponent(o)) {
            fireStateChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean addComponents(Collection<? extends SimComponent> c) {
        if (super.addComponents(c)) {
            fireStateChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeComponent(SimComponent o) {
        if (super.removeComponent(o)) {
            fireStateChanged();
            return true;
        }
        return false;
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
            while (!isFinished())
                iterate();
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

        fireSimIterateEvent(dt, curTime);                           // Step X. Notify listeners of change in simulation state

        super.processIncomingCommEvents(curTime);                   // Step 3. Process incoming comm events
        super.gatherSensoryData(dt);                                // Step 4. Sense environment
        super.developPointOfView();                                 // Step 5. Consolidate information to form opinion about the environment
        super.generateTasks(dt);                                    // Step 6. Generate tasks based on point-of-view
        super.sendAllCommEvents(curTime);                           // Step 7. Broadcast sensory/tasking events to sub-components

        super.setControlVariables(curTime, timePerStep);            // Step 8. Initialize control variables for this step
        super.adjustState(timePerStep);                             // Step 9. Adjust state based on the control variables

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
        fireSimResetEvent(dt, 0.0);
    }
    
    //
    // SIMULATION EVENT HANDLING CODE
    //

    /** Event handling code */
    transient protected SimulationEvent simResetEvent = new SimulationEvent(this, "Reset", null, 0);
    transient protected SimulationEvent simIterateEvent = new SimulationEvent(this, "Iterate", null, 0);
    protected ArrayList<SimulationEventListener> simListeners = new ArrayList<SimulationEventListener>();

    public void addSimulationEventListener(SimulationEventListener l) { simListeners.add(l); }
    public void removeSimulationEventListener(SimulationEventListener l) { simListeners.remove(l); }
    public void removeAllSimulationEventListeners() { simListeners.clear(); }

    protected void fireSimResetEvent(DistanceCache dc, double curTime) {
        simResetEvent.update(dc, curTime);
        simResetEvent = new SimulationEvent(this, "Reset", dc, curTime);
        for (SimulationEventListener sel : simListeners)
            sel.handleResetEvent(simResetEvent);
    }

    protected void fireSimIterateEvent(DistanceCache dc, double curTime) {
        simIterateEvent.update(dc, curTime);
        simIterateEvent = new SimulationEvent(this, "Iterate", dc, curTime);
        for (SimulationEventListener sel : simListeners)
            sel.handleIterationEvent(simIterateEvent);
    }

    
    //
    // EVENT HANDLING
    //

    transient protected ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    public void removeAllChangeListeners() {
        for (ChangeListener cl : listenerList.getListeners(ChangeListener.class))
            listenerList.remove(ChangeListener.class, cl);
    }

    public void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
    }
}
