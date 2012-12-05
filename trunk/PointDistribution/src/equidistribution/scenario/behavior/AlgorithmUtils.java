/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package equidistribution.scenario.behavior;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import scio.coordinate.utils.PlanarMathUtils;

/**
 *
 * @author elisha
 */
public class AlgorithmUtils {

    /** @return unit vector pointing from one vector to another */
    public static Point2D.Double unitVector(Point2D.Double from, Point2D.Double to) {
        return PlanarMathUtils.normalize(new Point2D.Double(to.x - from.x, to.y - from.y));
    }

    /**
     * @return vector pointing from one vector to another, scaled to specified length;
     *      returns zero vector if from/to points are equal
     */
    public static Point2D.Double scaledUnitVector(double length, Point2D.Double from, Point2D.Double to) {
        Point2D.Double dir = new Point2D.Double(to.x - from.x, to.y - from.y);
        double magn = dir.distance(0, 0);
        if (magn == 0)
            return new Point2D.Double();
        dir.x *= length/magn;
        dir.y *= length/magn;
        return dir;
    }

    /** @return closest point in a set of points to the given center. */
    public static Point2D.Double closestNeighbor(Point2D.Double center, Set pts) {
        TreeSet<Point2D.Double> sorted = new TreeSet<Point2D.Double>(new PointDistanceSorter(center));
        sorted.addAll(pts);
        return sorted.first();
    }

    /** @return farthest points in a set of points to the given center. */
    public static Point2D.Double farthestNeighbor(Point2D.Double center, Set pts) {
        TreeSet<Point2D.Double> sorted = new TreeSet<Point2D.Double>(new PointDistanceSorter(center));
        sorted.addAll(pts);
        return sorted.last();
    }

    //
    // INNER CLASSES
    //

    /** Sorts a set of points based on distance from a central point. */
    static class PointDistanceSorter implements Comparator<Point2D.Double> {
        Point2D.Double center;
        public PointDistanceSorter(Point2D.Double center) {
            this.center = center;
        }
        public int compare(Point2D.Double a, Point2D.Double b) {
            return Double.compare(a.distanceSq(center), b.distanceSq(center));
        }
    }
}
