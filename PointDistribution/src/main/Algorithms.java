/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import org.bm.blaise.scio.algorithm.PolygonUtils;
import scio.coordinate.utils.PlanarMathUtils;

import static main.MovementScenarioParameters.*;

/**
 * This class stores various algorithms used in the point distribution sim.
 * @author Elisha Peterson, ...
 */
public enum Algorithms
        implements DistributionAlgorithm {

    /** Algorithm has each point go towards the neighbor with the greatest area. */
    Go_to_Neighbor_with_Largest_Area() {
        @Override public String toString() { return "Go to neighbor with largest area"; }

        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            // these are the points in the old scenario
            final Point2D.Double[] oldp = scenario.getPoints();
            // this is the number of points
            int n = oldp.length;
            // here we initialize a new array for the new locations of the points
            Point2D.Double[] newp = new Point2D.Double[n];

            // this is the maximum change in distance from old position to new position
            double MAX_STEP_DISTANCE = .01;

            for (int i = 0; i < n; i++) {
                // here we iterate over all the neighbors of the current point to find the one with maximum area
                Point2D.Double bestNbr = null;
                double bestArea = 0.0;
                for (Point2D.Double nbr : scenario.neighbors(oldp[i])) {
                    double area = scenario.cellArea(nbr);
                    if (area > bestArea) {
                        bestNbr = nbr;
                        bestArea = area;
                    }                    
                }
                // don't move if already have largest area
                if (scenario.cellArea(oldp[i]) > bestArea)
                    newp[i] = (Point2D.Double) oldp[i].clone();
                else {
                    // this is a unit vector pointing from oldPoints[i] to bestNbr
                    Point2D.Double unitChange = unitVector(oldp[i], bestNbr);
                    // this is the new location
                    newp[i] = new Point2D.Double(
                            oldp[i].x + MAX_STEP_DISTANCE * unitChange.x,
                            oldp[i].y + MAX_STEP_DISTANCE * unitChange.y);
                }
            }

            // don't let points move outside the boundary
            keepPointsInPolygon(newp, scenario.getDomain());

            return newp;
        }
    },


    /** 
     * This algorithm moves a point by using a weighted summation of directions to
     * its neighbors; the coefficient of the weighting is proportional to the difference
     * in areas (so points will tend to move closer to neighbors with larger areas,
     * and further from neighbors with smaller areas).
     */
    Go_to_Neighbors_Weighted_by_Difference_in_Areas() {
        @Override public String toString() { return "Weight movement by difference in area from neighbors"; }

        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            // this is the set of points in the previous iteration
            Point2D.Double[] oldp = scenario.getPoints();
            // this is the number of points
            int n = oldp.length;
            // here we initialize a new array for the new locations of the points
            Point2D.Double[] newp = new Point2D.Double[n];

            for (int i = 0; i < n; i++) {
                Point2D.Double dir = new Point2D.Double();
                double area = scenario.cellArea(oldp[i]);
                for (Point2D.Double nbr : scenario.neighbors(oldp[i])) {
                    // this is the difference in areas
                    double dArea = scenario.cellArea(nbr) - area;
                    // this is a scaling factor used to determine the relative movement
                    dArea *= MovementScenarioParameters.TO_NEIGHBOR_BY_WEIGHTED_AREA_FACTOR;

                    // this shifts the point by a scaled amount towards the neighbor
                    Point2D.Double unitChange = unitVector(oldp[i], nbr);
                    dir.x += dArea * unitChange.x;
                    dir.y += dArea * unitChange.y;
                }
                newp[i] = PlanarMathUtils.sum(oldp[i], dir);
            }

            // don't let points move outside the boundary
            keepPointsInPolygon(newp, scenario.getDomain());

            return newp;
        }
    },


    /** 
     * This algorithm moves a single point in a random direction.
     */
    Move_One_Point_Randomly() {
        @Override public String toString() { return "Move one point randomly"; }

        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            // this is the set of points in the previous iteration
            Point2D.Double[] oldp = scenario.getPoints();
            // this is the number of points
            int n = oldp.length;
            // stores the new points to be returned
            Point2D.Double[] newp = new Point2D.Double[n];

            // calculate an approximate value to move the point by; use a value related
            // to the perimeter of the polygon and the square root of the number of players
            double scenarioDX = PolygonUtils.perimeterOf(scenario.getDomain());
            scenarioDX *= RANDOM_MOTION_FACTOR / Math.sqrt(n);

            // generate a random index
            int iRandom = (int) Math.floor(Math.random() * oldp.length);

            for (int i = 0; i < n; i++)
                if (i == iRandom) {
                    // move this particular point in a random direction
                    double randDir = 2 * Math.PI * Math.random();
                    newp[i] = new Point2D.Double(
                            oldp[i].x + scenarioDX * Math.cos(randDir),
                            oldp[i].y + scenarioDX * Math.sin(randDir)
                            );

                } else
                    // other points stay in the same place
                    newp[i] = (Point2D.Double) oldp[i].clone();

            // don't let changed point move outside the boundary
            keepPointInPolygon(newp[iRandom], scenario.getDomain());

            return newp;
        }
    },


    /** 
     * This algorithm alternates between copying movements from adjacent players,
     * and (if outside a prescribed region of areas centered about the average area),
     * moving to/from the boundary or another neighbor.
     */
    Copy_Neighbor_Movements__Move_Away_if_Too_Small__Move_To_if_Too_Big() {

        @Override public String toString() { return "Copy neighbor movements outside of threshold area"; }

        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
            // this is the set of points in the previous iteration
            Point2D.Double[] oldp = scenario.getPoints();
            // this is the number of points
            int n = oldp.length;
            // here we initialize a new array for the new locations of the points
            Point2D.Double[] newp = new Point2D.Double[n];
            
            // this describes the movements in the last iteration
            Point2D.Double[] oldmove = scenario.lastMovement();
            // this will store the movements in the new iteration
            Point2D.Double[] newmove = new Point2D.Double[n];

            // this is the average area in the simulation
            double avg = scenario.meanArea();

            
            for (int i = 0; i < n; i++) {
                if (scenario.cellArea(oldp[i]) < MIN_AREA_THRESHOLD * avg) {
                    // move away from the boundary or closest neighbor

                    // this stores the point to move from
                    Point2D.Double moveAwayFrom = null;

                    if (scenario.isBoundaryPoint(oldp[i]))
                        // if adjacent to boundary, move away from nearest boundary
                        moveAwayFrom = PolygonUtils.closestPointOnPolygon(oldp[i], scenario.getDomain());
                    else
                        // otherwise, move away from closest neighbor
                        moveAwayFrom = getClosest(oldp[i], scenario.neighbors(oldp[i]));

                    newmove[i] = new Point2D.Double(
                            -MOVE_TO_NBR_FACTOR * (moveAwayFrom.x - oldp[i].x),
                            -MOVE_TO_NBR_FACTOR * (moveAwayFrom.y - oldp[i].y) );

                } else if (scenario.cellArea(oldp[i]) > MAX_AREA_THRESHOLD * avg) {
                    // move toward the boundary or farthest neighbor

                    // this stores the point to move to
                    Point2D.Double moveToward = null;

                    if (scenario.isBoundaryPoint(oldp[i]))
                        // if adjacent to boundary, move toward nearest boundary
                        moveToward = PolygonUtils.closestPointOnPolygon(oldp[i], scenario.getDomain());
                    else
                        // otherwise, move away from farthest neighbor
                        moveToward = getFarthest(oldp[i], scenario.neighbors(oldp[i]));

                    newmove[i] = new Point2D.Double(
                            MOVE_TO_NBR_FACTOR * (moveToward.x - oldp[i].x),
                            MOVE_TO_NBR_FACTOR * (moveToward.y - oldp[i].y) );

                } else {
                    // mimic neighbors movement
                    
                    newmove[i] = new Point2D.Double();
                    for (Point2D.Double pt : scenario.neighbors(oldp[i])) {
                        // find index of this point to retrieve prior move
                        int index = -1;
                        for (int j = 0; j < n; j++)
                            if (oldp[j] == pt) {
                                index = j;
                                break;
                            }
                        
                        // set the new move
                        newmove[i].x += oldmove[index].x * COPY_MOVEMENT_FACTOR;
                        newmove[i].y += oldmove[index].y * COPY_MOVEMENT_FACTOR;
                    }
                }
            }

            for (int i = 0; i < n; i++)
                newp[i] = new Point2D.Double(oldp[i].x + newmove[i].x, oldp[i].y + newmove[i].y);

            // don't let points move outside the boundary
            keepPointsInPolygon(newp, scenario.getDomain());

            return newp;
        }
    },
    
