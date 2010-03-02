/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Arrays;
import scio.coordinate.utils.PlanarMathUtils;

/**
 * This class stores various algorithms used in the point distribution sim.
 * @author Elisha Peterson, ...
 */
public enum Algorithms
        implements DistributionScenarioAlgorithmInterface {

    /** Algorithm has each point go towards the neighbor with the greatest area. */
    Go_to_largest_area() {

        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            // these are the points in the old scenario
            final Point2D.Double[] oldPoints = scenario.getPoints();

            // here we initialize a new array for the new locations of the points
            Point2D.Double[] newPoints = new Point2D.Double[oldPoints.length];

            // this is the maximum change in distance from old position to new position
            double MAX_STEP_DISTANCE = .01;

            for (int i = 0; i < oldPoints.length; i++) {
                // here we iterate over all the neighbors of the current point to find the one with maximum area
                Point2D.Double bestNbr = null;
                double bestArea = 0.0;
                for (Point2D.Double nbr : scenario.getPointsAdjacentTo(oldPoints[i])) {
                    double area = scenario.getArea(nbr);
                    if (area > bestArea) {
                        bestNbr = nbr;
                        bestArea = area;
                    }
                }
                // don't move if already have largest area
                if (scenario.getArea(oldPoints[i]) > bestArea)
                    newPoints[i] = (Double) oldPoints[i].clone();
                else {
                    // this is a unit vector pointing from oldPoints[i] to bestNbr
                    Point2D.Double unitChange = unitVector(oldPoints[i], bestNbr);
                    // this is the new location
                    newPoints[i] = new Point2D.Double(
                            oldPoints[i].x + MAX_STEP_DISTANCE * unitChange.x,
                            oldPoints[i].y + MAX_STEP_DISTANCE * unitChange.y);
                }
            }

            return newPoints;
        }
    },
    /** Placeholder for next algorithm */
    Go_to_area_weighted_by_difference() {

        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            final Point2D.Double[] oldPoints = scenario.getPoints();
            Point2D.Double[] newPoints = new Point2D.Double[oldPoints.length];
            double SCALE_CHANGE = 2;

            for (int i = 0; i < oldPoints.length; i++) {
                Point2D.Double dir = new Point2D.Double();
                double area = scenario.getArea(oldPoints[i]);
                for (Point2D.Double nbr : scenario.getPointsAdjacentTo(oldPoints[i])) {
                    double dArea = scenario.getArea(nbr) - area;
                    dArea /= SCALE_CHANGE; //
                    Point2D.Double unitChange = unitVector(oldPoints[i], nbr);
                    dir.x += dArea * unitChange.x;
                    dir.y += dArea * unitChange.y;
                }
                newPoints[i] = PlanarMathUtils.sum(oldPoints[i], dir);
            }

            return newPoints;
        }
    },
    /** Placeholder for next algorithm */
    ALGORITHM3() {

        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            return scenario.getPoints();
        }
    },
    /** Placeholder for next algorithm */
    ALGORITHM4() {

        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            return scenario.getPoints();
        }
    },
    /** Placeholder for next algorithm */
    ALGORITHM5() {

        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            return scenario.getPoints();
        }
    };

    public static Point2D.Double unitVector(Point2D.Double from, Point2D.Double to) {
        return PlanarMathUtils.normalize(new Point2D.Double(to.x - from.x, to.y - from.y));
    }

}
