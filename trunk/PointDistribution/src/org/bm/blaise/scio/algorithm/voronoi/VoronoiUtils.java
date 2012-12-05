/**
 * VoronoiUtils.java
 * Created on Dec 15, 2009
 */

package org.bm.blaise.scio.algorithm.voronoi;

import java.awt.geom.Point2D;
import java.util.Comparator;
import scio.coordinate.utils.BasicMathUtils;
import scio.coordinate.geometry.PlanarGeometryUtils;

/**
 * <p>
 *    This class contains static utility methods for use in the Voronoi algorithm.
 * </p>
 * @author Elisha Peterson
 */
public class VoronoiUtils {

    /**
     * Finds vector perpendicular to vector p1->p2, found by rotating that vector
     * to the right by 90 degrees.
     * @param p1 first point
     * @param p2 second point
     * @return vector orthog to p1->p2
     */
    public static Point2D.Double perpSlope(Point2D.Double p1, Point2D.Double p2) {
        return new Point2D.Double(p1.y-p2.y, p2.x-p1.x);
    }

    /**
     * Implements the quadratic formula, for the root of the equation a*x^2+b*x+c=0,
     * where the choice of root is determined by <code>plus</code>
     * @param positiveRoot if true, returns positive root, else returns negative root
     * @return a root of the quadratic, or <code>NaN</code> if the result is imaginary.
     */
    public static double quadraticRoot(double a, double b, double c, boolean positiveRoot) {
        return BasicMathUtils.quadraticRoots(a, b, c)[positiveRoot ? 1 : 0];
    }

    /**
     * Evaluates parabola at given y-value, where the parabola is specified
     * by a point and a directrix.
     * @param input the value to evaluate at
     * @param focus the focus of the parabola
     * @param directrix the directrix of the parabola
     * @return the x-value of the parabola at specified input
     */
    public static double getXOnParabolaOfGivenFocusAndDirectrix(double input, Point2D.Double focus, double directrix) {
        if (focus.x == directrix) 
            return directrix;
        double[] coeffs = PlanarGeometryUtils.parabolaByFocusAndDirectrix(focus, directrix);
        return coeffs[0]*input*input + coeffs[1]*input + coeffs[2];
//        return .5 * ((input-focus.y)*(input-focus.y) / (focus.x-directrix) + focus.x + directrix);
    }

    /** Sorts points by angle relative to central point. */
    static class VertexEdgeComparator implements Comparator<Point2D.Double> {
        Point2D.Double n;
        public VertexEdgeComparator(Point2D.Double n) { this.n = n; }
        public int compare(Point2D.Double o1, Point2D.Double o2) {
            double angle1 = Double.isInfinite(o1.x) ? o1.y : Math.atan2(o1.y - n.y, o1.x - n.x);
            double angle2 = Double.isInfinite(o2.x) ? o2.y : Math.atan2(o2.y - n.y, o2.x - n.x);
            return angle1 > angle2 ? 1 : angle1==angle2 ? 0 : -1;
        }
    }
}
