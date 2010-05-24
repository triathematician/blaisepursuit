/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bm.blaise.scio.algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import scio.coordinate.utils.PlanarMathUtils;

/**
 *
 * @author ae3263
 */
public class PolygonIntersectionUtils {

    private static final boolean VERBOSE = false;

    /**
     * Intersects two <b>CONVEX</b> polygons in the plane.
     * @param poly1 first polygon
     * @param poly2 second polygon
     * @return a new polygon representing the intersection, or one of the original polygons if
     *   it is contained entirely within another polygon
     */
    public static Point2D.Double[] intersectionOfConvexPolygons(Point2D.Double[] poly1, Point2D.Double[] poly2) {
        boolean inside = true;
        for (int i = 0; i < poly1.length; i++)
            if (!PolygonUtils.inPolygon(poly1[i], poly2)) {
                inside = false;
                break;
            }
        if (inside)
            return poly1;

        inside = true;
        for (int i = 0; i < poly2.length; i++)
            if (!PolygonUtils.inPolygon(poly2[i], poly1)) {
                inside = false;
                break;
            }
        if (inside)
            return poly2;

        if (VERBOSE) System.out.println("---------------");
        PolyGraph pg = PolyGraph.intersectionInstance(poly1, poly2);
        Point2D.Double v0 = null, v1 = null;
        for (Entry<Point2D.Double,List<Point2D.Double>> e : pg.adjs.entrySet())
            if (e.getValue().size() > 0) {
                v0 = e.getKey();
                v1 = e.getValue().get(0);
            }
        if (v0 == null) {
            return new Point2D.Double[]{};
        }
        Point2D.Double[] result;
        try {
            result = pg.traceLeft(v0, v1);
        } catch (Exception ex) {
//            System.out.println("Failed to find polygon intersection: " + ex);
//            System.out.println("... polygon 1 = " + Arrays.toString(poly1));
//            System.out.println("... polygon 2 = " + Arrays.toString(poly2));
//            System.out.println("... returning empty array");
            return new Point2D.Double[]{};
        }
        if (VERBOSE) System.out.println("  RESULT: " + Arrays.toString(result));
        return result;
    }

    /** Stores the graph used to trace out results. */
    static class PolyGraph {
        Map<Point2D.Double,List<Point2D.Double>> adjs;
        Point2D.Double[] poly1;
        Point2D.Double[] poly2;
        Point2D.Double[][] intTable;

        private PolyGraph(Point2D.Double[] poly1, Point2D.Double[] poly2) {
            adjs = new HashMap<Point2D.Double,List<Point2D.Double>>();
            intTable = new Point2D.Double[poly1.length][poly2.length];
            for (int i = 0; i < intTable.length; i++) {
                for (int j = 0; j < intTable[0].length; j++) {
                    intTable[i][j] = new Point2D.Double();
                    PolygonUtils.intersectSegments(intTable[i][j], poly1[i], poly1[(i+1)%poly1.length], poly2[j], poly2[(j+1)%poly2.length]);
                }
            }
            this.poly1 = poly1;
            this.poly2 = poly2;
        }

        private Point2D.Double getPoint(int i, int j) {
            if (j==-1) {
                return Double.isInfinite(poly1[i].x) ? poly1[(i+1)%poly1.length] : poly1[i];
            } else if (i==-1) {
                return Double.isInfinite(poly2[j].x) ? poly2[(j+1)%poly2.length] : poly2[j];
            } else {
                return intTable[i][j];
            }
        }

        private void addAdjacency(Point2D.Double p0, Point2D.Double p1) {
            if (VERBOSE) System.out.println("adding adjacency " + p0 + " and " + p1);
            if (adjs.containsKey(p0)) {
                adjs.get(p0).add(p1);
            } else {
                ArrayList<Point2D.Double> pp = new ArrayList<Point2D.Double>();
                pp.add(p1);
                adjs.put(p0, pp);
            }
            if (adjs.containsKey(p1)) {
                adjs.get(p1).add(p0);
            } else {
                ArrayList<Point2D.Double> pp = new ArrayList<Point2D.Double>();
                pp.add(p0);
                adjs.put(p1, pp);
            }
        }

        /** Returns instance of a PolyGraph representing the intersection of two polygons. */
        static PolyGraph intersectionInstance(Point2D.Double[] poly1, Point2D.Double[] poly2) {
            if (VERBOSE) System.out.println("Intersecting " + Arrays.toString(poly1) + " and " + Arrays.toString(poly2));
            PolyGraph result = new PolyGraph(poly1, poly2);
            List<Integer[]> segIntLoc = new ArrayList<Integer[]>();
            // add all intersections
            for (int i = 0; i < poly1.length; i++) {
                PolygonUtils.intersectionOf(PolygonUtils.polySeg(poly1, i), poly2, segIntLoc);
                for (Integer[] loc : segIntLoc) {
                    result.addAdjacency(result.getPoint(i, loc[0]), result.getPoint(loc[1]==-1?(i+1)%poly1.length:i, loc[1]));
                }
            }
            for (int i = 0; i < poly2.length; i++) {
                PolygonUtils.intersectionOf(PolygonUtils.polySeg(poly2, i), poly1, segIntLoc);
                for (Integer[] loc : segIntLoc) {
                    result.addAdjacency(result.getPoint(loc[0], i), result.getPoint(loc[1], loc[1]==-1?(i+1)%poly2.length:i));
                }
            }
            if (VERBOSE) System.out.println("Adjacencies (prelim) : " + result.adjs);
            // remove any vertices outside polygons
            HashSet<Point2D.Double> removeSet = new HashSet<Point2D.Double>();
            for (int i = 0; i < poly1.length; i++) {
                if (!PolygonUtils.inPolygon(poly1[i], poly2)) {
                    removeSet.add(poly1[i]);
                }
            }
            for (int i = 0; i < poly2.length; i++) {
                if (!PolygonUtils.inPolygon(poly2[i], poly1)) {
                    removeSet.add(poly2[i]);
                }
            }
            for (Point2D.Double p : removeSet) {
                result.adjs.remove(p);
            }
            for (List<Point2D.Double> pl : result.adjs.values()) {
                pl.removeAll(removeSet);
            }
            if (VERBOSE) System.out.println("             (final) : " + result.adjs);
            return result;
        }

        /** Finds the leftmost point of the inputs */
        Point2D.Double nextLeft( Point2D.Double last, Point2D.Double pt) {
            double bestAngle = 0;
            Point2D.Double bestPoint = null;
            double angle = 0;
            for (Point2D.Double p : adjs.get(pt)) {
                if (p==last) continue;
                angle = PlanarMathUtils.positiveAngleBetween3Points(last, pt, p);
                if (angle > bestAngle) {
                    bestAngle = angle;
                    bestPoint = p;
                }
            }
            return bestPoint;
        }

        /** Traces around the inside of the point graph, starting from the v0->v1 edge. */
        public Point2D.Double[] traceLeft(Point2D.Double v0, Point2D.Double v1) {
            ArrayList<Point2D.Double> result = new ArrayList<Point2D.Double>();
            result.add(v0);
            Point2D.Double last = v0;
            Point2D.Double cur = v0;
            Point2D.Double next = v1;
            while (next != v0) {
                result.add(next);
                last = cur;
                cur = next;
                next = nextLeft(last, cur);
                if (next == last || next == cur)
                    throw new IllegalStateException("Algorithm broken: traceLeft(" + v0 + ", " + v1 + ") at partial solution " + result);
            }
            return result.toArray(new Point2D.Double[]{});
        }
    }
}
