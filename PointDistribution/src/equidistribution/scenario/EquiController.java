/*
 * EquiController.java
 * Created Aug 18, 2010
 */

package equidistribution.scenario;

import equidistribution.scenario.EquiTeam.Agent;
import equidistribution.scenario.behavior.AgentBehavior;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import simutils.Simulation;
import simutils.SimulationEvent;
import simutils.SimulationEventListener;
import simutils.SimulationMetric;

/**
 * Provides overall control of an equi-distribution scenario.
 * @author Elisha Peterson
 */
public class EquiController
        implements Simulation, SimulationEventListener {
    
    /** Handles property change events */
    private PropertyChangeSupport pcs;
    /** The scenario */
    EquiScenario scenario;

    // CONSTRUCTOR

    /** Constructs scenario with null team */
    public EquiController() {
        scenario = new EquiScenario();
        scenario.addSimulationEventListener(this);
        pcs = new PropertyChangeSupport(this);
    }

    void setScenario(EquiScenario sc) {
        adjusting = true;
        if (scenario != null)
            scenario.removeSimulationEventListener(this);
        scenario = sc;
        adjusting = false;
        scenario.addSimulationEventListener(this);
        scenario.validate();
        pcs.firePropertyChange("scenario", null, this);
    }

    // CHANGE PROPERTIES

    boolean adjusting = false;

    public boolean isAdjusting() { return adjusting; }
    /**
     * Sets flag notifying that the scenario is being adjusted.
     * The scenario will not be "recalculated" until the flag is switched to false.
     */
    public void setAdjusting(boolean value) {
        if (adjusting != value) {
            adjusting = value;
            if (adjusting)
                scenario.invalidate();
            else
                scenario.validate();
        }
    }

    // BEAN PATTERNS

//    public EquiScenario getScenario() { return scenario; }
//
//    public void setScenario(EquiScenario sc) {
//        if (scenario != sc) {
//            EquiScenario oldValue = scenario;
//            scenario = sc;
//            pcs.firePropertyChange("scenario", oldValue, scenario);
//        }
//    }

    public GlobalParameters getParameters() { return scenario.GLOBAL; }
    public void setParameters(GlobalParameters par) { }

    /** @return global region priority */
    public double getPriority() { return scenario.priority; }
    /** Sets global region priority */
    public void setPriority(double p) { scenario.priority = p; }

    /** @return copy of the current border */
    public Point2D.Double[] getBorder() { return scenario.border; }
    /** @return copy of the i'th border point */
    public Point2D.Double getBorder(int i) { return scenario.border[i]; }
    /** Sets the border to the specified list */
    public void setBorder(Point2D.Double[] b) {
        if (!adjusting) scenario.invalidate();
        scenario.border = b;
        if (!adjusting) scenario.validate();
        pcs.firePropertyChange("border", null, scenario.border);
    }
    /** Sets i'th border point */
    public void setBorder(int i, Point2D.Double b) { 
        if (!adjusting) scenario.invalidate();
        Point2D.Double old = scenario.border[i];
        scenario.border[i] = b;
        if (!adjusting) scenario.validate();
        pcs.fireIndexedPropertyChange("border", i, old, b);
    }

    /** @return zone priorities */
    public Double[] getZonePriority() { return scenario.pZones.toArray(new Double[]{}); }
    /** @return i'th zone priority */
    public Double getZonePriority(int i) { return scenario.pZones.get(i); }
    /** Sets all zone priorities */
    public void setZonePriority(Double[] pp) {
        if (!adjusting) scenario.invalidate();
        scenario.pZones.clear();
        for (Double p : pp)
            scenario.pZones.add(p);
        if (!adjusting) scenario.validate();
        pcs.firePropertyChange("zone priority", null, scenario.pZones);
    }
    /** Sets i'th zone priority */
    public void setZonePriority(int i, Double p) {
        if (!adjusting) scenario.invalidate();
        Double old = scenario.pZones.get(i);
        scenario.pZones.set(i, p);
        if (!adjusting) scenario.validate();
        pcs.fireIndexedPropertyChange("zone priority", i, old, p);
    }

    /** @return copy of the current zones */
    public Point2D.Double[][] getZones() { return scenario.zones.toArray(new Point2D.Double[][]{}); }
    /** @return copy of the i'th zone */
    public Point2D.Double[] getZones(int i) { return scenario.zones.get(i); }
    /** Sets the zones to the specified list */
    public void setZones(Point2D.Double[][] b) {
        if (!adjusting) scenario.invalidate();
        scenario.zones.clear();
        for (Point2D.Double[] z : b)
            scenario.addZone(1.0, z);
        if (!adjusting) scenario.validate();
        pcs.firePropertyChange("zones", null, scenario.zones);
    }
    /** Sets i'th zone */
    public void setZones(int i, Point2D.Double[] z) { 
        if (!adjusting) scenario.invalidate();
        Point2D.Double[] old = scenario.zones.get(i);
        scenario.zones.set(i, z);
        if (!adjusting) scenario.validate();
        pcs.fireIndexedPropertyChange("zones", i, old, z);
    }

    private static final Agent[] ARR = new Agent[]{};

    /** @return number of agents on team */
    public int getAgentNumber() { return scenario.team.size(); }
    /** Sets number of agents on team */
    public void setAgentNumber(int n) {
        int cur = getAgentNumber();
        if (cur != n) {
            if (!adjusting) scenario.invalidate();
            List<Agent> agents = scenario.team.agents;
            while (agents.size() > n)
                agents.remove(agents.size() - 1);
            if (agents.size() < n) {
                Agent last = agents.get(agents.size()-1);
                while (agents.size() < n) {
                    Agent newAgent = new Agent(last.capacity, last.behavior);
                    agents.add(newAgent);
                    newAgent.x = Math.random();
                    newAgent.y = Math.random();
                }
            }
            if (Math.abs(cur-n) != 1)
                randomizeAgentLocations();
            scenario.validate();
        }
    }

    /** @return list of agents on team */
    public Agent[] getAgent() { return scenario.team.agents().toArray(ARR); }
    /** @return i'th agent on team */
    public Agent getAgent(int i) { return scenario.team.agents().get(i); }
    /** Sets team to the specified agent list */
    public void setAgent(Agent[] arr) {
        if (!adjusting) scenario.invalidate();
        scenario.team = new EquiTeam(Arrays.asList(arr));
        if (!adjusting) scenario.validate();
    }
    /** Sets i'th member of team to be the specified agent */
    public void setAgent(int i, Agent a) {
        if (simEvent) return;
        if (!adjusting) scenario.invalidate();
        Agent old = scenario.team.agents.get(i);
        scenario.team.agents.set(i, a);
        if (!adjusting) scenario.validate();
    }

    /** @return copy of agent locations as an array */
    public Point2D.Double[] getAgentLoc() {
        Point2D.Double[] result = new Point2D.Double[scenario.team.agents.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = new Point2D.Double(scenario.team.agents.get(i).x, scenario.team.agents.get(i).y);
        return result;
    }
    /** Sets team to the specified locations */
    public void setAgentLoc(Point2D.Double[] arr) {
        if (simEvent) return;
        if (!adjusting) scenario.invalidate();
        for (int i = 0; i < arr.length; i++)
            scenario.team.agents.get(i).setLoc(arr[i]);
        if (!adjusting) scenario.validate();
    }

    /** @return i'th agent capacity */
    public double getAgentCapacity(int i) { return scenario.team.agents.get(i).capacity; }
    /** Sets i'th agent capacity */
    public void setAgentCapacity(int i, double cap) {
        if (!adjusting) scenario.invalidate();
        scenario.team.agents.get(i).capacity = cap;
        if (!adjusting) scenario.validate();
    }

    /** Sets behavior of all agents in the simulation */
    public void setBehavior(AgentBehavior b) {
        if (!adjusting) scenario.invalidate();
        for (Agent a : scenario.team.agents)
            a.behavior = b;
        if (!adjusting) scenario.validate();
    }

    // BEAN PATTERNS: DEPEND ON SCENARIO STATUS/POSITIONS

    /** @return total (weighted) area */
    public Double getTotalWeightedArea() {
        return scenario.totalWeightedArea();
    }

    /** @return total agent capacity */
    public Double getTotalAgentCapacity() {
        return scenario.team.totalCapacity();
    }

    /** @return target area */
    public Double getTargetArea() {
        return scenario.targetArea();
    }

    private static final Point2D.Double[][] ARR2 = new Point2D.Double[][]{};

    /** @return current partition */
    public Point2D.Double[][] getPartition() {
        return scenario.subRegions == null
                ? null
                : scenario.subRegions.values().toArray(ARR2);
    }

    /** @return current network */
    public Point2D.Double[][] getNetwork() {
        if (scenario.network == null)
            return null;
        ArrayList<Point2D.Double[]> edges = new ArrayList<Point2D.Double[]>();
        for (Entry<Agent, Set> en : scenario.network.entrySet())
            for (Object o : en.getValue())
                edges.add(new Point2D.Double[]{en.getKey(), (Point2D.Double) o});
        return edges.toArray(ARR2);
    }


    // ACTIONS

    public void randomizeAgentLocations() { 
        scenario.invalidate();
        scenario.randomizeAgentLocations();
        scenario.validate();
    }
                
    public void iterate() { scenario.iterate(); }

    public void run() { scenario.run(); }
    
    // UTILITIES

    /** @return current value of specified metric @ current simulation time */
    public <V> V value(SimulationMetric<V> metric) {
        return metric.value(scenario, scenario.step());
    }

//    /** Clones an array of points; no pointers are the same in the copy. */
//    private static Point2D.Double[] copy(Point2D.Double[] original) {
//        Point2D.Double[] result = new Point2D.Double[original.length];
//        for (int i = 0; i < result.length; i++)
//            result[i] = new Point2D.Double(original[i].x, original[i].y);
//        return result;
//    }

    // PropertyChangeSupport methods

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) { pcs.removePropertyChangeListener(propertyName, listener); }
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) { pcs.removePropertyChangeListener(listener); }
    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) { pcs.addPropertyChangeListener(propertyName, listener); }
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) { pcs.addPropertyChangeListener(listener); }

    //
    // SIMULATION LISTENING METHODS
    //

    boolean simEvent = false;

    protected ArrayList<SimulationEventListener> simListeners = new ArrayList<SimulationEventListener>();

    public void addSimulationEventListener(SimulationEventListener l) { simListeners.add(l); }
    public void removeSimulationEventListener(SimulationEventListener l) { simListeners.remove(l); }

    public void handleResetEvent(SimulationEvent e) {
        simEvent = true;
        for (SimulationEventListener l : simListeners) l.handleResetEvent(e);
        pcs.firePropertyChange("locations", null, getAgentLoc());
        pcs.firePropertyChange("partition", null, getPartition());
        simEvent = false;
    }

    public void handleIterationEvent(SimulationEvent e) {
        simEvent = true;
        for (SimulationEventListener l : simListeners) l.handleIterationEvent(e);
        pcs.firePropertyChange("locations", null, getAgentLoc());
        pcs.firePropertyChange("partition", null, getPartition());
        simEvent = false;
    }

    public void handleGenericEvent(SimulationEvent e) {
        simEvent = true;
        for (SimulationEventListener l : simListeners) l.handleGenericEvent(e);
        simEvent = false;
    }
}
