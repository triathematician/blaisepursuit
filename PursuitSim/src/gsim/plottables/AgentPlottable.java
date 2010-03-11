/**
 * TeamPlottableGroup.java
 * Created on Aug 17, 2009
 */

package gsim.plottables;

import gsim.logger.AgentLogger;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import javax.swing.event.ChangeEvent;
import org.bm.blaise.sequor.timer.TimeClock;
import org.bm.blaise.specto.plottable.VPath;
import org.bm.blaise.specto.plottable.VPoint;
import org.bm.blaise.specto.primitive.PointStyle;
import org.bm.blaise.specto.visometry.PlottableGroup;
import org.bm.blaise.specto.visometry.Visometry;
import org.bm.blaise.specto.visometry.VisometryGraphics;
import sim.component.InitialPositionSetter;
import sim.component.agent.Agent;
import sim.component.ParametricPath;
import sim.component.RandomPath;

/**
 * <p>
 *   <code>TeamPlottableGroup</code> encapsulates visual elements for the specified team.
 *   Agents are represented by moveable points. The data is stored in an associated "logger"
 *   class.
 * </p>
 *
 * @author Elisha Peterson
 */
public class AgentPlottable extends PlottableGroup<Point2D.Double> {

    /** Log storing the underlying data */
    AgentLogger log;
    /** Visual for the initial points */
    VPoint<Point2D.Double> initialPoint;
    /** Visual for the path */
    VPath<Point2D.Double> path;

    /**
     * Constructs for a given team logger. Also initializes and displays the
     * initial points, initializes the player paths.
     * @param log
     */
    public AgentPlottable(AgentLogger log) {
        name = log.getAgent().toString();
        this.log = log;
        path = new VPath<Point2D.Double>(log.getPath());
        add(path);

        initialPoint = new VPoint<Point2D.Double>(log.getInitialPosition());
        initialPoint.setLabel(name);
        initialPoint.setCoordVisible(false);
        setEditable(log.getAgent() instanceof Agent);
        if ( log.getAgent() instanceof InitialPositionSetter )
            add(initialPoint); // keep initial points on top
        if (log.getAgent() instanceof Agent) {
            setColor(((Agent) log.getAgent()).getParameters().getColor());
        }
    }

    boolean editing = false;

    /** Call when the log has updated, typically after a simulation is complete. Revises the paths. */
    public void updatePath() {
        editing = true;
        initialPoint.setPoint(log.getInitialPosition());
        path.setValues(log.getPath());
        editing = false;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!editing) {
            // if an initial point changes, pass the change on to the AGENT's initial position. then re-run the simulation
            // called when an initial point changes
            if (e.getSource() == initialPoint) {
                Point2D.Double p = initialPoint.getPoint();
                assert p != null;
                if (log.getAgent() instanceof InitialPositionSetter) {
                    ((InitialPositionSetter) log.getAgent()).setInitialPosition(p);
                    fireStateChanged();
                }
            }
        }
    }

    public PointStyle getPointStyle() {
        return initialPoint.getStyle();
    }

    public void setPointStyle(PointStyle style) {
        initialPoint.setStyle(style);
        fireStateChanged();
    }

    public Color getColor() {
        return initialPoint.getStyle().getFillColor();
    }

    public void setColor(Color color) {
        initialPoint.getStyle().setFillColor(color);
        initialPoint.getLabelStyle().setColor(color);
        path.getStyle().setColor(color);
    }

    double time;

    @Override
    public void recomputeAtTime(Visometry<Double> vis, VisometryGraphics<Double> canvas, TimeClock clock) {
        this.time = clock.getTime();
        super.recomputeAtTime(vis, canvas, clock);
    }

    @Override
    public void paintComponent(VisometryGraphics<Double> canvas) {
        if (isAnimationOn()) {
            Color fill = getColor();
            initialPoint.getStyle().setFillColor(null);
            super.paintComponent(canvas);
            initialPoint.getStyle().setFillColor(fill);

            if (path.getValues().length > 0) {
                canvas.setPointStyle(initialPoint.getStyle());
                Point2D.Double lastPoint = path.getValues()[Math.min((int) time, path.getValues().length-1)];
                canvas.drawPoint(lastPoint, selected);
            }
        } else {
            super.paintComponent(canvas);
        }
    }
}
