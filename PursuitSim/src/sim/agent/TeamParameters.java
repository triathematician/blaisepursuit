/**
 * TeamParameters.java
 * Created on Jul 17, 2009
 */

package sim.agent;

import java.awt.Color;
import java.util.Arrays;
import sim.metrics.CaptureCondition;
import sim.metrics.VictoryCondition;
import sim.tasks.Tasker;
import sim.agent.LocationGenerator.LocEnum;
import sim.agent.Sensor.SensorEnum;
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

    String name = "";
    AgentParameters defaults;

    LocationGenerator startLoc = LocationGenerator.DELEGATE_INSTANCE;
    Sensor sensor = Sensor.NO_SENSOR;
    Tasker tasker = Tasker.NO_TASKER;
    
    VictoryCondition victory = VictoryCondition.NONE;
    CaptureCondition[] capture = new CaptureCondition[]{};

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
        this.defaults.color = color;
    }

    //
    // BUILDERS
    //

    public TeamParameters name(String name) { this.name = name; return this; }
    public TeamParameters color(Color col) { defaults.color = col; return this; }

    public TeamParameters locations(LocationGenerator loc) { this.startLoc = loc; return this; }
    public TeamParameters locations(LocEnum locEnum) { startLoc = LocationGenerator.getInstance(locEnum); return this; }
    
    public TeamParameters sensor(Sensor sensor) { this.sensor = sensor; return this; }
    public TeamParameters sensor(SensorEnum sensorEnum) { sensor = Sensor.getInstance(sensorEnum); return this; }

    public TeamParameters tasker(Tasker tasker) { this.tasker = tasker; return this; }
    public TeamParameters tasker(TaskerEnum taskerEnum) { tasker = Tasker.getInstance(taskerEnum); return this; }

    public TeamParameters victory(VictoryCondition vic) { this.victory=vic; return this; }

    /** Appends a capture condition to the list of capture conditions, and returns the class. */
    public TeamParameters capture(CaptureCondition cond) {
        CaptureCondition[] newCaps = new CaptureCondition[capture.length + 1];
        System.arraycopy(capture, 0, newCaps, 0, capture.length);
        newCaps[capture.length] = cond;
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
                + tasker + ", "
                + victory + ", "
                + (capture.length == 0 ? "" : capture.length == 1 ? capture[0] : Arrays.toString(capture)) + "]";
    }

    public void copyParametersFrom(TeamParameters par2) {
        defaults.copyParametersFrom(par2.defaults);
        setLocationGenerator(par2.getLocationGenerator());
        setSensor(par2.getSensor());
        setTasker(par2.getTasker());
        setVictoryCondition(par2.getVictoryCondition());
        setCaptureCondition(par2.getCaptureCondition());
        setName(par2.name + " Copy");
        setColor(par2.getColor());
    }

    //
    // GETTERS & SETTERS
    //

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

    public Tasker getTasker() {
        return tasker;
    }

    public void setTasker(Tasker taskGen) {
        this.tasker = taskGen;
    }

    public CaptureCondition[] getCaptureCondition() {
        return capture;
    }

    public void setCaptureCondition(CaptureCondition... capture) {
        this.capture = capture;
    }

    public CaptureCondition getCaptureCondition(int i) {
        return capture[i];
    }

    public void setCaptureCondition(int i, CaptureCondition cap) {
        capture[i] = cap;
    }

    public VictoryCondition getVictoryCondition() {
        return victory;
    }

    public void setVictoryCondition(VictoryCondition victory) {
        this.victory = victory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return defaults.color;
    }

    public void setColor(Color color) {
        defaults.color = color;
    }
}
