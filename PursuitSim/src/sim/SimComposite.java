/**
 * SimulationComponentComposite.java
 * Created on Jul 17, 2009
 */

package sim;

import sim.comms.Comm;
import sim.comms.CommBroadcaster;
import sim.comms.CommReceiver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import sim.component.VisiblePlayer;
import sim.component.agent.Agent;

/**
 * <p>
 *   <code>SimulationComponentComposite</code> represents a collection of simulation components.
 *   Calls to the template method algorithms pass along the call to sub-components.
 * </p>
 *
 * @author Elisha Peterson
 */
public class SimComposite extends SimComponent implements CommBroadcaster, CommReceiver {

    /** Stores components in this piece of the simulation. */
    protected List<SimComponent> components;

    /** Initializes the component list. */
    public SimComposite() {
        components = new ArrayList<SimComponent>();
    }

    //
    // BEAN PATTERNS
    //

    public SimComponent[] getComponent() {
        return components.toArray(new SimComponent[]{});
    }

    public SimComponent getComponent(int idx) {
        return components.get(idx);
    }

    public void setComponent(SimComponent[] components) {
        this.components = Arrays.asList(components);
    }

    public void setComponent(int idx, SimComponent sc) {
        this.components.set(idx, sc);
    }

    //
    // COMPOSITIONAL
    //

    /** @return list of all elements of a given type in the composite or components */
    public <T> ArrayList<T> getComponentsByType(Class<? extends T> cls) {
        ArrayList<T> result = new ArrayList<T>();
        for (SimComponent sc : components) {
            if (cls.isAssignableFrom(sc.getClass()))
                result.add((T) sc);
            if (sc instanceof SimComposite)
                result.addAll(((SimComposite)sc).getComponentsByType(cls));
        }
        return result;
    }

    /** @return list of all players in this composite, or in any subcomponents */
    public ArrayList<VisiblePlayer> getAllPlayers() {
        return getComponentsByType(VisiblePlayer.class);
    }

    /** @return list of all players in this composite, or in any subcomponents */
    public ArrayList<Agent> getAllAgents() {
        return getComponentsByType(Agent.class);
    }

    //
    // components DELEGATION METHODS
    //

    public boolean containsComponent(SimComponent elem) {
        return components.contains(elem);
    }

    public void clearComponents() {
        for (SimComponent sc : components)
            sc.parent = null;
        components.clear();
    }

    public boolean addComponents(Collection<? extends SimComponent> c) {
        for (SimComponent sc : c)
            sc.parent = this;
        return components.addAll(c);
    }

    public boolean addComponent(SimComponent o) {
        o.parent = this;
        return components.add(o);
    }

    public boolean removeComponent(SimComponent o) {
        o.parent = null;
        return components.remove(o);
    }

    //
    // SimulationComponent METHODS
    //

    public void initStateVariables() {
        for (SimComponent sc : components)
            sc.initStateVariables();
    }

    @Override
    public void gatherSensoryData(DistanceCache dt) {
        for (SimComponent sc : components)
            sc.gatherSensoryData(dt);
    }

    @Override
    public void developPointOfView() {
        for (SimComponent sc : components)
            sc.developPointOfView();
    }

    @Override
    public void generateTasks(DistanceCache dt) {
        for (SimComponent sc : components)
            sc.generateTasks(dt);
    }

    public void setControlVariables(double simTime, double timePerStep) {
        for (SimComponent sc : components)
            sc.setControlVariables(simTime, timePerStep);
    }

    public void adjustState(double timePerStep) {
        for (SimComponent sc : components)
            sc.adjustState(timePerStep);
    }

    @Override
    public void handleMajorEvents(DistanceCache dc, double curTime) {
        for (SimComponent sc : components)
            sc.handleMajorEvents(dc, curTime);
    }

    @Override
    public void checkVictory(DistanceCache dc, double curTime) throws SimulationTerminatedException {
        for (SimComponent sc : components)
            sc.checkVictory(dc, curTime);
    }

    public void sendAllCommEvents(double simTime) {
        for (SimComponent sc : components)
            if (sc instanceof CommBroadcaster)
                ((CommBroadcaster) sc).sendAllCommEvents(simTime);
    }

    /** Defaults to do nothing. */
    public void acceptEvent(Comm evt) {
    }

    public void processIncomingCommEvents(double simTime) {
        for (SimComponent sc : components)
            if (sc instanceof CommReceiver)
                ((CommReceiver) sc).processIncomingCommEvents(simTime);
    }
}
