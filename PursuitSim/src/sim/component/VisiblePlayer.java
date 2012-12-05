/**
 * AgentSensorProxy.java
 * Created on Jul 17, 2009
 */

package sim.component;

import java.awt.geom.Point2D;

/**
 * <p>
 *   <code>AgentSensorProxy</code> describes the parameters available when one agent senses
 *   another agent.
 * </p>
 *
 * @author Elisha Peterson
 */
public interface VisiblePlayer {

    /** Gets position of agent.
     * @return current location of the agent. */
    public Point2D.Double getPosition();

    /** Gets velocity of agent.
     * @return current velocity of the agent. */
    public Point2D.Double getVelocity();

    /** Gets active status of agent.
     * @return true if the agent is currently active, otherwise false. */
    public boolean isActive();
}
