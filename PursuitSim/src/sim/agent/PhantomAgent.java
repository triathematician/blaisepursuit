/**
 * PhantomAgent.java
 * Created on Jul 24, 2009
 */

package sim.agent;

import java.awt.geom.Point2D;

/**
 * <p>
 *   <code>PhantomAgent</code> represents a "phantom" or non-existent agent, but which
 *   can be used to create tasks, etc.
 * </p>
 *
 * @author Elisha Peterson
 */
public class PhantomAgent implements AgentSensorProxy {

    Point2D.Double position;
    Point2D.Double velocity;
    SimulationTeam team;

    public PhantomAgent(Point2D.Double position, Point2D.Double velocity, SimulationTeam team) {
        this.position = position;
        this.velocity = velocity;
        this.team = team;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public Point2D.Double getVelocity() {
        return velocity;
    }

    public SimulationTeam getTeam() {
        return team;
    }

    public boolean isActive() {
        return true;
    }
}
