/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.geom.Point2D;

/**
 * This class stores various algorithms used in the point distribution sim.
 * @author Elisha Peterson, ...
 */
public enum Algorithms implements DistributionScenarioAlgorithmInterface {
    /** Algorithm has each point go towards the neighbor with the greatest area. */
    GO_TO_LARGEST_AREA() {
        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            // these are the points in the old scenario
            Point2D.Double[] oldPoints = scenario.getPoints();

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
                // once the point with maximum area is none, we head directly towards that point
                double nbrDist = bestNbr.distance(oldPoints[i]);
                // this is a unit vector pointing from oldPoints[i] to bestNbr
                Point2D.Double posChange = new Point2D.Double((bestNbr.x - oldPoints[i].x)/nbrDist, (bestNbr.y - oldPoints[i].y)/nbrDist);
                // this is the new location
                newPoints[i] = new Point2D.Double(oldPoints[i].x + MAX_STEP_DISTANCE * posChange.x, oldPoints[i].y + MAX_STEP_DISTANCE * posChange.y);
            }

            return newPoints;
        }
    },

    /** Placeholder for next algorithm */
    ALGORITHM2() {
        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            return scenario.getPoints();
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


}
