/*
 * CellVis.java
 * Created May 21, 2010
 */

package org.bm.blaise.specto.plane.compgeom;

import java.awt.geom.Point2D;
import primitive.style.ShapeStyle;
import visometry.VPrimitiveEntry;
import visometry.plottable.Plottable;

/**
 * Displays a set of polygons, each given w/ unique colors.
 * @author Elisha Peterson
 */
public class CellVis extends Plottable<Point2D.Double> {

    VPrimitiveEntry entry;

    public CellVis(Point2D.Double[][] polys) {
        addPrimitive(entry = new VPrimitiveEntry(polys, new ColoredShapeStyle()));
    }

    public ShapeStyle getStyle() {
        return (ShapeStyle) entry.style;
    }

    public void setStyle(ShapeStyle style) { 
        entry.style = style; firePlottableStyleChanged();
    }

    public Point2D.Double[][] getPolys() {
        return (Point2D.Double[][]) entry.local;
    }

    public void setPolys(Point2D.Double[][] pp) { 
        entry.local = pp;
        entry.needsConversion = true;
        firePlottableChanged();
    }

}
