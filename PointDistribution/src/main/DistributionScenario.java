/*
 * PackingScenario.java
 * Created Jan 26, 2010
 */

package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.bm.blaise.scio.algorithm.PolygonIntersectionUtils;
import org.bm.blaise.scio.algorithm.PolygonUtils;
import org.bm.blaise.scio.algorithm.voronoi.VoronoiFrontier;
import util.ChangeBroadcaster;
import util.DefaultChangeBroadcaster;

/**
 * Parameters and computation for the "point-packing scenario", in particular manages
 * a polygon, and a collection of points in that polygon, and automatically computes the
 * areas nearest each point. Also manages a tesselation that keeps track of the division
 * of the polygon into near-sets. Uses a Voronoi calculation to compute the nearest sets,
 * and intersects the result with the outer polygon.
 *
 * @author Elisha Peterson
 */
public class DistributionScenario implements DistributionScenarioInterface,
        ChangeBroadcaster {

    /** Used to keep track of change listeners. */
    protected DefaultChangeBroadcaster changer = new DefaultChangeBroadcaster(this);

    public void addChangeListener(ChangeListener l) { changer.addChangeListener(l); }
    public void removeChangeListener(ChangeListener l) { changer.removeChangeListener(l); }

    /** Notify interested listeners of an (unspecified) change in the plottable. */
    public void fireStateChanged() { changer.fireStateChanged(); }

    //
    // VARIABLES - SETUP
    //

    /** Points within the scenario. */
    Point2D.Double[] points;
    /** Stores the "movement" within the scenario (from a prior position. */
    Point2D.Double[] movement;
    /** Polygon representing the boundaries of the scenario. */
    Point2D.Double[] polygon;

    //
    // VARIABLES - COMPUTED
    //

    /** Stores the computed Voronoi tesselation, with adjacencies, etc. */
    transient VoronoiFrontier voronoi;
    /** Stores polygons adjacent to points. */
    transient LinkedHashMap<Point2D.Double, Point2D.Double[]> polygonMap;
    /** Stores the areas "owned" by various points in the scenario; maintained in same order as the points. */
    transient LinkedHashMap<Point2D.Double, Double> areas;
    /** Stores adjacencies among points in the scenario. */
    transient LinkedHashMap<Point2D.Double, Set<Point2D.Double>> adjacencyMap;
    /** Describes those points that are adjacent to boundary. */
    transient HashSet<Point2D.Double> boundaryPoints;

    /** Stores average area of polygons. */
    transient double areaAverage = -1.0;
    /** Stores max difference of area from mean (L-infinity norm) */
    transient double ellInfinity = -1.0;
    /** Stores sum of differences of areas from mean (L-1 norm) */
    transient double ellOne = -1.0;
    /** Stores sum of squares of differences of areas from mean (L-2 norm) */
    transient double ellTwo = -1.0;

    //
    // CONSTRUCTOR
    //

    final static Point2D.Double[] DEFAULT_POLY = new Point2D.Double[]{ new Point2(.25,0), new Point2(1.25,0), new Point2(2,1), new Point2(0,1.25) };

    public DistributionScenario() {
        this (DEFAULT_POLY,
            new Point2D.Double[]{ 
                new Point2(.25,.75), new Point2(.5,.25), new Point2(1.5,.75), new Point2(1, .75),
                new Point2(.6,.7), new Point2(.8, .9), new Point2(1,.5), new Point2(1.1, .25),
                new Point2(1.2,1)
        });
    }

    /** Construct scenario with polygon and specified number of points in the polygon */
    public DistributionScenario(Point2D.Double[] polygon, int nPts) {
        this.polygon = polygon;
        randomizeLocations(nPts);
        this.movement = new Point2D.Double[points.length];
        for (int i = 0; i < points.length; i++)
            movement[i] = new Point2D.Double();
        recompute();
    }

    /** Constructs the scenario. */
    public DistributionScenario(Point2D.Double[] polygon, Point2D.Double[] points) {
        this.polygon = polygon;
        this.points = points;
        this.movement = new Point2D.Double[points.length];
        for (int i = 0; i < points.length; i++)
            movement[i] = new Point2D.Double();
        recompute();
    }

    @Override public String toString() { return "Point distribution scenario"; }

    /**
     * Randomizes locations of points in the polygon
     * @param n number of points
     */
    void randomizeLocations(int n) {
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        for (Point2D.Double p : polygon) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }

        Point2D.Double[] pts = new Point2D.Double[n];
        for (int i = 0; i < pts.length; i++) {
            pts[i] = null;
            while (pts[i] == null) {
                pts[i] = new Point2D.Double(
                        minX + Math.random() * (maxX - minX),
                        minY + Math.random() * (maxY - minY)
                        );
                if (!PolygonUtils.inPolygon(pts[i], getDomain()))
                    pts[i] = null;
            }
        }
        setPoints(pts);
    }

    //
    // GETTERS & SETTERS
    //

    public Point2D.Double[] getDomain() {
        return polygon;
    }

    public void setDomain(Point2D.Double[] polygon) {
        this.polygon = polygon;
        recompute();
    }

    public Point2D.Double getDomain(int i) {
        return polygon[i];
    }

    public void setDomain(int i, Point2D.Double p) {
        polygon[i] = p;
        recompute();
    }

    public Point2D.Double[] getPoints() {
        return points;
    }

    public void setPoints(Point2D.Double[] points) {
        if (this.points != null && points.length == this.points.length)
            for (int i = 0; i < points.length; i++)
                movement[i] = new Point2D.Double(points[i].x - this.points[i].x, points[i].y - this.points[i].y);
        else
            this.movement = new Point2D.Double[points.length];
        this.points = points;
        recompute();
    }

    public Point2D.Double getPoints(int i) {
        return points[i];
    }

    public void setPoints(int i, Point2D.Double p) {
        movement[i] = new Point2D.Double(p.x - movement[i].x, p.y - movement[i].y);
        points[i] = p;
        recompute();
    }

    //
    // INTERFACE METHODS
    //

    public Point2D.Double[] lastMovement() {
        return movement;
    }

    public Point2D.Double lastMovement(int i) {
        return movement[i];
    }

    public double meanArea() {
        return areaAverage;
    }

    public double maxAreaDeviation() {
        return ellInfinity;
    }

    public double sumDeviation() {
        return ellOne;
    }

    public double sumSquaredDeviation() {
        return ellTwo;
    }

    public Double cellArea(Point2D.Double point) {
        return areas.get(point);
    }

    public Point2D.Double[] cellBoundary(Point2D.Double point) {
        return polygonMap.get(point);
    }

    public Set<Point2D.Double> neighbors(Point2D.Double point) {
        return adjacencyMap.get(point);
    }

    public boolean isBoundaryPoint(Point2D.Double point) {
        return boundaryPoints.contains(point);
    }


    //
    // CALCULATION
    //

    public boolean computing = false;

    /** Recomputes the Voronoi tesselation, the adjacencies, and the areas. */
    void recompute() {
        if (computing) {
            System.out.println("Attempting to recompute while already computing...");
            return;
        }
        computing = true;
        try {
            // convert points to list, and compute Voronoi tesselation
            List<Point2D.Double> aPoints = new ArrayList<Point2D.Double>();
            for (int i = 0; i < points.length; i++) { aPoints.add(points[i]); }
            voronoi = new VoronoiFrontier(aPoints);

            // create the polygon map and compute the areas
            if (polygonMap == null) {
                polygonMap = new LinkedHashMap<Point2D.Double, Point2D.Double[]>();
                areas = new LinkedHashMap<Point2D.Double, Double>();
                adjacencyMap = new LinkedHashMap<Point2D.Double, Set<Point2D.Double>>();
                boundaryPoints = new HashSet<Point2D.Double>();
            } else {
                polygonMap.clear();
                areas.clear();
                adjacencyMap.clear();
                boundaryPoints.clear();
            }

            // clip polygons & compute total area
            double totalArea = 0.0;
            double curArea;
            for (Point2D.Double p : points) {
                Point2D.Double[] ptPoly = voronoi.getPolygonMap().get(p).getVertices();
                Point2D.Double[] pClip = PolygonIntersectionUtils.intersectionOfConvexPolygons(ptPoly, polygon);
                if (pClip != ptPoly)
                    boundaryPoints.add(p);
                polygonMap.put(p, pClip);
                curArea = PolygonUtils.areaOf(pClip);
                areas.put(p, curArea);
                totalArea += curArea;
            }
            areaAverage = totalArea / points.length;

            // variance of areas
            ellInfinity = 0.0;
            ellOne = 0.0;
            ellTwo = 0.0;
            for (Point2D.Double p : points) {
                ellInfinity = Math.max(ellInfinity, Math.abs(areas.get(p) - areaAverage));
                ellOne += Math.abs(areas.get(p) - areaAverage);
                ellTwo += (areas.get(p) - areaAverage) * (areas.get(p) - areaAverage);
            }

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
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
            computing = false;
        }
        computing = false;
        fireStateChanged();
    }
}
