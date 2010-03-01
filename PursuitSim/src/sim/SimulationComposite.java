/**
 * SimulationComponentComposite.java
 * Created on Jul 17, 2009
 */

package sim;

import java.awt.geom.Point2D;
import sim.comms.CommEvent;
import sim.comms.CommEventBroadcaster;
import sim.comms.CommEventReceiver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *   <code>SimulationComponentComposite</code> represents a collection of simulation components.
 *   Calls to the template method algorithms pass along the call to sub-components.
 * </p>
 *
 * @author Elisha Peterson
 */
public class SimulationComposite extends SimulationComponent implements CommEventBroadcaster, CommEventReceiver {

    /** Stores components in this piece of the simulation. */
    protected List<SimulationComponent> components;

    /** Initializes the component list. */
    public SimulationComposite() {
        components = new ArrayList<SimulationComponent>();
    }

    //
    // BEAN PATTERNS
    //

    public SimulationComponent[] getComponent() {
        return components.toArray(new SimulationComponent[]{});
    }

    public SimulationComponent getComponent(int idx) {
        return components.get(idx);
    }

    public void setComponent(SimulationComponent[] components) {
        this.components = Arrays.asList(components);
    }

    public void setComponent(int idx, SimulationComponent sc) {
        this.components.set(idx, sc);
    }

    //
    // COMPOSITIONAL
    //

    /** @return list of all agents in this composite, or in any subcomponents */
    public List<AgentSensorProxy> getAllAgents() {
        List<AgentSensorProxy> agents = new ArrayList<AgentSensorProxy>();
        for (SimulationComponent sc : components) {
            if (sc instanceof SimulationComposite) {
                agents.addAll(((SimulationComposite)sc).getAllAgents());
            } else if (sc instanceof AgentSensorProxy) {
                agents.add((AgentSensorProxy) sc);
            }
        }
        return agents;
    }

    //
    // components DELEGATION METHODS
    //

    public boolean containsComponent(SimulationComponent elem) {
        return components.contains(elem);
    }

    public void clearComponents() {
        components.clear();
    }

    public boolean addComponents(Collection<? extends SimulationComponent> c) {
        return components.addAll(c);
    }

    public boolean addComponent(SimulationComponent o) {
        return components.add(o);
    }

    //
    // SimulationComponent METHODS
    //
    
    public Point2D.Double getPosition() {
        return null;
    }

    public void initStateVariables() {
        for (SimulationComponent sc : components) {
            sc.initStateVariables();
        }
    }

    @Override
    public void gatherSensoryData(DistanceCache dt) {
        for (SimulationComponent sc : components) {
            sc.gatherSensoryData(dt);
        }
    }

    @Override
    public void developPointOfView() {
        for (SimulationComponent sc : components) {
            sc.developPointOfView();
        }
    }

    @Override
    public void generateTasks(DistanceCache dt) {
        for (SimulationComponent sc : components) {
            sc.generateTasks(dt);
        }
    }

    public void setControlVariables(double simTime, double timePerStep) {
        for (SimulationComponent sc : components) {
            sc.setControlVariables(simTime, timePerStep);
        }
    }

    public void adjustState(double timePerStep) {
        for (SimulationComponent sc : components) {
            sc.adjustState(timePerStep);
        }
    }

    @Override
    public void handleMajorEvents(DistanceCache dc, double curTime) {
        for (SimulationComponent sc : components) {
            sc.handleMajorEvents(dc, curTime);
        }
    }

    @Override
    public void checkVictory(DistanceCache dc, double curTime) throws SimulationTerminatedException {
        for (SimulationComponent sc : components) {
            sc.checkVictory(dc, curTime);
        }
    }

    public void sendAllCommEvents(double simTime) {
        for (SimulationComponent sc : components) {
            if (sc instanceof CommEventBroadcaster) {
                ((CommEventBroadcaster) sc).sendAllCommEvents(simTime);
            }
        }
    }

    /** Defaults to do nothing. */
    public void acceptEvent(CommEvent evt) {
    }

    public void processIncomingCommEvents(double simTime) {
        for (SimulationComponent sc : components) {
            if (sc instanceof CommEventReceiver) {
                ((CommEventReceiver) sc).processIncomingCommEvents(simTime);
            }
        }
    }
}
