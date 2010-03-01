/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bm.blaise.scio.algorithm;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import scio.coordinate.utils.BasicMathUtils;
import scio.coordinate.utils.PlanarMathUtils;

/**
 * Various utilities for computations involving points and polygons.
 * 
 * @author ae3263
 */
public class PolygonUtils {

    static final boolean VERBOSE = false;

    /**
     * Computes the area of a polygon, using a standard coordinate geometry formula.
     * @param polygon the polygon, as a list of points
     * @return the area of a polygon, or 0 if either the polygon has fewer than 3 vertices or is null
     */
    public static double area(Point2D.Double[] polygon) {
        if (polygon == null || polygon.length <= 2)
            return 0;
        double sum = 0;
        int i2;
        for (int i = 0; i < polygon.length; i++) {
            i2 = (i+1) % polygon.length;
            sum += polygon[i].x * polygon[i2].y - polygon[i].y * polygon[i2].x;
        }
        return Math.abs(sum/2);
    }

    /**
     * Tests to see if a polygon contains a point; uses a winding number formula. Allows for "points at infinity" in the polgyon.
     * @param point the point to check
     * @param polygon the polygon that may contain the point
     * @return true if the polygon contains the point, or false if it does not (or is null)
     */
    public static boolean inPolygon(Point2D.Double point, Point2D.Double[] polygon) {
        if (polygon == null || polygon.length <= 2 || PlanarMathUtils.isInfinite(point))
            return false;
        double angleSum = 0;
        int i2;
        double angle;
        for (int i = 0; i < polygon.length; i++) {
            i2 = (i+1) % polygon.length;
            angle = PlanarMathUtils.positiveAngleBetween3Points(polygon[i], point, polygon[i2]);
            angleSum += angle > Math.PI ? angle - 2*Math.PI : angle;
        }
        boolean result = Math.abs(angleSum) > .1;
//        System.out.println("Found that " + point + (result ? " is in " : " is not in ") + "the polygon " + Arrays.toString(polygon));
        return result;
    }

    /**
     * Finds point of intersection between two line segments (p0--p1 and p2--p3).
     * Permits points at infinity, whose form is (Infinity, angle)... this leads to a ray intersection method
     * @param result pointer for the resulting point of intersecction; will be null if there is no intersection
     * @param s1a,s1b comprise first segment
     * @param s2a,s2b comprise second segment
     * @return time (relative to first segment) at which intersection occurs; may be NaN if no intersection
     */
    public static double intersectSegments(Point2D.Double result, Point2D.Double s1a, Point2D.Double s1b, Point2D.Double s2a, Point2D.Double s2b) {
        if (result == null) result = new Point2D.Double();
        if ((Double.isInfinite(s1a.x) && Double.isInfinite(s1b.x)) || (Double.isInfinite(s2a.x) && Double.isInfinite(s2b.x))) {
            // a "segment" at infinity has no intersections
            return Double.NaN;
        } else if (Double.isInfinite(s1b.x)) {
            // reorganize inputs
            return intersectSegments(result, s1b, s1a, s2a, s2b);
        } else if (Double.isInfinite(s2b.x)) {
            // reorganize inputs
            return intersectSegments(result, s1a, s1b, s2b, s2a);
        } else if (Double.isInfinite(s1a.x) && Double.isInfinite(s2a.x)) {
            // 2 rays intersecting
            return intersectRays(result, s1b, s1a.y, s2b, s2a.y);
        } else if (Double.isInfinite(s1a.x)) {
            // ray and segment intersecting; request time relative to ray
            return intersectSegmentRay(result, s2a, s2b, s1b, s1a.y, false);
            // ray and segment intersecting
        } else if (Double.isInfinite(s2a.x)) {
            return intersectSegmentRay(result, s1a, s1b, s2b, s2a.y, true);
        }
        // computes coordinates of intersection of segments
        double t[] = BasicMathUtils.solveLinear(
                new double[]{s1b.x-s1a.x, s2a.x-s2b.x, s1a.x-s2a.x},
                new double[]{s1b.y-s1a.y, s2a.y-s2b.y, s1a.y-s2a.y});
        // must have both between 0 and 1 for intersection
        if (t==null || t[0]<0 || t[0]>1 || t[1]<0 || t[1] > 1) {
            return Double.NaN;
        }
        result.x = s1a.x+t[0]*(s1b.x-s1a.x);
        result.y = s1a.y+t[0]*(s1b.y-s1a.y);
        return t[0];
    }

