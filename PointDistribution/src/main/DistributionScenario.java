/*
 * PackingScenario.java
 * Created Jan 26, 2010
 */

package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bm.blaise.scio.algorithm.PolygonIntersectionUtils;
import org.bm.blaise.scio.algorithm.PolygonUtils;
import org.bm.blaise.scio.algorithm.voronoi.VoronoiFrontier;

/**
 * Parameters and computation for the "point-packing scenario", in particular manages
 * a polygon, and a collection of points in that polygon, and automatically computes the
 * areas nearest each point. Also manages a tesselation that keeps track of the division
 * of the polygon into near-sets. Uses a Voronoi calculation to compute the nearest sets,
 * and intersects the result with the outer polygon.
 *
 * @author Elisha Peterson
 */
public class DistributionScenario implements DistributionScenarioInterface {

    //
    // VARIABLES - SETUP
    //

    /** Points within the scenario. */
    Point2D.Double[] points;
    /** Polygon representing the boundaries of the scenario. */
    Point2D.Double[] polygon;

    //
    // VARIABLES - COMPUTED
    //

    /** Stores the computed Voronoi tesselation, with adjacencies, etc. */
    transient VoronoiFrontier voronoi;
    /** Stores polygons adjacent to points. */
    transient HashMap<Point2D.Double, Point2D.Double[]> polygonMap;
    /** Stores the areas "owned" by various points in the scenario; maintained in same order as the points. */
    transient HashMap<Point2D.Double, Double> areas;
    /** Stores adjacencies among points in the scenario. */
    transient HashMap<Point2D.Double, Set<Point2D.Double>> adjacencyMap;

    /** Stores average area of polygons. */
    transient double areaAverage = -1.0;
    /** Stores variance in area of polygons. */
    transient double areaVariance = -1.0;

    //
    // CONSTRUCTOR
    //

    public DistributionScenario() {
        this (new Point2D.Double[]{ new Point2(.25,0), new Point2(1.25,0), new Point2(2,1), new Point2(0,1.25) },
            new Point2D.Double[]{ 
                new Point2(.25,.75), new Point2(.5,.25), new Point2(1.5,.75), new Point2(1, .75),
                new Point2(.6,.7), new Point2(.8, .9), new Point2(1,.5), new Point2(1.1, .25),
                new Point2(1.2,1)
        });
    }

    /** Constructs the scenario. */
    public DistributionScenario(Point2D.Double[] polygon, Point2D.Double[] points) {
        this.polygon = polygon;
        this.points = points;
        recompute();
    }

    @Override public String toString() { return "Point distribution scenario"; }

    //
    // GETTERS & SETTERS
    //

    public Point2D.Double[] getBoundaryPolygon() {
        return polygon;
    }

    public void setBoundaryPolygon(Point2D.Double[] polygon) {
        this.polygon = polygon;
        recompute();
    }

    public Point2D.Double getBoundaryPolygon(int i) {
        return polygon[i];
    }

    public void setBoundaryPolygon(int i, Point2D.Double p) {
        polygon[i] = p;
        recompute();
    }

    public Point2D.Double[] getPoints() {
        return points;
    }

    public void setPoints(Point2D.Double[] points) {
        this.points = points;
        recompute();
    }

    public Point2D.Double getPoints(int i) {
        return points[i];
    }

    public void setPoints(int i, Point2D.Double p) {
        points[i] = p;
        recompute();
    }

    public double getAreaAverage() {
        return areaAverage;
    }

    public double getAreaVariance() {
        return areaVariance;
    }

    //
    // INTERFACE METHODS
    //

    public Double getArea(Point2D.Double point) {
        return areas.get(point);
    }

    public Point2D.Double[] getNearestPolygon(Point2D.Double point) {
        return polygonMap.get(point);
    }

    public Set<Point2D.Double> getPointsAdjacentTo(Point2D.Double point) {
        return adjacencyMap.get(point);
    }

    //
    // CALCULATION
    //

    /** Recomputes the Voronoi tesselation, the adjacencies, and the areas. */
    void recompute() {
        // convert points to list, and compute Voronoi tesselation
        List<Point2D.Double> aPoints = new ArrayList<Point2D.Double>();         
        for (int i = 0; i < points.length; i++) { aPoints.add(points[i]); }
        voronoi = new VoronoiFrontier(aPoints);

        // create the polygon map and compute the areas
        if (polygonMap == null) {
            polygonMap = new HashMap<Point2D.Double, Point2D.Double[]>();
            areas = new HashMap<Point2D.Double, Double>();
            adjacencyMap = new HashMap<Point2D.Double, Set<Point2D.Double>>();
        } else {
            polygonMap.clear();
            areas.clear();
            adjacencyMap.clear();
        }
        double total = 0.0;
        double cur;
        for (Point2D.Double p : points) {
            Point2D.Double[] pClip = PolygonIntersectionUtils.intersect(voronoi.getPolygonMap().get(p).getVerticesAsArray(), polygon);
            polygonMap.put(p, pClip);
            cur = PolygonUtils.area(pClip);
            areas.put(p, cur);
            total += cur;
        }
        areaAverage = total / points.length;
        // variance of areas
        total = 0.0;
        for (Point2D.Double p : points) {
            total += (areas.get(p)-areaAverage) * (areas.get(p)-areaAverage);
        }
        areaVariance = total / points.length;

        // create the adjacency map
        for (Point2D.Double[] adj : voronoi.getAdjacencyList()) {
            if (!adjacencyMap.containsKey(adj[0])) {
                adjacencyMap.put(adj[0], new HashSet<Point2D.Double>());
            }
            if (!adjacencyMap.containsKey(adj[1])) {
                adjacencyMap.put(adj[1], new HashSet<Point2D.Double>());
            }
            adjacencyMap.get(adj[0]).add(adj[1]);
            adjacencyMap.get(adj[1]).add(adj[0]);
        }
    }
}
