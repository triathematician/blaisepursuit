/**
 * AgentParameters.java
 * Created on Jul 17, 2009
 */
package sim.agent;

import java.awt.geom.Point2D;
import sim.tasks.Tasker;
import sim.tasks.TaskChooser;
import sim.tasks.Router;

/**
 * <p>
 *   <code>AgentParameters</code> is an interface that describes the basic
 *   properties of an agent in a pursuit-evasion game.
 * </p>
 *
 * @author Elisha Peterson
 */
public interface AgentParameters {

    /**
     * Gives the top speed of the agent.
     * @return top speed of the agent
     */
    public double getTopSpeed();

    /** Sets top speed of agent. */
    public void setTopSpeed(double speed);

    /** @return turning radius of the agent */
    public double getTurningRadius();

    /** Sets turning radius of agent */
    public void setTurningRadius(double rad);

    /**
     * Gives the sensor used by the agent.
     * @return sensor being used by the agent
     */
    public Sensor getSensor();

    /** Sets sensor type of agent. */
    public void setSensor(Sensor sensor);

    /** @return communications radius of the agent */
    public double getCommRadius();

    /** Sets comm radius of agent. */
    public void setCommRadius(double rad);

    /**
     * Gives the agent's initial position in the simulation.
     * @return initial position of the agent
     */
    public Point2D.Double getInitialPosition();

    /** Sets initial position of agent */
    public void setInitialPosition(Point2D.Double ip);

    /**
     * Tells whether an agent is active when the simulation begins.
     * @return true if the agent begins the simulation as active
     */
    public boolean isInitiallyActive();

    /** Sets initially active status of agent */
    public void setInitiallyActive(boolean ia);

    // /** @return algorithm for fusing sensory data from multiple sources. */
    // public SensorFusionAlgorithm getSensorFusionAlgorithm();

    /**
     * Gives algorithm for generating tasks for self and teammates.
     * @return algorithm for generating tasks for other agents
     */
    public Tasker getTasker();

    /** Sets task generator of agent */
    public void setTasker(Tasker tg);

    /**
     * Gives algorithm for prioritizing tasks.
     * @return algorithm for prioritizing tasks
     */
    public TaskChooser getTaskChooser();

    /** Sets task prioritizer of agent */
    public void setTaskChooser(TaskChooser tp);

    /**
     * Gives algorithm for implementing a chosen task.
     * @return algorithm for implementing a chosen task
     */
    public Router getRouter();

    /** Sets task implementer of agent */
    public void setRouter(Router ti);
}
