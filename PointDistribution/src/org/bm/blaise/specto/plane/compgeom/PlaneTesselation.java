/**
 * PlaneTesselation.java
 * Created on Dec 15, 2009
 */

package org.bm.blaise.specto.plane.compgeom;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bm.blaise.scio.algorithm.Tesselation;
import org.bm.blaise.scio.algorithm.Tesselation.Polygon;
import primitive.GraphicMesh;
import primitive.style.MeshStyle;
import visometry.VPrimitiveEntry;
import visometry.plottable.Plottable;

/**
 * Displays a tesselation on the plane. The tesselation must be stored in a <code>Tesselation</code>
 * format, which is provided to this plottable. The tesselation is displayed using a <code>MeshStyle</code>,
 * customized to display each area with a unique color. This requires converting the tesselation
 * to a <code>GraphicMesh</code>.
 *
 * @see GraphicMesh, MeshStyle, Tesselation
 *
 * @author Elisha Peterson
 */
public class PlaneTesselation extends Plottable<Point2D.Double> {

    /** The tesselation. */
    Tesselation tess;
    /** Primitive entry for the mesh. */
    VPrimitiveEntry meshEntry;

    /** Assigns colors to interior polygons. */
    Map<Polygon, Color> colorMap;

    //
    // CONSTRUCTOR
    //

    public PlaneTesselation(Tesselation tess) {
        this.tess = tess;
        MeshStyle style = new MeshStyle();
        style.getPointStyle().setRadius(3);
        style.getSegmentStyle().setStrokeColor(new Color(128, 32, 32));
        style.getAreaStyle().setFillOpacity(0.2f);
        style.getAreaStyle().setStroke(null);
        addPrimitive(meshEntry = new VPrimitiveEntry(null, style));
    }

    @Override public String toString() { return "Plane Tesselation"; }

    //
    // GETTERS & SETTERS
    //

    /** @return underlying tesselation */
    public Tesselation getTesselation() { return tess; }
    /** Sets tesselation */
    public void setTesselation(Tesselation tess) { this.tess = tess; firePlottableChanged(); }
    
    //
    // STYLE GETTERS & SETTERS
    //

    /** @return mesh style */
    public MeshStyle getMeshStyle() { return (MeshStyle) meshEntry.style; }
    /** Sets mesh style */
    public void setMeshStyle(MeshStyle style) { meshEntry.style = style; firePlottableStyleChanged(); }

    /** @return the map associating polygons in the tesselation to colors */
    public Map<Polygon, Color> getColorMap() { return colorMap; }
    /** Sets the map associating polygons in the tesselation to colors */
    public void setColorMap(Map<Polygon, Color> colorMap) { this.colorMap = colorMap; firePlottableStyleChanged(); }

    //
    // DRAW METHODS
    //

    /** Utility method... cycles through colors. */
    static Color nextColor(Color c, int cr, int cg, int cb) { return new Color((c.getRed()+cr) % 255, (c.getGreen()+cg)%255, (c.getBlue()+cb) % 255); }

    @Override
    protected void recompute() {
        List<Point2D.Double> vv = tess.getVertices();
        // need to create the mesh associated with the tesselation
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
        meshEntry.setLocal(new GraphicMesh<Point2D.Double>(points, edges, areas));
        needsComputation = false;
    }
}
