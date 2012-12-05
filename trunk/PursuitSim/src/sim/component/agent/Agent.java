/**
 * Agent.java
 * Created on Jul 14, 2009
 */
package sim.component.agent;

import sim.component.VisiblePlayer;
import sim.component.InitialPositionSetter;
import sim.component.team.Team;
import sim.*;
import sim.comms.*;
import sim.tasks.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * <p>
 *   <code>Agent</code> represents an agent in a simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class Agent extends SimCommunicator
        implements VisiblePlayer, InitialPositionSetter {

    /** Agent's parameters */
    public AgentParameters par;

    /** The agent's team */
    transient Team team;
    /** Stores state during the simulation. */
    transient public AgentState state = null;

    //
    // CONTROL VARIABLES
    //

    /** Heading (control variable), as an angle in radians */
    double heading;
    /** Speed (control variable) */
    double speed;

    //
    // CONSTRUCTORS
    //

    /** Constructs with no name */
    public Agent() {
        par = new AgentParameters();
    }
    
    /** Constructs without a team (defaults to null). */
    public Agent(String name) {
        par = new AgentParameters(name);
    }

    public Agent(AgentParameters parameters) {
        this.par = parameters;
    }

    /** Constructs with specified parameters. */
    public Agent(String name, AgentParameters parameters) {
        this.par = parameters;
        par.name = name;
    }

    /** Constructs with a specified team. */
    public Agent(String name, Team team) {
        this.team = team;
        par = new AgentParameters();
        if (team != null && team.getParameters() != null)
            par.copyParametersFrom(team.getAgentDefaults());
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

    public boolean isSafe() {
        return state.safe;
    }

    public void deactivate() {
        state.active = false;
        team.state.activeAgents.remove(this);
    }

    public void safety() {
        state.active = false;
        state.safe = true;
        team.state.activeAgents.remove(this);
        team.state.safeAgents.add(this);
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
        if (state == null)
            state = new AgentState();
        state.initialize(par.initLoc.x, par.initLoc.y);
        heading = 0;
        speed = 0;
    }

    /** Clears the current comm-sensor data, and adds all available positions. */
    @Override
    public void processAvailablePosEvents() {
        if (!state.active) return;
        state.commPlayers.clear();
        for (SensorComm sc : sensorComms)
            state.commPlayers.addAll(sc.getPositions());
        sensorComms.clear();
    }

    /** Sets the available task comm events. */
    @Override
    public void processAvailableTaskEvents() {
        if (!state.active) return;
        state.commTasks.clear();
        for (TaskComm sc : taskComms)
            for (Task t : sc.getTasks())
                if (t.owner == this)
                    state.commTasks.add(t);
        taskComms.clear();
    }

    /** Compiles knowledge of visible opponents using the sensor. Sets the teammates
     * according to the team's active agents.
     * @param dt current table of distances
     */
    @Override
    public void gatherSensoryData(DistanceCache dt) {
        if (!state.active) return;
        // sense players on the field
        par.sensor.findAgents(dt, this, state.visiblePlayers);
        // adjust teammates within comm distance
        if (state.visiblePlayers.size() > 0 && team != null) {
            state.commTeam.clear();
            for(VisiblePlayer vp : team.state.activeAgents)
                if (vp instanceof Agent && dt.getDistance(this, vp) < par.commRad)
                    state.commTeam.add((Agent) vp);
        }
    }

    /** An agent compiles his "POV" by adding all observations supplied by incoming events
     * to his own observations.
     * @param dt the table of distances
     */
    @Override
    public void developPointOfView() {
        if (!state.active) return;
        state.povPlayers.clear();
        state.povPlayers.addAll(state.visiblePlayers);
        state.povPlayers.addAll(state.commPlayers);
    }

    @Override
    public void generateTasks(DistanceCache dt) {
        if (!state.active) return;
        state.tasks.clear();
        if (par.taskers == null || par.taskers.length == 0) {
        } else if (par.taskers.length == 1)
            state.tasks.addAll(par.taskers[0].generateGiven(dt, this, state.povPlayers, state.commTeam));
        else {
            state.tasks = new ArrayList<Task>();
            for(Tasker t : par.taskers)
                state.tasks.addAll(t.generateGiven(dt, this, state.povPlayers, state.commTeam));
        }
        state.tasks.addAll(state.commTasks);
//        System.out.println("    " + par.name + ": generateTasks " + "    tasks=" + state.tasks);
    }

    public Task mainTask = null;

    /**
     * Uses the task prioritizer to create a "main task". This task is then
     * used by the implementer to find an appropriate heading.
     */
    @Override
    public void setControlVariables(double simTime, double timePerStep) {
        if (!state.active) return;
        if (state.tasks.size() > 0) {
            mainTask = par.taskChooser.chooseTask(state.tasks);
            // adjust heading; constrain to maximum of distance_traveled_per_step / turning_radius
            Point2D.Double desiredHeading = par.router.getDirectionFor(mainTask);
            if (par.turnRad == 0 || state.velocity == null)
                heading = Math.atan2(desiredHeading.y, desiredHeading.x);
            else {
                double maxHeadingChange = speed * timePerStep / par.turnRad;
                double desiredAngle = Math.atan2(desiredHeading.y, desiredHeading.x); // range of -pi to pi
                heading = Math.abs(angleDiff(heading, desiredAngle)) > maxHeadingChange
                        ? heading + Math.signum(angleDiff(heading, desiredAngle)) * maxHeadingChange
                        : desiredAngle;
            }

            // adjust speed: constrain to within given acceleration and given top speed
            double desiredSpeed = Math.min(par.topSpeed, state.position.distance(mainTask.targetLoc) / timePerStep);
            speed = Math.abs(desiredSpeed - speed) > (par.acceleration * timePerStep)
                    ? speed + Math.signum(desiredSpeed - speed) * par.acceleration * timePerStep
                    : desiredSpeed;

//            System.out.println(String.format(par.name + " Heading ... %.2f -[%.2f]-> %.2f\t\t\t\t Speed ... %.2f -> %.2f",
//                    desiredAngle, maxHeadingChange, heading,
//                    desiredSpeed, speed));
        }
    }

    /** Computes difference in angles, in value of -pi to pi. */
    double angleDiff(double angle1, double angle2) {
        double value = (angle2 - angle1) % (2*Math.PI) ; // now in range from -2pi to 2pi
        double result = 0.0;
        if (value < -Math.PI)
            result = value + 2*Math.PI;
        else if (value > Math.PI)
            result = value - 2*Math.PI;
        else
            result = value;
        return result;
    }

    @Override
    public void adjustState(double timePerStep) {
        if (!state.active) return;
        if (mainTask != null)
            state.velocity = new Point2D.Double(speed * Math.cos(heading), speed * Math.sin(heading));
        if (state.velocity != null) {
            state.position.x += state.velocity.x * timePerStep;
            state.position.y += state.velocity.y * timePerStep;
        }
    }

    public void sendAllCommEvents(double simTime) {
        if (!state.active) return;
        if (state.visiblePlayers.size() > 0 && state.commTeam.size() > 0) {
            SensorComm pce = new SensorComm(this, simTime, simTime, state.visiblePlayers);
            for (Agent sc : state.commTeam)
                sc.acceptEvent(pce);
        }
    }
}
