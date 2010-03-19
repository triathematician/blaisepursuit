/*
 * PackingScenarioInterface.java
 * Created Jan 26, 2010
 */

package main;

import java.awt.geom.Point2D;
import java.util.Set;

/**
 * This interface describes the primary methods used to access the <code>PackingScenario</code> class,
 * and is an attempt at an API for working with the computations. The <i>"points"</i> in the scenario
 * are the points that are to be evenly spaced, and the <i>"boundary polygon"</i> is the outside region
 * of the scenario. Whenever the points are changed using the <code>setPoints</code> method, or the boundary
 * is changed using the <code>setBoundaryPolygon</code> method, the resulting closest-point polygons
 * should be recomputed, and then may be retrieved using the appropriate methods below.
 * <br><br>
 * <b>NOTE:</b> Present algorithms assume that the boundary polygon is convex. Otherwise the behavior
 * is unspecified.
 *
 * @author Elisha Peterson
 * @see java.awt.geom.Point2D.Double
 */
public interface DistributionScenarioInterface {

    
    //
    // SETUP METHODS
    //

    /**
     * Returns the points supplied to the scenario.
     * @return set of points comprising the points within the scenario
     */
    public Point2D.Double[] getPoints();

    /**
     * Sets up the scenario to the specified collection of points. Should also recompute results.
     * @param points the list of points as an array
     */
    public void setPoints(Point2D.Double[] points);

    /**
     * Returns the i'th point supplied to the scenario.
     * @param i the index of the point
     * @return the i'th point
     */
    public Point2D.Double getPoints(int i);

    /**
     * Changes the i'th point
     * @param i the index of the point
     * @param p the new point value
     */
    public void setPoints(int i, Point2D.Double p);

    /**
     * Returns the boundary polygon supplied to the scenario
     * @return set of points comprising the boundary polygon in the scenario
     */
    public Point2D.Double[] getBoundaryPolygon();

    /**
     * Sets up the scenario with the specified boundary polygon. Should also recompute results.
     * @param polygon the boundary polygon as an array of <i>consecutive</i> vertices
     */
    public void setBoundaryPolygon(Point2D.Double[] polygon);

    /**
     * Returns the i'th coordinate of the boundary polygon
     * @param i the index of the coordinate
     * @return the i'th coordinate
     */
    public Point2D.Double getBoundaryPolygon(int i);

    /**
     * Changes the i'th coordinate of the boundary polygon
     * @param i the index of the coordinate
     * @param p the new coordinate
     */
    public void setBoundaryPolygon(int i, Point2D.Double p);


    //
    // MEMORY METHODS
    //

    /**
     * Returns the movement at the last step of the scenario (may not be supported always)
     * @return set of points comprising the last movement within the scenario
     */
    public Point2D.Double[] getLastMovement();

    /**
     * Returns the movement at the last step of the scenario (may not be supported always)
     * @param i the index of the point
     * @return point comprising the last movement within the scenario
     */
    public Point2D.Double getLastMovement(int i);


    //
    // CALCULATED RESULTS METHODS
    //

    /**
     * Returns the polygon comprising the set of points closer to the given point than any other, as an array of points.
     * @param point a reference to one of the points
     * @return an array of points describing the boundary of the polygon adjacent to the given point
     */
    public Point2D.Double[] getNearestPolygon(Point2D.Double point);

    /**
     * Returns the area of the polygon associated with given point.
     * @param point a reference to one of the points
     * @return the area of the set of points closer to the provided point than any other
     */
    public Double getArea(Point2D.Double point);


    //
    // ADJACENCY CONFIGURATION METHODS
    //

    /**
     * Returns a collection of points which are the neighbors of provided point in the resulting tesselation.
     * @param point a reference to one of the points
     * @return a collection of points that are adjacent in the current tesselation
     */
    public Set<Point2D.Double> pointsAdjacentTo(Point2D.Double point);

    /**
     * Returns true if specified point is adjacent to the boundary in a simulation.
     * @param point a reference to one of the points
     * @return true if the point is adjacent to the boundary
     */
    public boolean isAdjacentToBoundary(Point2D.Double point);

    //
    // STATISTICAL METHODS
    //

    /**
     * Returns average area of polygons in nearest-point tesselation (L0-norm)
     * @return the average area
     */
    public double getAreaAverage();

    /**
     * Returns the max difference from the area to the mean (L1-norm)
     * @return the maximum distance from an area to the mean
     */
    public double getAreaMaxDifference();

    /**
     * Returns the sum of difference from the area to the mean (L2-norm)
     * @return sum distances from areas to the mean
     */
    public double getAreaSumDifference();

    /**
     * Returns the sum of squareas of difference from the area to the mean (L2-norm)
     * @return sum squares distances from areas to the mean
     */
    public double getAreaSumSquareDifference();
    
}
