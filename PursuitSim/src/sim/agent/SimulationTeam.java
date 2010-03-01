/**
 * Team.java
 * Created on Jul 14, 2009
 */
package sim.agent;

import sim.*;
import sim.comms.*;
import sim.tasks.*;


import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *   <code>Team</code> represents a team of agents in a simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class SimulationTeam extends SimulationComposite {

    //
    // PARAMETERS
    //

    /** Default parameters used for new agents. May be null-valued. */
    TeamParameters par;

    //
    // CONSTRUCTORS
    //

    /** No-arg constructor. */
    public SimulationTeam() {
        this(new TeamParameters());
    }
    /** Default constructor. */
    public SimulationTeam(String name) {
        this(name, 0, new TeamParameters());
    }
    public SimulationTeam(TeamParameters par) {
        this.par = par;
        setNumberOfAgents(0);
    }
    /** Constructs with a default size and default parameters. */
    public SimulationTeam(String name, int size, TeamParameters parameters) {
        this.par = parameters;
        par.name = name;
        setNumberOfAgents(size);
    }

    public SimulationTeam(int size, TeamParameters parameters) {
        this.par = parameters;
        setNumberOfAgents(size);
    }

    //
    // UTILITIES
    //

    @Override
    public String toString() {
        return par.name;
    }

    //
    // GETTERS & SETTERS
    //

    public TeamParameters getParameters() {
        return par;
    }

    public void setParameters(TeamParameters par) {
        this.par = par;
    }

    public int getNumberOfAgents() {
        return components.size();
    }

    public void setNumberOfAgents(int size) {
        while (components.size() < size)
            addNewAgent();
        while (components.size() > size)
            components.remove(components.size() - 1);
    }

    public AgentParameters getAgentDefaults() {
        return par.defaults;
    }

    /** @return the agents within the team that begin as active */
    public List<SimulationAgent> getInitiallyActiveAgents() {
        ArrayList<SimulationAgent> result = new ArrayList<SimulationAgent>(getNumberOfAgents());
        for (SimulationComponent sc : components)
            if (sc instanceof SimulationAgent)
                result.add((SimulationAgent) sc);
        return result;
    }

    /** @return the active agents within the team */
    public List<SimulationAgent> getActiveAgents() {
        ArrayList<SimulationAgent> result = new ArrayList<SimulationAgent>(getNumberOfAgents());
        for (SimulationComponent sc : components) {
            // TODO (later) write method for isActive()
            if (sc instanceof SimulationAgent && ((SimulationAgent) sc).state.isActive())
                result.add((SimulationAgent) sc);
        }
        return result;
    }

    /** @return number of currently active agents in the simulation */
    public int getNumberOfActiveAgents() {
        return getActiveAgents().size();
    }


    //
    // COMPOSITIONAL HELPERS
    //

    /** Adds a new agent to the team using default parameters. */
    void addNewAgent() {
        addComponent(new SimulationAgent(par.getName() + " " + components.size(), this));
    }

    /** 
     * Sets up the starting locations for the team, using the specified points.
     * <ul>
     *  <li>If there are fewer points than players, the remaining players are set to the origin.
     *  <li>If there are more points than players, additional points are ignored.
     *  <li>If any value is null, the player is set to the origin.
     * </ul>
     *
     * @param start non-null array of points
     * @throw IllegalArgumentException if the array is null
     */
    public void setStartingLocations(Point2D.Double[] start) {
        if (start == null)
            throw new IllegalArgumentException("null value to setStartingLocations method");
        ArrayList<SimulationAgent> initAgents = new ArrayList<SimulationAgent>();
        for (SimulationComponent sc : components) {
            if (sc instanceof SimulationAgent) {
                initAgents.add((SimulationAgent) sc);
            }
        }
        for (int i = 0; i < Math.min(start.length, initAgents.size()); i++) {
            initAgents.get(i).par.initLoc = start[i] == null ? new Point2D.Double() : start[i];
        }
        for (int i = Math.min(start.length, initAgents.size()); i < initAgents.size(); i++) {
            initAgents.get(i).par.initLoc = new Point2D.Double();
        }
    }

    /** 
     * Initialize the starting locations... if the starting location generator is non-null,
     * it is used to set up the initial agent locations. Otherwise, the agents are responsible
     * for generating their own locations.
     */
    public void initStartingLocations() {
        if (par.startLoc != LocationGenerator.DELEGATE_INSTANCE) {
            int num = 0;
            for (SimulationComponent sc : components)
                if (sc instanceof SimulationAgent)
                    num++;
            setStartingLocations(par.startLoc.getLocations(num));
        }
    }

    /** 
     * Sets up an autonomous task generator for each agent within the team.
     */
    public void setAgentTaskGenerator(Tasker newTasker) {
        for (SimulationComponent sc : components) {
            if (sc instanceof SimulationAgent) {
                ((SimulationAgent) sc).par.setTasker(newTasker);
            }
        }
        // set default tasking for future agents that are created
        if (par.defaults != null)
            ((AgentParameters) par.defaults).setTasker(newTasker);
    }

    //
    // SimulationComponent METHODS
    //
    
    /** Stores state during the simulation. */
    transient TeamState state;

    @Override
    public void initStateVariables() {
        state = new TeamState();
        initStartingLocations();
                    // **TESTING**  TODO - temporarily here... should move to state variables
                    timeToBroadcast = 0.0;
        super.initStateVariables();
    }

    @Override
    public void handleMajorEvents(DistanceCache dc, double curTime) {
    }

    @Override
    public void checkVictory(DistanceCache dc, double curTime) throws SimulationTerminatedException {
        if (par.victory.hasBeenMet(dc, curTime)) {
            throw new SimulationTerminatedException(par.victory);
        }
    }

    @Override
    public void gatherSensoryData(DistanceCache dt) {
        // global sensor
        state.visibleOpponents = par.sensor.findAgents(dt, null, null);
        state.visibleOpponents.removeAll(getActiveAgents());
//        System.out.println("  " + name + ": gatherSensoryData ;" + "  Opponents=" + state.visibleOpponents.toString());
        // subcomponent sensors
        super.gatherSensoryData(dt);
    }

    @Override
    public void generateTasks(DistanceCache dt) {
        super.generateTasks(dt);
        // TODO - fix this... currently the team task generation does not work properly
        if (par.tasker != null) {
            state.tasks = par.tasker.generateGiven(dt, this, state.visibleOpponents);
        } else {
            state.tasks = Collections.emptyList();
        }
//        System.out.println("  " + name + ": tasks = " + state.tasks);
    }

    //
    // SimulationComponentComposite METHODS
    //

    // here I am testing methods for simple communications at cost
    // TODO (later) - full implementation of communicating at cost
    transient double timeToBroadcast = 0.0;
    final double timePerBroadcast = 0.2;

    @Override
    public void sendAllCommEvents(double simTime) {
        // haven't yet reached broadcast point, so do not broadcast yet
        if (timeToBroadcast > simTime)
            return;

        // otherwise, set new broadcast time
        timeToBroadcast = simTime + timePerBroadcast;
        // 0. Subcomponents generate events
        super.sendAllCommEvents(simTime);
        // 1. Broadcast position events to team
        if (state.visibleOpponents.size() > 0) {
            PositionCommEvent pce = new sim.comms.PositionCommEvent(this, simTime, simTime, state.visibleOpponents);
            CommEventReceiver cer = null;
            for (SimulationComponent sc : components) {
                if (sc instanceof CommEventReceiver) {
                    cer = (CommEventReceiver) sc;
                    cer.acceptEvent(pce);
                    //System.out.println("  " + getName() + "   sendAllCommEvents -> " + sc + " (positions=" + pce + ")");
                }
            }
        }
    // 2. (TODO - tasking) Broadcast task events
    }
}