/**
 * VoronoiFrontier.java
 * Created on Dec 11, 2009
 */

package org.bm.blaise.specto.plane.compgeom;

import coordinate.DomainHint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bm.blaise.scio.algorithm.Tesselation;
import org.bm.blaise.scio.algorithm.voronoi.VoronoiFrontier;
import org.bm.blaise.scio.algorithm.voronoi.VoronoiUtils;
import primitive.GraphicMesh;
import primitive.style.MeshStyle;
import primitive.style.PathStyle;
import scio.coordinate.RealInterval;
import util.ChangeBroadcaster;
import visometry.PointDragListener;
import visometry.VDraggablePrimitiveEntry;
import visometry.VPrimitiveEntry;
import visometry.plottable.VPointSet;

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
public class PlaneVoronoiFrontier extends VPointSet<Point2D.Double>
        implements PointDragListener<Point2D.Double> {

    /** Base object encoding the algorithm and scenario */
    VoronoiFrontier frontier;

    /** Stores entry for the directrix. */
    VDraggablePrimitiveEntry dxEntry;
    /** Stores entry for the frontier */
    VPrimitiveEntry frontierEntry;
    /** Stores entry for the tesselation. */
    VPrimitiveEntry tessEntry;

    public PlaneVoronoiFrontier(Point2D.Double... values) {
        super(values);
        frontier = new VoronoiFrontier(Arrays.asList(values));
        frontier.setMaxDirectrix(0.0);
        addPrimitive(dxEntry = new VDraggablePrimitiveEntry(null, new PathStyle(), this));
        addPrimitive(frontierEntry = new VPrimitiveEntry(null, dxEntry.style));
        addPrimitive(tessEntry = new VPrimitiveEntry(null, new MeshStyle()));
    }

    @Override
    public String toString() {
        return "Voronoi Tesselation";
    }

    /** @return location of directrix */
    public double getDirectrix() { return frontier == null ? Double.NaN : frontier.getMaxDirectrix(); }
    /** Sets the directrix value */
    public void setDirectrix(double directrix) { if (frontier != null && directrix != frontier.getMaxDirectrix()) { frontier.setMaxDirectrix(directrix); firePlottableChanged(); } }

    /** @return directrix style */
    public PathStyle getDirectrixStyle() { return (PathStyle) dxEntry.style; }
    /** Sets new directrix style */
    public void setDirectrixStyle(PathStyle style) { dxEntry.style = style; firePlottableStyleChanged(); }
    /** @return directrix visibility */
    public boolean isDirectrixVisible() { return dxEntry.visible; }
    /** Sets directrix visibility */
    public void setDirectrixVisible(boolean visibility) { dxEntry.visible = visibility; frontierEntry.visible = false; firePlottableStyleChanged(); }

    /** @return tesselation style */
    public MeshStyle getTesselationStyle() { return (MeshStyle) tessEntry.style; }
    /** Sets new tesselation style */
    public void setTesselationStyle(MeshStyle style) { tessEntry.style = style; firePlottableStyleChanged(); }
    /** @return tesselation visibility */
    public boolean isTesselationVisible() { return tessEntry.visible; }
    /** Sets tesselation visibility */
    public void setTesselationVisibile(boolean visibility) { tessEntry.visible = visibility; firePlottableStyleChanged(); }

    //
    // DRAW METHODS
    //

    /** y-axis bounds */
    transient RealInterval yInterval;

    @Override
    protected void recompute() {
        // get vertical bounds
        if (yInterval == null) {
            yInterval = (RealInterval) parent.requestDomain("y", Double.class);
            if (yInterval == null)
                throw new IllegalStateException("Unable to retrieve appropriate domain from parent class!");
            ((ChangeBroadcaster)yInterval).addChangeListener(this);
        }

        // update the directrix
        double curDir = frontier.getMaxDirectrix();
        dxEntry.setLocal(new Point2D.Double[]{ 
            new Point2D.Double(curDir, yInterval.getMinimum()),
            new Point2D.Double(curDir, yInterval.getMaximum())
        });

        // recompute the voronoi frontier tesselation
        if (!dxEntry.visible) {
            frontier.setMaxDirectrix(Double.POSITIVE_INFINITY);
            frontier.calculate();
            frontier.setMaxDirectrix(curDir);
        } else {
            frontier.calculate();
        }

        // create the mesh associated with the tesselation
        Tesselation tess = frontier.getTesselation();
        List<Point2D.Double> vv = tess.getVertices();
        Point2D.Double[] points = vv.toArray(new Point2D.Double[]{});
        int[][] edges = new int[tess.getEdges().size()][2];
        int pos = 0;
        for (Tesselation.Edge e : tess.getEdges()) {
            edges[pos][0] = vv.indexOf(e.v1);
            edges[pos][1] = vv.indexOf(e.v2);
            pos++;
        }
        ArrayList<int[]> areas = new ArrayList<int[]>();
        for (Tesselation.Polygon p : tess.getPolygons()) {
            Point2D.Double[] pp = p.getVertices();
            int[] summand = new int[pp.length];
            for (int i = 0; i < pp.length; i++)
                summand[i] = vv.indexOf(pp[i]);
            areas.add(summand);
        }
        tessEntry.setLocal(new GraphicMesh<Point2D.Double>(points, edges, areas));

        // recompute frontier curve (only if directrix is visible)
        if (dxEntry.visible) {
            List<Point2D.Double> frontierCurve = new ArrayList<Point2D.Double>();
            for (VoronoiFrontier.FrontierArc arc : frontier) {
                double diff = (Double) parent.requestScreenSampleDomain("y", Double.class, 1f, DomainHint.REGULAR).getSampleDiff();
                double ly = Math.max(yInterval.getMinimum(), arc.lowerY(curDir));
                double uy = Math.min(yInterval.getMaximum(), arc.upperY(curDir));
                for (double y = ly; y <= uy; y += diff)
                    frontierCurve.add(new Point2D.Double(VoronoiUtils.getXOnParabolaOfGivenFocusAndDirectrix(y, arc.getPoint(), curDir), y));
            }
            frontierEntry.setLocal(frontierCurve.toArray(new Point2D.Double[]{}));
        }

        needsComputation = false;
    }



}
