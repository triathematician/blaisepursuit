/**
 * MetricUtils.java
 * Created on Dec 7, 2009
 */

package org.bm.blaise.scio.algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import scio.coordinate.utils.BasicMathUtils;
import scio.coordinate.utils.PlanarMathUtils;

/**
 * <p>
 *    This class provides utilities used to compute various metrics for packing scenarios.
 * </p>
 * @author Elisha Peterson
 */
public class MetricUtils {
    

    //
    // BASIC DISTANCE CALCULATIONS
    //
    
    /**
     * Returns the distance from a point to a line segment.
     * @param point the point
     * @param segment1 first point on segment
     * @param segment2 second point on segment
     * @return distance from a point to the segment
     */
    public static double distanceToSegment(Point2D.Double point, Point2D.Double segment1, Point2D.Double segment2) {
        return point.distance(PlanarMathUtils.closestPointOnSegment(point, segment1, segment2));
    }

    /** Compute the max distance from a point to a polygonal object (must occur at a vertex). */
    public static double maxDistanceToPolygon(Point2D.Double point, Point2D.Double[] polygon) {
        Point2D.Double bestPoint = null;
        double bestDist = 0;
        for (int i = 0; i < polygon.length; i++) {
            double dist = polygon[i].distance(point);
            if (dist > bestDist) {
                bestPoint = polygon[i];
                bestDist = dist;
            }
        }
        return bestDist;
    }

    /** Compute the min distance from a point to a polygonal object (must occur at a vertex). */
    public static double minDistanceToPolygon(Point2D.Double point, Point2D.Double[] polygon) {
        Point2D.Double bestPoint = null;
        double bestDist = Double.MAX_VALUE;
        for (int i = 0; i < polygon.length; i++) {
            double dist = distanceToSegment(point, polygon[i], polygon[(i+1)%polygon.length]);
            if (dist < bestDist) {
                bestPoint = polygon[i];
                bestDist = dist;
            }
        }
        return bestDist;
    }

    //
    // MORE GENERAL DISTANCE CALCULATIONS
    //


}
