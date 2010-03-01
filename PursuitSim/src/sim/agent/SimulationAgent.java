/**
 * Agent.java
 * Created on Jul 14, 2009
 */
package sim.agent;

import sim.*;
import sim.comms.*;
import sim.tasks.*;
import java.awt.geom.Point2D;
import java.util.Collections;
import scio.coordinate.utils.PlanarMathUtils;

/**
 * <p>
 *   <code>Agent</code> represents an agent in a simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class SimulationAgent extends SimulationComponentCommAdapter 
        implements AgentSensorProxy, InitialPositionSetter {

    //
    // PARAMETERS
    //

    /** The agent's team */
    transient SimulationTeam team;
    /** Agent's parameters */
    AgentParameters par;
    /** Stores state during the simulation. */
    transient AgentState state = null;

    //
    // CONSTRUCTORS
    //

    /** Constructs with no name */
    public SimulationAgent() {
        par = new AgentParameters();
    }
    
    /** Constructs without a team (defaults to null). */
    public SimulationAgent(String name) {
        par = new AgentParameters(name);
    }

    public SimulationAgent(AgentParameters parameters) {
        this.par = parameters;
    }

    /** Constructs with specified parameters. */
    public SimulationAgent(String name, AgentParameters parameters) {
        this.par = parameters;
        par.name = name;
    }

    /** Constructs with a specified team. */
    public SimulationAgent(String name, SimulationTeam team) {
        this.team = team;
        par = new AgentParameters();
        if (team != null && team.par != null)
            par.copyParametersFrom(team.par.getDefaultAgentParameters());
        par.name = name;
    }

    //
    // UTILITY METHODS
    //

    @Override
    public String toString() {
        return par.name;
    }
    
    //
    // GETTERS & SETTERS
    //

    public AgentParameters getParameters() {
        return par;
    }

    public void setParameters(AgentParameters par) {
        this.par = par;
    }

    public void setInitialPosition(Point2D.Double ip) {
        par.setInitialPosition(ip);
    }

    public Point2D.Double getInitialPosition() {
        return par.getInitialPosition();
    }



    public AgentSensorProxy getSensorProxy() {
        return state;
    }

    //
    // AgentSensorProxy METHODS
    //

    public Point2D.Double getPosition() {
        return state.position;
    }

    public Point2D.Double getVelocity() {
        return state.velocity;
    }

    public boolean isActive() {
        return state.active;
    }

    //
    // SimulationComponent METHODS
    //
    
    /**
     * Initializes the agent's state. This is called AFTER the team initializes the
     * starting locations. Places the agent at the origin if there is no set starting location;
     * otherwise places the agent at the starting location.
     */
    @Override
    public void initStateVariables() {
        state = new AgentState();
        state.position = new Point2D.Double(par.initLoc.x, par.initLoc.y);
        state.active = true;
    }

    /** Sets the available position comm events. */
    @Override
    public void processAvailablePosEvents() {
        state.positionComms = super.availablePosEvents;
    }

    /** Sets the available task comm events. */
    @Override
    public void processAvailableTaskEvents() {
        state.taskComms = super.availableTaskEvents;
    }

    /** Compiles knowledge of visible opponents using the sensor. Sets the teammates
     * according to the team's active agents.
     * @param dt current table of distances
     */
    @Override
    public void gatherSensoryData(DistanceCache dt) {
        state.visibleOpponents = par.sensor.findAgents(dt, state.position, state.velocity);
        if (team != null) {
            state.availableMates = team.getActiveAgents();
            state.availableMates.remove(this);
        }
        state.visibleOpponents.removeAll(state.availableMates);
        state.visibleOpponents.remove(this);
//        System.out.println(
//                "    " + getName() + ": gatherSensoryData " + "    Opponents=" + state.visibleOpponents.toString() + "\n"
//              + "                           " + "   Teammates=" + state.availableMates.toString());
    }

    /** An agent compiles his "POV" by adding all observations supplied by incoming events
     * to his own observations.
     * @param dt the table of distances
     */
    @Override
    public void developPointOfView() {
        //System.out.print("    " + getName() + ": developPointOfView");
        // TODO (much later) - implement a more advanced algorithm for POV reconstruction!
        state.povOpponents.clear();
        // add visible opponents
        state.povOpponents.addAll(state.visibleOpponents);
        // add any communicated visible opponents
        for (PositionCommEvent pce : state.positionComms) {
            //System.out.println("              ....adding to POV: " + pce.getPositions());
            state.povOpponents.addAll(pce.getPositions());
        }
        //System.out.println("        ....final POV: " + state.povOpponents);
    }

    @Override
    public void generateTasks(DistanceCache dt) {
        if (par.tasker != null) {
            state.tasks = par.tasker.generateGiven(dt, this, state.povOpponents, state.availableMates);
        } else {
            state.tasks = Collections.emptyList();
        }
//        System.out.println("    " + getName() + ": generateTasks " + "    tasks=" + state.tasks);
    // TODO (later) - comm events for transmitting tasks to teammates
    }

    public Task mainTask;

    /**
     * Uses the task prioritizer to create a "main task". This task is then
     * used by the implementer to find an appropriate heading.
     */
    @Override
    public void setControlVariables(double simTime, double timePerStep) {
        mainTask = par.taskChooser.chooseTask(state.tasks);
        if (mainTask == null) {
            state.heading.x = 0; state.heading.y = 0;
        } else {
            Point2D.Double oldHeading = state.heading;
            double maxAngle = state.velocity.distance(0,0)*timePerStep/par.turnRad;
            state.heading = par.router.getDirectionFor(mainTask);
            if (PlanarMathUtils.angleBetween(oldHeading, state.heading) > maxAngle)
                state.heading = PlanarMathUtils.rotate(oldHeading, PlanarMathUtils.crossProduct(oldHeading, state.heading) > 0 ? maxAngle : -maxAngle);
        }
    }

    @Override
    public void adjustState(double timePerStep) {
        // fix turning radius
        
        // TODO - add restrictions here on tangential and normal acceleration
        state.velocity = new Point2D.Double(par.topSpeed * state.heading.x, par.topSpeed * state.heading.y);
        state.position.x += state.velocity.x * timePerStep;
        state.position.y += state.velocity.y * timePerStep;
    }

    @Override
    public void sendAllCommEvents(double simTime) {
        // 1. Broadcast position events to teammates
        if (state.visibleOpponents.size() > 0) {
            PositionCommEvent pce = new sim.comms.PositionCommEvent(this, simTime, simTime, state.visibleOpponents);
            CommEventReceiver cer = null;
            for (SimulationAgent sc : state.availableMates) {
                if (sc instanceof CommEventReceiver) {
                    cer = (CommEventReceiver) sc;
                    cer.acceptEvent(pce);
//                    System.out.println("    " + getName() + "   sendAllCommEvents -> " + sc + " (positions=" + pce + ")");
                }
            }
        }
    // 2. (TODO - tasking) Broadcast task events
    }
}
