/**
 * TeamPlottableGroup.java
 * Created on Aug 17, 2009
 */

package gsim.plottables;

import gsim.logger.TeamLogger;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import org.bm.blaise.specto.plottable.VPath;
import org.bm.blaise.specto.plottable.VPointSet;
import org.bm.blaise.specto.primitive.PointStyle;
import org.bm.blaise.specto.visometry.PlottableGroup;
import sim.component.team.LocationGenerator;
import sim.component.agent.Agent;
import sim.component.team.Team;

/**
 * <p>
 *   <code>TeamPlottableGroup</code> encapsulates visual elements for the specified team.
 *   Agents are represented by moveable points. The data is stored in an associated "logger"
 *   class.
 * </p>
 *
 * @author Elisha Peterson
 */
public class TeamPlottableGroup extends PlottableGroup<Point2D.Double> {

    /** Log storing the underlying data */
    TeamLogger log;
    /** Visual for the set of initial points */
    VPointSet<Point2D.Double> initialPoints;
    /** Visual for the set of paths */
    List<VPath<Point2D.Double>> paths;
    /** The team color. */
    Color teamColor;

    /**
     * Constructs for a given team logger. Also initializes and displays the
     * initial points, initializes the player paths.
     * @param log
     */
    public TeamPlottableGroup(TeamLogger log) {
        name = log.getTeam().toString();
        this.log = log;
        teamColor = log.getTeam().getParameters().getColor();

        paths = new ArrayList<VPath<Point2D.Double>>(log.getAgents().size());
        for (Agent sa : log.getAgents()) {
            VPath<Point2D.Double> path = new VPath<Point2D.Double>(log.getPath(sa));
            path.getStyle().setColor( sa.par.color == null
                    ? teamColor
                    : sa.par.color );
            paths.add(path);
        }
        addAll(paths);

        initialPoints = new VPointSet<Point2D.Double>(log.getInitialPositions());
        initialPoints.getPointStyle().setFillColor(teamColor.brighter());
        initialPoints.getPointStyle().setStrokeColor(teamColor);
        initialPoints.setEditable(log.getTeam() instanceof Team);
        add(initialPoints); // keep initial points on top
    }

    boolean editing = false;

    /** Call when the log has updated, typically after a simulation is complete. Revises the paths. */
    public void updatePaths() {
        editing = true;
        initialPoints.setValues(log.getInitialPositions());
        int n = log.getAgents().size();
        while (paths.size() > n) {
            remove(paths.get(paths.size()-1));
            paths.remove(paths.size()-1);
        }
        teamColor = log.getTeam().getParameters().getColor();
        
        for (int i = 0; i < n; i++) {
            if (i >= paths.size()) {
                Agent sa = log.getAgents().get(i);
                VPath<Point2D.Double> path = new VPath<Point2D.Double>(log.getPath(sa));
                path.getStyle().setColor( sa.par.color == null
                        ? teamColor
                        : sa.par.color );
                paths.add(path);
                add(paths.get(i));
            } else
                paths.get(i).setValues(log.getPath(i));
        }
        editing = false;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!editing) {
            // if an initial point changes, pass the change on to the AGENT's initial position. then re-run the simulation
            // called when an initial point changes
            if (e.getSource() == initialPoints) {
                int idx = initialPoints.getSelectedIndex();
                Point2D.Double p = initialPoints.getSelectedValue();
                assert p != null;
                log.getTeam().getParameters().setLocationGenerator(LocationGenerator.DELEGATE_INSTANCE);
                log.getInitialPositionSetter(idx).setInitialPosition(p);
                fireStateChanged();
            }
        }
    }

    public Color getBaseColor() {
        return initialPoints.getPointStyle().getFillColor();
    }

    public void setBaseColor(Color c) {
        if (c != null) {
            teamColor = c;
            initialPoints.getPointStyle().setFillColor(teamColor.brighter());
            initialPoints.getPointStyle().setStrokeColor(teamColor);
            fireStateChanged();
        }
    }


    public PointStyle getPointStyle() {
        return initialPoints.getPointStyle();
    }

    public void setPointStyle(PointStyle style) {
        initialPoints.setPointStyle(style);
        fireStateChanged();
    }


}
