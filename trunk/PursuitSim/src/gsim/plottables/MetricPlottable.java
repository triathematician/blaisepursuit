/**
 * MetricPlottable.java
 * Created on Feb 10, 2010
 */

package gsim.plottables;

import gsim.logger.AgentMetricLogger;
import gsim.logger.MetricLogger;
import java.awt.Color;
import java.awt.geom.Point2D;
import org.bm.blaise.specto.plottable.VPath;
import sim.component.team.Team;

/**
 * <p>
 *   <code>MetricPlottable</code> encapsulates metric data.
 *   class.
 * </p>
 *
 * @author Elisha Peterson
 */
public class MetricPlottable extends VPath<Point2D.Double> {

    MetricLogger log;
    String name;

    /**
     * Constructs for a given team logger. Also initializes and displays the
     * initial points, initializes the player paths.
     * @param log
     */
    public MetricPlottable(MetricLogger log) {
        super(new Point2D.Double[]{});
        this.log = log;
        this.name = log.getName();
        setValues(log.getValueGraph());
        setBaseColor(log.getColor());
    }

    /** Call when the log has updated. Revises the paths. */
    public void updatePaths() {
        values = log.getValueGraph();
    }

    public void setBaseColor(Color c) {
        if (c != null) {
            getStyle().setColor(c);
            fireStateChanged();
        }
    }

    public Color getBaseColor() {
        return getStyle().getColor();
    }

    @Override
    public String toString() {
        return name;
    }
}
