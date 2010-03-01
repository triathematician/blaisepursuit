/**
 * VoronoiFrontier.java
 * Created on Dec 11, 2009
 */

package org.bm.blaise.specto.plane.compgeom;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.bm.blaise.scio.algorithm.Tesselation.Polygon;
import org.bm.blaise.scio.algorithm.voronoi.VoronoiFrontier;
import org.bm.blaise.scio.algorithm.voronoi.VoronoiFrontier.FrontierArc;
import org.bm.blaise.scio.algorithm.voronoi.VoronoiUtils;
import org.bm.blaise.specto.plottable.VPointSet;
import org.bm.blaise.specto.primitive.BlaisePalette;
import org.bm.blaise.specto.primitive.PathStyle;
import org.bm.blaise.specto.primitive.PointStyle.PointShape;
import org.bm.blaise.specto.visometry.VisometryGraphics;
import org.bm.blaise.specto.visometry.VisometryMouseEvent;

/**
 * <p>
 *    This class is a dynamic visualization of the Voronoi algorithm. It allows the user
 *    to move a "sweep line" across the screen and see the resulting Delaunay triangulation
 *    and Voronoi tesselation that results from using Fortune's algorithm up to
 *    the point of the sweep line (within the code called the <i>directrix</i>).
 * </p>
 * <p>
 *    There are options to show/hide both the triangulation and the tesselation.
 * </p>
 * @author Elisha Peterson
 */
public class PlaneVoronoiFrontier extends VPointSet<Point2D.Double> {

    /** Whether directrix and frontier are displayed and used in calcs */
    boolean directrixVisible = true;
    /** Style used for the directrix. */
    PathStyle directrixStyle = new PathStyle(BlaisePalette.STANDARD.func1());
    /** Class responsible for the algorithm. */
    VoronoiFrontier frontier;

    /** Style used for the triangulation. */
    PathStyle delaunayStyle = new PathStyle(BlaisePalette.STANDARD.vector());
    /** Whether delaunay lines are visible. */
    boolean delaunayVisible = false;

    /** This plottable displays the resselation. */
    PlaneTesselation voronoiTesselation = new PlaneTesselation(null);
    /** Whether tesselation is visible. */
    boolean tesselationVisible = true;

    //
    // CONSTRUCTORS
    //
    
    /** Construct with a list of values. */
    public PlaneVoronoiFrontier(Point2D.Double... values) {
        super(values);
        setLabelsVisible(false);
        frontier = new VoronoiFrontier(getValuesAsList());
        setDirectrix(0);
        addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                frontier.setPoints(getValuesAsList());
                if (!directrixVisible) {
                    double curDir = frontier.getMaxDirectrix();
                    frontier.setMaxDirectrix(Double.POSITIVE_INFINITY);
                    frontier.calculate();
                    frontier.setMaxDirectrix(curDir);
                } else {
                    frontier.calculate();
                }
                voronoiTesselation.setTesselation(frontier.getTesselation());
                Color col = new Color(200, 200, 200);
                Map<Polygon, Color> colorMap = new HashMap<Polygon, Color>();
                for(Point2D.Double v : PlaneVoronoiFrontier.this.values) {
                    colorMap.put(frontier.getPolygonMap().get(v), col);
                    col = PlaneTesselation.nextColor(col, 13, 83, 157);
                }
                voronoiTesselation.setColorMap(colorMap);
            }
        });
        fireStateChanged();
    }

    //
    // GETTERS & SETTERS
    //

    public double getDirectrix() {
        if (frontier != null)
            return frontier.getMaxDirectrix();
        else
            return Double.NaN;
    }

    public void setDirectrix(double directrix) {
        if (frontier != null && frontier.getMaxDirectrix() != directrix) {
            frontier.setMaxDirectrix(directrix);
            fireStateChanged();
        }
    }

    public boolean isDirectrixVisible() {
        return directrixVisible;
    }

    public void setDirectrixVisible(boolean directrixVisible) {
        if (this.directrixVisible != directrixVisible) {
            this.directrixVisible = directrixVisible;
            fireStateChanged();
        }
    }

    public boolean isDelaunayVisible() {
        return delaunayVisible;
    }

    public void setDelaunayVisible(boolean delaunayVisible) {
        this.delaunayVisible = delaunayVisible;
        fireStateChanged();
    }

    public boolean isTesselationVisible() {
        return tesselationVisible;
    }

    public void setTesselationVisible(boolean tesselationVisible) {
        this.tesselationVisible = tesselationVisible;
        fireStateChanged();
    }

    /** Retrieves values as a list instead of an array. */
    List<Point2D.Double> getValuesAsList() {
        List<Point2D.Double> aPoints = new ArrayList<Point2D.Double>();
        for (int i = 0; i < values.length; i++) { aPoints.add(values[i]); }
        return aPoints;
    }

    public VoronoiFrontier getFrontier() {
        return frontier;
    }

    //
    // PAINT METHODS
    //

    @Override
    public void paintComponent(VisometryGraphics<Point2D.Double> vg) {
        // draws the tesselation
        if (tesselationVisible)
            voronoiTesselation.paintComponent(vg);

        if (directrixVisible) {
            // draws the directri
            vg.setPathStyle(directrixStyle);
            double directrix = getDirectrix();
            vg.drawVLine(new Point2D.Double(directrix,0));
            PointShape shp = pointStyle.getShape();
            vg.setPointStyle(pointStyle);
            pointStyle.setShape(PointShape.CROSS);
            vg.drawPoint(new Point2D.Double(directrix, 0));
            pointStyle.setShape(shp);

            // draws the frontier curve
            List<Point2D.Double> frontierCurve = new ArrayList<Point2D.Double>();
            for (FrontierArc arc : frontier) {
                double ly = Math.max(-10.0, arc.lowerY(directrix));
                double uy = Math.min(10.0, arc.upperY(directrix));
                for (double y = ly; y <= uy; y += .05)
                    frontierCurve.add(new Point2D.Double(VoronoiUtils.getXOnParabolaOfGivenFocusAndDirectrix(y, arc.getPoint(), directrix), y));
            }
            vg.drawPath(frontierCurve.toArray(new Point2D.Double[]{}));
        }

        // draws delaunay connections
        if (delaunayVisible) {
            vg.setPathStyle(delaunayStyle);
            List<Point2D.Double[]> connections = frontier.getAdjacencyList();
            for (Point2D.Double[] c : connections) {
                vg.drawSegment(c[0], c[1]);
            }
        }

        // draws the points
        super.paintComponent(vg);
    }

    //
    // MOUSE HANDLING
    //

    boolean clickDirectrix = false;

    @Override
    public boolean isClickablyCloseTo(VisometryMouseEvent<Point2D.Double> e) {
        boolean clickPoint = super.isClickablyCloseTo(e);
        if (clickPoint) {
            clickDirectrix = false;
            return true;
        } else {
            if (e.withinRangeOf(new Point2D.Double(getDirectrix(), 0), 5)) {
                clickDirectrix = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseDragged(VisometryMouseEvent<Point2D.Double> e) {
        if (clickDirectrix) {
            setDirectrix(e.getCoordinate().x);
        } else {
            super.mouseDragged(e);
        }
    }

    @Override
    public String toString() {
        return "Voronoi Tesselation";
    }

}
