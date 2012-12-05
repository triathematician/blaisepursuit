/**
 * PhantomAgent.java
 * Created on Jul 24, 2009
 */

package sim.component;

import sim.component.team.Team;
import java.awt.geom.Point2D;

/**
 * <p>
 *   <code>PhantomAgent</code> represents a "phantom" or non-existent agent, but which
 *   can be used to create tasks, etc.
 * </p>
 *
 * @author Elisha Peterson
 */
public class PhantomAgent implements VisiblePlayer {

    Point2D.Double position;
    Point2D.Double velocity;

    public PhantomAgent(Point2D.Double position, Point2D.Double velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    @Override
    public String toString() {
        return String.format("PhantomPlayer @ (%.2f, %.2f)", position.x, position.y);
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public Point2D.Double getVelocity() {
        return velocity;
    }

    public Team getTeam() {
        return null;
    }

    public boolean isActive() {
        return true;
    }
}
