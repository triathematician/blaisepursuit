/**
 * PlaneTesselation.java
 * Created on Dec 15, 2009
 */

package org.bm.blaise.specto.plane.compgeom;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Map;
import org.bm.blaise.scio.algorithm.Tesselation;
import org.bm.blaise.scio.algorithm.Tesselation.Edge;
import org.bm.blaise.scio.algorithm.Tesselation.Polygon;
import org.bm.blaise.specto.primitive.BlaisePalette;
import org.bm.blaise.specto.primitive.PathStyle;
import org.bm.blaise.specto.primitive.PointStyle;
import org.bm.blaise.specto.primitive.PointStyle.PointShape;
import org.bm.blaise.specto.primitive.ShapeStyle;
import org.bm.blaise.specto.visometry.AbstractPlottable;
import org.bm.blaise.specto.visometry.VisometryGraphics;

/**
 * <p>
 *    This class draws a tesselation on the plane.
 * </p>
 * @author Elisha Peterson
 */
public class PlaneTesselation extends AbstractPlottable<Point2D.Double> {

    /** The tesselation. */
    Tesselation tess;

    /** Used to draw vertices. */
    PointStyle vertexStyle = new PointStyle();

    /** Used to draw edges. */
    PathStyle edgeStyle = new PathStyle(BlaisePalette.STANDARD.func1());

    /** Assigns colors to interior polygons. */
    Map<Polygon, Color> colorMap;
    /** Used to draw interior elements. */
    ShapeStyle interiorStyle = new ShapeStyle();

    //
    // CONSTRUCTOR
    //

    public PlaneTesselation(Tesselation tess) {
        this.tess = tess;
        vertexStyle.setRadius(3);
        vertexStyle.setStrokeColor(BlaisePalette.STANDARD.func1());
        vertexStyle.setShape(PointShape.CIRCLE);
        interiorStyle.setFillOpacity(0.2f);
        interiorStyle.setStroke(null);
    }

    //
    // GETTERS & SETTERS
    //

    public Tesselation getTesselation() {
        return tess;
    }

    public void setTesselation(Tesselation tess) {
        this.tess = tess;
    }
    
    //
    // STYLE GETTERS & SETTERS
    //

    public PathStyle getEdgeStyle() {
        return edgeStyle;
    }

    public void setEdgeStyle(PathStyle edgeStyle) {
        this.edgeStyle = edgeStyle;
    }

    public ShapeStyle getInteriorStyle() {
        return interiorStyle;
    }

    public void setInteriorStyle(ShapeStyle interiorStyle) {
        this.interiorStyle = interiorStyle;
    }

    public PointStyle getVertexStyle() {
        return vertexStyle;
    }

    public void setVertexStyle(PointStyle vertexStyle) {
        this.vertexStyle = vertexStyle;
    }

    public Map<Polygon, Color> getColorMap() {
        return colorMap;
    }

    public void setColorMap(Map<Polygon, Color> colorMap) {
        this.colorMap = colorMap;
    }

    /** Cycles through colors. */
    protected static Color nextColor(Color c, int cr, int cg, int cb) {
        return new Color((c.getRed()+cr) % 255, (c.getGreen()+cg)%255, (c.getBlue()+cb) % 255);
    }

    //
    // DRAW METHODS
    //

    @Override
    public void paintComponent(VisometryGraphics<Point2D.Double> vg) {
        vg.setShapeStyle(interiorStyle);
        Color is = interiorStyle.getFillColor();
        Color c = is;
        for (Polygon p : tess.getPolygons()) {
            if (colorMap != null) {
                c = colorMap.get(p);
                if (c != null)
                    interiorStyle.setFillColor(c);
            } else {
                interiorStyle.setFillColor(c);
                c = nextColor(c, 13, 83, 157);
            }
            vg.drawClosedPath(p.getVerticesAsArray());
        }
        interiorStyle.setFillColor(is);

        vg.setPathStyle(edgeStyle);
        for (Edge e : tess.getEdges()) {
            vg.drawSegment(e.v1, e.v2);
        }

        vg.setPointStyle(vertexStyle);
        for (Point2D.Double p : tess.getVertices()) {
            vg.drawPoint(p);
        }
    }
}