    public static double intersectSegmentRay(Point2D.Double result, Point2D.Double sa, Point2D.Double sb, Point2D.Double ray, double theta, boolean timeSegment) {
        if (result == null) result = new Point2D.Double();
        if (VERBOSE) System.out.print("Intersecting segment " + sa + " and " + sb + " with ray " + ray + " , " + theta);
        double t[] = BasicMathUtils.solveLinear(
                new double[]{sb.x-sa.x, -Math.cos(theta), sa.x-ray.x},
                new double[]{sb.y-sa.y, -Math.sin(theta), sa.y-ray.y});
        if (t==null || t[0]<0 || t[0]>1 || t[1]<0) {
            if (VERBOSE) System.out.println(" ... found no intersections.");
            return Double.NaN;
        }
        result.x = sa.x+t[0]*(sb.x-sa.x);
        result.y = sa.y+t[0]*(sb.y-sa.y);
        if (VERBOSE) System.out.println(" ... found " + result);
        return timeSegment ? t[0] : t[1];
    }

    public static double intersectRays(Point2D.Double result, Point2D.Double ray1, double theta1, Point2D.Double ray2, double theta2) {
        if (result == null) result = new Point2D.Double();
        double t[] = BasicMathUtils.solveLinear(
                new double[]{Math.cos(theta1), -Math.cos(theta2), ray1.x-ray2.x},
                new double[]{Math.sin(theta1), -Math.sin(theta2), ray1.y-ray2.y});
        if (t==null || t[0]<0 || t[1]<0) {
            return Double.NaN;
        }
        result.x = ray1.x+t[0]*Math.cos(theta1);
        result.y = ray1.y+t[0]*Math.sin(theta1);
        return t[0];
    }

    /**
     * Returns array of segments comprising the intersection between a polygon and a line segment.
     * @param segment an array of 2 points representing the segment
     * @param polygon an array of n points representing the polygon
     * @param adjacencies a pointer that will be replaced by the adjacencies among edges of intersection in the polygon; a -1 here will
     *   indicate one of the segment endpoints
     * @return an array of arrays of length 2 comprising the segments of the intersection
     */
    public static Point2D.Double[][] intersectionOf(Point2D.Double[] segment, Point2D.Double[] polygon, List<Integer[]> adjacencies) {
        if (segment == null || segment.length != 2) {
            throw new IllegalArgumentException("Segment should have 2 points!");
        }
        Point2D.Double s1a = segment[0];
        Point2D.Double s1b = segment[1];
        Point2D.Double s2a;
        Point2D.Double s2b;

        // sort points of intersection by their t-values; the keys are the time of intersection; the integers indicate edges of the polygon.
        TreeMap<Double, Integer> xMap = new TreeMap<Double, Integer>();
        if (inPolygon(s1a, polygon)) xMap.put(0.0, -1); // indicates that endpoints of segment are contained in the result
        if (inPolygon(s1b, polygon))
//        {
//            if (Double.isInfinite(s1a.x)) xMap.put(0.0, -1); else
            xMap.put(1.0, -1);
//        }
        double t = Double.NaN;
        Point2D.Double px = new Point2D.Double();
        for (int i = 0; i < polygon.length; i++) {
            s2a = polygon[i];
            s2b = polygon[(i+1)%polygon.length];
            t = intersectSegments(px, s1a, s1b, s2a, s2b);
            if (!Double.isNaN(t)) xMap.put(t, i);
        }
        if (VERBOSE) System.out.println(" ... found points of intersection: " + xMap);
        
        // add pairs of points to the result
        Point2D.Double[][] result = new Point2D.Double[xMap.size()/2][2];
        adjacencies.clear();
        Point2D.Double[] curSeg = new Point2D.Double[2];
        Integer[] curSegAdj = new Integer[2];
        int pos = 0;
        for (Entry<Double,Integer> x : xMap.entrySet()) {
            // account for rays as well as segments
            Point2D.Double pt = Double.isInfinite(s1a.x) ? new Point2D.Double(s1b.x+x.getKey()*Math.cos(s1a.y), s1b.y+x.getKey()*Math.sin(s1a.y))
                        : Double.isInfinite(s1b.x) ? new Point2D.Double(s1a.x+x.getKey()*Math.cos(s1b.y), s1a.y+x.getKey()*Math.sin(s1b.y))
                        : new Point2D.Double(s1a.x+x.getKey()*(s1b.x-s1a.x), s1a.y+x.getKey()*(s1b.y-s1a.y));
            if (curSeg[0] == null) {
                curSeg[0] = pt;
                curSegAdj[0] = x.getValue();
            } else {
                curSeg[1] = pt;
                result[pos] = curSeg;
                curSegAdj[1] = x.getValue();
                adjacencies.add(curSegAdj);
                pos++;
                curSeg = new Point2D.Double[2];
            }
        }
        if (VERBOSE) System.out.println(" ... returning " + Arrays.deepToString(result));
        if (VERBOSE) System.out.println(" ... adjacencies " + Arrays.deepToString(adjacencies.toArray(new Integer[][]{})));
        return result;
    }

