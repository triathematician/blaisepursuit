/*
 * DistributionAlgorithm.java
 * Created Jan 28, 2010
 */

package main;

import java.awt.geom.Point2D;

/**
 * This class describes the method required for a point-distribution algorithm.
 *
 * @author Elisha Peterson
 */
public interface DistributionAlgorithm {

    /**
     * This method uses a supplied distribution scenario to compute new locations of points, which
     * should be returned as an ordered array in the same order as the scenario's points.
     * @param scenario the distribution scenario, in which points and the polygon have already been specified
     * @return an array of points representing the new positions of the points in the scenario
     */
    public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario);

}
