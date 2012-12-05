/**
 * ConvexHull.java
 * Created on Dec 7, 2009
 */

package org.bm.blaise.specto.plane.compgeom;

import java.awt.geom.Point2D;
import org.bm.blaise.scio.algorithm.PointSetAlgorithms;
import primitive.style.ShapeStyle;
import visometry.VPrimitiveEntry;
import visometry.plottable.VPointSet;

/**
 * <p>
 *    This class displays the convex hull of the provided list of points.
 *    The points are handled by an underlying set of points. Whenever that set
 *    changes, e.g. when the user moves a point around, the hull computation
 *    is done again.
 * </p>
 * @author Elisha Peterson
 */
public class PlaneConvexHull extends VPointSet<Point2D.Double> {

    /** Stores the hull outline */
    VPrimitiveEntry hullEntry;

    /** Construct with a provided list of double values. */
    public PlaneConvexHull(Point2D.Double[] values) {
        super(values);
        addPrimitive(hullEntry = new VPrimitiveEntry(null, new ShapeStyle()));
    }

    @Override
    protected void recompute() {
        hullEntry.setLocal(PointSetAlgorithms.convexHull((Point2D.Double[]) entry.local));
        needsComputation = false;
    }
    
}