    static Point2D.Double[] polySeg(Point2D.Double[] poly, int loc) {
        return new Point2D.Double[]{poly[loc], poly[(loc+1)%poly.length]};
    }

//    /**
//     * Starting at the current point in the polygon, looks for the next point of intersection with the CONVEX clipping polygon,
//     * in the natural ordering specified by the vertices.
//     * Stores this point in px, and returns the location of the first vertex of that edge in the clip polygon.
//     * @param polygon a polygon
//     * @param startPos first point position within the polygon
//     * @param clip a CONVEX polygon that intersects the given one
//     * @param px a pointer to a variable that will store the point of intersection
//     * @return an array {pPos, cPos}; if the next
//     * location of the first vertex along the edge of intersection in the polygon (first index) and the clipping polygon (second index),
//     *    or if the next intersection is a point that is contained in the polygon, then it returns {pPos, -1} where pPos is the location of that point,
//     *    or returns (-1,-1) if there are no intersections
//     */
//    static int[] locationOfNextIntersection(Point2D.Double[] polygon, int startPos, Point2D.Double[] clip, Point2D.Double px) {
//        px = null;
//        int cPos = -1;
//        int pPos = startPos;
//        do {
//            for (cPos = 0; cPos < clip.length; cPos++) {
//                px = intersectionOfSegments(polygon[pPos%polygon.length], polygon[(pPos+1)%polygon.length], clip[cPos%clip.length], clip[(cPos+1)%clip.length]);
//                if (px != null)
//                    return new int[] { pPos, cPos };
//            }
//            pPos = (pPos + 1) % polygon.length;
//
//            if (inPolygon(polygon[pPos%polygon.length], clip)) {
//                px = null;
//                return new int[] { pPos, -1 };
//            }
//        } while (pPos != startPos);
//        return new int[] { -1, -1 };
//    }
//
//    /**
//     * Returns clipped version of a polygon. Assumes both polygons are convex and positively-oriented,
//     * so that the interior of the polygon is to the left when the vertices are listed in order.
//     * @param polygon the polygon to clip (should be convex)
//     * @param clip the region used to clip (as a convex polygon also)
//     * @return the clipped version of the polygon
//     */
//    public static Point2D.Double[] clipPolygon(Point2D.Double[] polygon, Point2D.Double[] clip) {
//        if (polygon.length == 0 || clip.length == 0)
//            return new Point2D.Double[]{};
//
//        // state variables
//        int pPos = 0; // location on polygon
//        int cPos = 0; // location on clipper
//        boolean onPoly = true; // current edge traversal location
//        ArrayList<Point2D.Double> result = new ArrayList<Point2D.Double>();
//
//        if (inPolygon(polygon[0], clip))
//            result.add(polygon[0]);
//        while (pPos < polygon.length) {
//            Point2D.Double px = null; // stores local point of intersection
//            int[] nextLoc = onPoly
//                ? locationOfNextIntersection(polygon, pPos, clip, px)
//                : locationOfNextIntersection(polygon, cPos, clip, px); // finds next position of next vertex in clip OR intersection
//            if (nextLoc[0] == -1) { // no intersections found
//                System.out.println("No more intersections found.");
//                break;
//            } else if (nextLoc[1] == -1) { // found a point inside the region
//                result.add(onPoly ? polygon[nextLoc[0]] : clip[nextLoc[0]]);
//                if (onPoly) pPos++; else cPos++;
//            } else {
//                result.add(px);
//                onPoly = !onPoly;
//            }
//        }
//
//        return result.toArray(new Point2D.Double[]{});
//    }
}
