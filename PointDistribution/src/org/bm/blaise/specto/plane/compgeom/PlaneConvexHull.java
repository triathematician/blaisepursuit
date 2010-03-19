/**
 * ConvexHull.java
 * Created on Dec 7, 2009
 */

package org.bm.blaise.specto.plane.compgeom;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.bm.blaise.specto.plottable.VPolygon;
import org.bm.blaise.specto.visometry.VisometryGraphics;
import org.bm.blaise.scio.algorithm.PointSetAlgorithms;

/**
 * <p>
 *    This class displays the convex hull of the provided list of points. Points may
 *    be moved around dynamically while the hull is recomputed.
 * </p>
 * @author Elisha Peterson
 */
public class PlaneConvexHull extends VPolygon<Point2D.Double> {

    Point2D.Double[] hull;

    /** Construct with a provided list of double values. */
    public PlaneConvexHull(Point2D.Double[] values) {
        super(values);
        hull = PointSetAlgorithms.convexHull(values);
    }

    @Override
    protected void fireStateChanged() {
        hull = PointSetAlgorithms.convexHull(values);
        super.fireStateChanged();
    }

    @Override
    public void draw(VisometryGraphics<Double> vg) {
        vg.setPointStyle(pointStyle);
        vg.drawPoints(values);
        if (labelsVisible) {
            if (labelStyle != null)
                vg.setStringStyle(labelStyle);
            for (int i = 0; i < values.length; i++)
                vg.drawString(getValueString(i), values[i], 5, -5);
        }

        vg.drawShape(hull, shapeStyle);
        if (pointsVisible)
            vg.drawPoints(hull);
    }
}