//    /** Boundary Zone algorithm: no knowledge of areas, just nearest neighbors */
//    Boundary_Zone() {
//        @Override public String toString() { return "Keep out of a boundary zone"; }
//
//        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) {
//            // this is the set of points in the previous iteration
//            Point2D.Double[] oldp = scenario.getPoints();
//            // this is the number of points
//            int n = oldp.length;
//            // here we initialize a new array for the new locations of the points
//            Point2D.Double[] newp = new Point2D.Double[n];
//            // this describes the movements in the last iteration
//            Point2D.Double[] oldmove = scenario.lastMovement();
//
//            for (int i = 0; i < n; i++) {
//                newp[i] = new Point2D.Double(oldp[i].x, oldp[i].y);
//
//                // move to or from closest two neighbors
//                Point2D.Double[] closest = getTwoClosest(oldp[i], scenario.neighbors(oldp[i]));
//
//                if (closest.length > 0) {
//
//                    // if too close to nearest neighbor, move away
//                    if (oldp[i].distance(closest[0]) < BOUNDARY_BUFFER) {
//                        newp[i].x += (newp[i].x - closest[0].x) * MOVE_TO_NBR_FACTOR;
//                        newp[i].y += (newp[i].y - closest[0].y) * MOVE_TO_NBR_FACTOR;
//                    } else {
//                        // otherwise copy movements
//                        for (int j = 0; j < closest.length; j++) {
//                            // find index of this point to retrieve prior move
//                            int index = -1;
//                            for (int k = 0; k < n; k++)
//                                if (oldp[k] == closest[j]) {
//                                    index = k;
//                                    break;
//                                }
//
//                            // set the new move
//                            if (index != -1) {
//                                newp[i].x += oldmove[index].x * COPY_MOVEMENT_FACTOR;
//                                newp[i].y += oldmove[index].y * COPY_MOVEMENT_FACTOR;
//                            }
//                        }
//                    }
//                }
//
//                // don't let point move outside the boundary
//                keepPointInPolygon(newp[i], scenario.getDomain());
//
//                // if end up too close to the boundary, move away from the closest boundary point
//                Point2D.Double boundaryPt = PolygonUtils.closestPointOnPolygon(newp[i], scenario.getDomain());
//                double dist = newp[i].distance(boundaryPt);
//                if (dist < 1e-6) {
//                    //System.out.println("zero dist...");
//                } else if (dist < BOUNDARY_BUFFER) {
//                    double moveFactor = (BOUNDARY_BUFFER - dist) / dist * (1 + .1 * Math.random());
//                    newp[i].x += (newp[i].x - boundaryPt.x) * moveFactor;
//                    newp[i].y += (newp[i].y - boundaryPt.y) * moveFactor;
//                }
//
//                // don't let point move outside the boundary
//                keepPointInPolygon(newp[i], scenario.getDomain());
//            }
//
//            return newp;
//        }
//    },

    /** Test combo algorithm */
    Test_Combo() {
        @Override public String toString() { return "Default TO-LARGEST, first three WEIGHTED"; }
        public Point2D.Double[] getNewPositions(DistributionScenarioInterface scenario) { return TEST1.getNewPositions(scenario); }
    };

    static DistributionAlgorithm TEST1 = new ComboAlgorithm(Algorithms.Go_to_Neighbor_with_Largest_Area){
        {
            addAlternateAlgorithm(Algorithms.Go_to_Neighbors_Weighted_by_Difference_in_Areas, new int[] {0,1,2});
        }
    };


    //
    //
    // UTILITY METHODS
    //
    //

    /** @return unit vector pointing from one vector to another */
    public static Point2D.Double unitVector(Point2D.Double from, Point2D.Double to) {
        return PlanarMathUtils.normalize(new Point2D.Double(to.x - from.x, to.y - from.y));
    }

    /** @return closest point in a set of points to the given center. */
    public static Point2D.Double getClosest(Point2D.Double center, Collection<Point2D.Double> pts) {
        TreeSet<Point2D.Double> sorted = new TreeSet<Point2D.Double>(new PointDistanceSorter(center));
        sorted.addAll(pts);
        return sorted.first();
    }

    /**
     * @return Two closest points in a set of points to the given center. If pts is empty, returns an
     *   empty array. If pts has one element, returns just that element.
     */
    public static Point2D.Double[] getTwoClosest(Point2D.Double center, Collection<Point2D.Double> pts) {
        TreeSet<Point2D.Double> sorted = new TreeSet<Point2D.Double>(new PointDistanceSorter(center));
        sorted.addAll(pts);
        if (pts.size() == 0)
            return new Point2D.Double[]{};
        else if (pts.size() == 1)
            return new Point2D.Double[]{ sorted.first() };

        Iterator<Point2D.Double> it = sorted.iterator();
        return new Point2D.Double[]{ it.next(), it.next() };
    }

    /** @return farthest points in a set of points to the given center. */
    public static Point2D.Double getFarthest(Point2D.Double center, Collection<Point2D.Double> pts) {
        TreeSet<Point2D.Double> sorted = new TreeSet<Point2D.Double>(new PointDistanceSorter(center));
        sorted.addAll(pts);
        return sorted.last();
    }


    /** Sorts a set of points based on distance from a central point. */
    static class PointDistanceSorter implements Comparator<Point2D.Double> {
        Point2D.Double center;
        public PointDistanceSorter(Point2D.Double center) {
            this.center = center;
        }
        public int compare(Point2D.Double a, Point2D.Double b) {
            return Double.compare(a.distanceSq(center), b.distanceSq(center));
        }
    }

    /**
     * Checks to see if specified point is inside polygon. If it is, keeps the point as is;
     * otherwise, moves to closest point that IS inside the polygon.
     * @param position the position (this will be changed by this method)
     * @param polygon the polygon (w/ all segments disjoint)
     */
    static void keepPointInPolygon(Point2D.Double position, Point2D.Double[] polygon) {
        if (PolygonUtils.inPolygon(position, polygon))
            return;

        Point2D.Double closest = PolygonUtils.closestPointOnPolygon(position, polygon);
        position.x = closest.x;
        position.y = closest.y;
    }
    /**
     * Checks to see if specified point is inside polygon. If it is, keeps the point as is;
     * otherwise, moves to closest point that IS inside the polygon.
     * @param positions the positions (this will be changed by this method)
     * @param polygon the polygon (w/ all segments disjoint)
     */
    static void keepPointsInPolygon(Point2D.Double[] positions, Point2D.Double[] polygon) {
        for (Point2D.Double pt : positions)
            keepPointInPolygon(pt, polygon);
    }

}
