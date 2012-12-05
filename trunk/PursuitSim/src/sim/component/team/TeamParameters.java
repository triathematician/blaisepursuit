/**
 * TeamParameters.java
 * Created on Jul 17, 2009
 */

package sim.component.team;

import java.awt.Color;
import java.util.Arrays;
import sim.component.agent.AgentParameters;
import sim.metrics.Capture;
import sim.metrics.VictoryCondition;
import sim.tasks.Tasker;
import sim.component.team.LocationGenerator.LocEnum;
import sim.comms.Sensor;
import sim.comms.Sensor.SensorEnum;
import sim.tasks.Tasker.TaskerEnum;

// TODO - add bounds checking for set commands

/**
 * <p>
 *   <code>TeamParameters</code> encodes a set of observable parameters for a team.
 * </p>
 *
 * @author Elisha Peterson
 */
public class TeamParameters {

    //
    // PROPERTIES
    //

    public String name = "Team";
    public AgentParameters defaults;

    public LocationGenerator startLoc = LocationGenerator.DELEGATE_INSTANCE;
    public Sensor sensor = Sensor.NO_SENSOR;
    public Tasker[] taskers = new Tasker[]{};
    
    public VictoryCondition victory = VictoryCondition.NONE;
    public Capture[] capture = new Capture[]{};

    //
    // CONSTRUCTORS
    //

    /** Constructs with defaults. */
    public TeamParameters() {
        this(new AgentParameters());
    }

    public TeamParameters(String name) {
        this.name = name;
    }

    /** Constructs with a set of agent parameter defaults. */
    public TeamParameters(AgentParameters agentDefaults) {
        this.defaults = agentDefaults;
    }

    public TeamParameters(String name, Color color, AgentParameters agentDefaults) {
        this.name = name;
        this.defaults = agentDefaults;
        this.defaults.setColor(color);
    }

    //
    // BUILDERS
    //

    public TeamParameters name(String name) { this.name = name; return this; }
    public TeamParameters color(Color col) { defaults.setColor(col); return this; }

    public TeamParameters locations(LocationGenerator loc) { this.startLoc = loc; return this; }
    public TeamParameters locations(LocEnum locEnum) { startLoc = LocationGenerator.getInstance(locEnum); return this; }
    
    public TeamParameters sensor(Sensor sensor) { this.sensor = sensor; return this; }
    public TeamParameters sensor(SensorEnum sensorEnum) { sensor = Sensor.getInstance(sensorEnum); return this; }

    /** Appends a tasker to the end of the list of taskers, and returns the parameters class. */
    public TeamParameters tasker(Tasker tasker) {
        Tasker[] newTaskers = new Tasker[taskers.length + 1];
        System.arraycopy(taskers, 0, newTaskers, 0, taskers.length);
        newTaskers[taskers.length] = tasker;
        taskers = newTaskers;
        return this;
    }
    public TeamParameters tasker(TaskerEnum taskerEnum) { return tasker(Tasker.getInstance(taskerEnum)); }

    public TeamParameters victory(VictoryCondition vic) { this.victory=vic; return this; }

    /** Appends a capture condition to the list of capture conditions, and returns the class. */
    public TeamParameters capture(Capture cond) {
        Capture[] newCaps = new Capture[capture.length + 1];
        System.arraycopy(capture, 0, newCaps, 0, capture.length);
        newCaps[capture.length] = cond;
        capture = newCaps;
        return this;
    }

    //
    // UTILITIES
    //

    @Override
    public String toString() {
        return "TeamParameters - " + name + " ["
                + startLoc + ", "
                + sensor + ", "
                + (taskers.length == 0 ? "" : taskers.length == 1 ? taskers[0] : Arrays.toString(taskers)) + ", "
                + victory + ", "
                + (capture.length == 0 ? "" : capture.length == 1 ? capture[0] : Arrays.toString(capture)) + "]";
    }

    public void copyParametersFrom(TeamParameters par2) {
        setName(par2.name + " Copy");
        setColor(par2.getColor());

        defaults.copyParametersFrom(par2.defaults);

        setLocationGenerator(par2.getLocationGenerator());

        setSensor(par2.getSensor());
        setTasker(par2.getTasker());
        setVictoryCondition(par2.getVictoryCondition());
        setCaptureCondition(par2.getCaptureCondition());
    }

    //
    // GETTERS & SETTERS
    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return defaults.getColor();
    }

    public void setColor(Color color) {
        defaults.setColor(color);
    }

    public AgentParameters getDefaultAgentParameters() {
        return defaults;
    }

    public void setDefaultAgentParameters(AgentParameters defaults) {
        this.defaults = defaults;
    }

    public LocationGenerator getLocationGenerator() {
        return startLoc;
    }

    public void setLocationGenerator(LocationGenerator startLoc) {
        this.startLoc = startLoc;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Tasker[] getTasker() {
        return taskers;
    }

    public void setTasker(Tasker[] tg) {
        if (this.taskers != tg) {
            this.taskers = tg;
        }
    }

    public Tasker getTasker(int i) {
        return taskers[i];
    }

    public void setTasker(int i, Tasker t) {
        taskers[i] = t;
    }

    public Capture[] getCaptureCondition() {
        return capture;
    }

    public void setCaptureCondition(Capture... capture) {
        this.capture = capture;
    }

    public Capture getCaptureCondition(int i) {
        return capture[i];
    }

    public void setCaptureCondition(int i, Capture cap) {
        capture[i] = cap;
    }

    public VictoryCondition getVictoryCondition() {
        return victory;
    }

    public void setVictoryCondition(VictoryCondition victory) {
        this.victory = victory;
    }
}
