/**
 * Team.java
 * Created on Jul 14, 2009
 */
package sim.component.team;

import java.awt.Color;
import sim.*;
import sim.comms.*;
import sim.tasks.*;


import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import sim.component.InitialPositionSetter;
import sim.component.agent.AgentParameters;
import sim.component.VisiblePlayer;
import sim.component.agent.Agent;
import sim.metrics.Capture;

/**
 * <p>
 *   <code>Team</code> represents a team of agents in a simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class Team extends SimComposite {

    /** Default parameters used for new agents. May be null-valued. */
    public TeamParameters par;

    /** Stores state during the simulation. */
    public transient TeamState state;

    /** Stores the number of agents in this component (and sub-components) */
    public transient int numAgents;

    //
    // CONSTRUCTORS
    //

    /** No-arg constructor. */
    public Team() {
        this(new TeamParameters());
    }
    /** Default constructor. */
    public Team(String name) {
        this(0, new TeamParameters());
    }
    public Team(TeamParameters parameters) {
        this(0, parameters);
    }
    public Team(String name, Color color, AgentParameters par, int size) {
        this(size, new TeamParameters(name, color, par));
    }

    /** Constructs with a default size and default parameters. */
    public Team(int size, TeamParameters parameters) {
        this.par = parameters;
        for (int i = 0; i < size; i++)
            addNewAgent();
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

    public AgentParameters getAgentDefaults() {
        return par.defaults;
    }

    public int getNumberOfAgents() {
        return numAgents;
    }

    //
    // STATE VARIABLES
    //

    /** @return the active agents within the team */
    public List<VisiblePlayer> getActivePlayers() {
        return state.activePlayers;
    }

    /** @return number of currently active agents in the simulation */
    public int getNumberOfActiveAgents() {
        return state.activeAgents.size();
    }

    /** @return the # of currently "safe" agents within the team */
    public int getNumberOfSafeAgents() {
        return state.safeAgents.size();
    }

    /** @return the # of opponents on given team that have been captured by this team */
    public int getNumberOfCaptures(Team team2) {
        int total = 0;
        for (Set<VisiblePlayer> set : state.captures.values())
            for(VisiblePlayer vp : set)
                if (team2.containsComponent((SimComponent) vp))
                    total++;
        return total;
    }


    //
    // COMPOSITIONAL HELPERS
    //

    /** Adds a new agent to the team using default parameters. */
    void addNewAgent() {
        addComponent(new Agent(par.name + " " + components.size(), this));
    }

    /** Sets up explicit starting locations. */
    public void setStartingLocations(Point2D.Double[] start) {
        par.startLoc = LocationGenerator.DELEGATE_INSTANCE;
        List<InitialPositionSetter> initials = this.getComponentsByType(InitialPositionSetter.class);
        for (int i = 0; i < start.length; i++)
            initials.get(i).setInitialPosition(start[i]);
    }

    /** 
     * Initialize the starting locations... if the starting location generator is non-null,
     * it is used to set up the initial agent locations. Otherwise, the agents are responsible
     * for generating their own locations.
     */
    public void initStartingLocations() {
        if (par.startLoc != LocationGenerator.DELEGATE_INSTANCE) {
            List<InitialPositionSetter> initials = this.getComponentsByType(InitialPositionSetter.class);
            Point2D.Double[] start = par.startLoc.getLocations(initials.size());
            for (int i = 0; i < start.length; i++)
                initials.get(i).setInitialPosition(start[i]);
        }
    }

    /** 
     * Sets up an autonomous task generator for each agent within the team.
     */
    public void addAgentTasker(Tasker newTasker) {
        for (SimComponent sc : components)
            if (sc instanceof Agent)
                ((Agent) sc).par.tasker(newTasker);
        // set default tasking for future agents that are created
        if (par.defaults != null)
            par.defaults.tasker(newTasker);
    }

    //
    // SimulationComponent METHODS
    //

    @Override
    public void initStateVariables() {
        if (state == null)
            state = new TeamState();
        numAgents = getAllAgents().size();
        state.initialize(this);
        initStartingLocations();
                    // **TESTING**  TODO - temporarily here... should move to state variables
                    timeOfNextBroadcast = 0.0;
        super.initStateVariables();
    }

    @Override
    public void handleMajorEvents(DistanceCache dc, double curTime) {
        if (par.capture == null || par.capture.length == 0)
            return;
        for(Capture cc : par.capture) {
            for (VisiblePlayer[] cap : cc.findCaptures(dc, curTime)) {
                if (cc.getCaptureAction() == Capture.Action.DEACTIVATE_OWNER
                        || cc.getCaptureAction() == Capture.Action.DEACTIVATE_BOTH)
                    ((Agent) cap[0]).deactivate();
                if (cc.getCaptureAction() == Capture.Action.DEACTIVATE_TARGET
                        || cc.getCaptureAction() == Capture.Action.DEACTIVATE_BOTH)
                    ((Agent) cap[1]).deactivate();
                    state.addCapture((Agent) cap[0], cap[1]);
                if (cc.getCaptureAction() == Capture.Action.SAFETY_OWNER)
                    ((Agent) cap[0]).safety();
            }
        }
    }

    @Override
    public void checkVictory(DistanceCache dc, double curTime) throws SimulationTerminatedException {
        if (par.victory.hasBeenMet(dc, curTime)) {
            throw new SimulationTerminatedException(par.victory);
        }
    }

    @Override
    public void gatherSensoryData(DistanceCache dt) {
        par.sensor.findAgents(dt, null, state.visibleOpponents);
        super.gatherSensoryData(dt);
    }

    @Override
    public void generateTasks(DistanceCache dt) {
        if (par.taskers == null || par.taskers.length == 0)
            state.tasks = Collections.emptyList();
        else if (par.taskers.length == 1)
            state.tasks = par.taskers[0].generateGiven(dt, this, state.visibleOpponents);
        else {
            state.tasks = new ArrayList<Task>();
            for(Tasker t : par.taskers)
                state.tasks.addAll(t.generateGiven(dt, this, state.visibleOpponents));
        }
//        System.out.println("  " + par.name + ": tasks = " + state.tasks);
        super.generateTasks(dt);
    }

    //
    // SimulationComponentComposite METHODS
    //

    // here I am testing methods for simple communications at cost
    // TODO (later) - full implementation of communicating at cost
    transient double timeOfNextBroadcast = 0.0;
    final double timePerBroadcast = 0.0;

    @Override
    public void sendAllCommEvents(double simTime) {
        if (timeOfNextBroadcast > simTime)
            return;
        else
            timeOfNextBroadcast = simTime + timePerBroadcast;

        // Broadcast position events to team
        if (state.visibleOpponents.size() > 0) {
            SensorComm pce = new SensorComm(this, simTime, simTime, state.visibleOpponents);
            for (SimComponent sc : components)
                if (sc instanceof CommReceiver)
                    ((CommReceiver) sc).acceptEvent(pce);
        }

        // Broadcast task events to team
        if (state.tasks.size() > 0) {
            TaskComm tce = new TaskComm(this, simTime, simTime, state.tasks);
            for (SimComponent sc : components)
                if (sc instanceof Agent)
                    ((Agent) sc).acceptEvent(tce);
        }

        super.sendAllCommEvents(simTime);
    }
}