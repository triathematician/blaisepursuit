/**
 * AgentParameterSupport.java
 * Created on Jul 23, 2009
 */

package sim.agent;

import java.awt.Color;
import java.awt.geom.Point2D;
import sim.agent.Sensor.SensorEnum;
import sim.tasks.Router;
import sim.tasks.Router.RouterEnum;
import sim.tasks.TaskChooser;
import sim.tasks.TaskChooser.ChooserEnum;
import sim.tasks.Tasker;
import sim.tasks.Tasker.TaskerEnum;

/**
 * <p>
 *   <code>AgentParameterSupport</code> encodes all the "starting values" of an agent,
 *   so that an agent can be completely reconstructed from an instance of this class.
 * </p>
 *
 * @author Elisha Peterson
 */
public class AgentParameters {
        
    //
    // PROPERTIES
    //

    String name = "";
    Color color = new Color (32, 32, 32);
    Point2D.Double initLoc = new Point2D.Double();

    double topSpeed = 1.0;
    double turnRad = 0.0;
    double commRad = 50.0;

    Sensor sensor = Sensor.NO_SENSOR;
    Tasker tasker = Tasker.NO_TASKER;
    TaskChooser taskChooser = TaskChooser.MAX_CHOOSER;
    Router router = Router.DEFAULT_INSTANCE;

    //
    // CONSTRUCTORS
    //

    /** Constructs with defaults. */
    public AgentParameters() {
    }

    public AgentParameters(String name) {
        this.name = name;
    }

    /** Construct with a few of the most common options. */
    public AgentParameters(String name, Color color, double speed) {
        this.name = name;
        this.color = color;
        this.topSpeed = speed;
    }


    //
    // BUILDERS
    //

    public AgentParameters name(String name) { this.name = name; return this; }
    public AgentParameters color(Color col) { this.color = col; return this; }
    public AgentParameters location(Point2D.Double loc) { this.initLoc = loc; return this; }
    
    public AgentParameters topSpeed(double speed) { this.topSpeed = speed; return this; }
    public AgentParameters turnRadius(double rad) { this.turnRad = rad; return this; }    
    public AgentParameters commRadius(double rad) { this.commRad = rad; return this; }

    public AgentParameters sensor(Sensor sensor) { this.sensor = sensor; return this; }
    public AgentParameters sensor(SensorEnum sensorEnum) { sensor = Sensor.getInstance(sensorEnum); return this; }

    public AgentParameters tasker(Tasker tasker) { this.tasker = tasker; return this; }
    public AgentParameters tasker(TaskerEnum taskerEnum) { tasker = Tasker.getInstance(taskerEnum); return this; }

    public AgentParameters taskChooser(TaskChooser chooser) { this.taskChooser = chooser; return this; }
    public AgentParameters taskChooser(ChooserEnum chooserEnum) { taskChooser = TaskChooser.getInstance(chooserEnum); return this; }

    public AgentParameters router(Router router) { this.router = router; return this; }
    public AgentParameters router(RouterEnum routerEnum) { router = Router.getInstance(routerEnum); return this; }

    //
    // UTILITIES
    //

    @Override
    public String toString() {
        return "AgentParameters - " + name + " ["
                + String.format("speed=%.2f, turnRad=%.2f, commRad=%.2f, ", topSpeed, turnRad, commRad)
                + ( initLoc==null ? "" : String.format("ipos=(%.2f, %.2f), ", initLoc.x, initLoc.y) )
                + sensor + ", "
                + tasker + ", "
                + taskChooser + ", "
                + router + "]";
    }

    public void copyParametersFrom(AgentParameters par2) {
        setName(par2.getName()+" Copy");
        setColor(par2.getColor());
        setInitialPosition(par2.getInitialPosition());

        setTopSpeed(par2.getTopSpeed());
        setTurningRadius(par2.getTurningRadius());
        setCommRadius(par2.getCommRadius());

        setSensor(par2.getSensor());
        setTasker(par2.getTasker());
        setTaskChooser(par2.getTaskChooser());
        setRouter(par2.getRouter());
    }

    //
    // GETTERS & SETTERS
    //

    public double getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(double speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("Speed < 0: " + speed);
        }
        if (this.topSpeed != speed) {
            this.topSpeed = speed;
        }
    }

    public double getTurningRadius() {
        return turnRad;
    }

    public void setTurningRadius(double rad) {
        if (rad < 0) {
            throw new IllegalArgumentException("Radius < 0: " + rad);
        }
        if (this.turnRad != rad) {
            this.turnRad = rad;
        }
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        if (this.sensor != sensor) {
            this.sensor = sensor;
        }
    }

    public double getCommRadius() {
        return commRad;
    }

    public void setCommRadius(double rad) {
        if (rad < 0) {
            throw new IllegalArgumentException("Radius < 0: " + rad);
        }
        if (this.commRad != rad) {
            this.commRad = rad;
        }
    }

    public Point2D.Double getInitialPosition() {
        return initLoc;
    }

    public void setInitialPosition(Point2D.Double ip) {
        // in older version the initLoc's x and y values were
        // changed if it was non-null. I'm not sure why that needs
        // to be the case...
        if (this.initLoc != ip) {
            this.initLoc = ip;
        }
    }

    public Tasker getTasker() {
        return tasker;
    }

    public void setTasker(Tasker tg) {
        if (this.tasker != tg) {
            this.tasker = tg;
        }
    }

    public TaskChooser getTaskChooser() {
        return taskChooser;
    }

    public void setTaskChooser(TaskChooser tp) {
        if (this.taskChooser != tp) {
            this.taskChooser = tp;
        }
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router ti) {
        if (this.router != ti) {
            this.router = ti;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    

}
