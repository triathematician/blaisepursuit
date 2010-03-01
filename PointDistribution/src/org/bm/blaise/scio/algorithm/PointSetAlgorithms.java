/**
 * PointsInPlane.java
 * Created on Jun 3, 2008
 */
package org.bm.blaise.scio.algorithm;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.bm.blaise.scio.algorithm.voronoi.VoronoiFrontier;
import scio.coordinate.utils.PlanarMathUtils;

/**
 * Library of computational geometry algorithms relating to points in the plane.
 * 
 * @author Elisha Peterson
 */
public class PointSetAlgorithms {

    static final boolean VERBOSE = true;

    /** 
     * Returns convex hull of a given set of points. Uses the <i>Graham scan</i> method.
     * @param points an array of points
     * @return an array of points, representing the convex hull of the provided set
     */
    public static Double[] convexHull(Double[] points) {
        List<Double> aPoints = new ArrayList<Double>();         // convert to array
        for (int i = 0; i < points.length; i++) { aPoints.add(points[i]); }        
        Double pivot = Collections.min(aPoints, YCOMPARE);              // STEP 1: choose pivot to be element with minimum y-value
        aPoints.remove(pivot);        
        Collections.sort(aPoints, new CCWPivotCompare(pivot));                  // STEP 2: sort remaining points by pivot element
        List<Double> hull = new ArrayList<Double>();            // STEP 3: set up hull with first two elements
        hull.add(pivot);
        hull.add(aPoints.get(0));
        for (int i = 1; i < aPoints.size(); i++) {                              // STEP 4: add in remaining points
            while (hull.size() >= 2 && ccw(hull.get(hull.size()-2), hull.get(hull.size()-1), aPoints.get(i)) != 1) { hull.remove(hull.size() - 1); }
            hull.add(aPoints.get(i));
        }
        return hull.toArray(new Double[]{});
    }

    /**
     * Computes the Voronoi tesselation of the plane corresponding to the provided set of points.
     * @param points an array of points
     * @return a tesselation corresponding to the provided array
     */
    public static List<Double[]> voronoiFortune(Double[] points) {
        List<Double> aPoints = new ArrayList<Double>();         // convert to list
        for (int i = 0; i < points.length; i++) { aPoints.add(points[i]); }
        return new VoronoiFrontier(aPoints).getAdjacencyList();
    }



    //
    //
    // STATIC HELPER METHODS and COMPARATORS
    //
    //

    /**
     * Uses determinant of three points to compute relative orientations, in particular
     * if in passing from p1 to p2 to p3 a "left" or "counterclockwise" turn is made
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     * @return +1 if a CCW turn is made, 0 if colinear, -1 if a CW turn is made
     */
    public static int ccw(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        return (int) Math.signum((p2.x - p1.x)*(p3.y - p1.y) - (p2.y - p1.y)*(p3.x - p1.x));
    }

    /** Comparator for sorting points by x value; if x values equal, sorts on y value. */
    public final static Comparator<Point2D.Double> XCOMPARE = new Comparator<Point2D.Double>() {
        public int compare(Point2D.Double p1, Point2D.Double p2) {
            int xCompare = java.lang.Double.compare(p1.x, p2.x);
            if (xCompare != 0) { return xCompare; }
            return java.lang.Double.compare(p1.y, p2.y);
        }
    };

    /** Comparator for sorting points by y value; if y values equal, sorts on x value. */
    public final static Comparator<Point2D.Double> YCOMPARE = new Comparator<Point2D.Double>(){
        public int compare(Point2D.Double p1, Point2D.Double p2) {
            int xCompare = java.lang.Double.compare(p1.y, p2.y);
            if (xCompare != 0) { return xCompare; }
            return java.lang.Double.compare(p1.x, p2.x);
        }
    };

    /** Comparator for sorting points (by angle) relative to a specified pivot point. */
    public static class CCWPivotCompare implements Comparator<Point2D.Double> {
        Point2D.Double pivot;
        public CCWPivotCompare(Point2D.Double pivot) { this.pivot = pivot; }
        public int compare(Point2D.Double p1, Point2D.Double p2) {
            return java.lang.Double.compare(
                    PlanarMathUtils.angle(new Point2D.Double(p1.x - pivot.x, p1.y - pivot.y)),
                    PlanarMathUtils.angle(new Point2D.Double(p2.x - pivot.x, p2.y - pivot.y)));
        }
    }
}
