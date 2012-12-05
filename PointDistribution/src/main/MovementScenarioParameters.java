/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

/**
 *
 * @author ae3263
 */
public class MovementScenarioParameters {

    //
    // Parameters for weighted area algorithm
    //

    public static double TO_NEIGHBOR_BY_WEIGHTED_AREA_FACTOR = .4;

    public double getToNeighborByWeightedAreaFactor() {
        return TO_NEIGHBOR_BY_WEIGHTED_AREA_FACTOR;
    }

    public void setToNeighborByWeightedAreaFactor(double factor) {
        if (factor >= 0)
            TO_NEIGHBOR_BY_WEIGHTED_AREA_FACTOR = factor;
    }

    //
    // Parameters for random motion algorithm
    //

    // multiplier for "random motion"
    public static double RANDOM_MOTION_FACTOR = .1;


    public double getRandomMotionFactor() {
        return RANDOM_MOTION_FACTOR;
    }

    public void setRandomMotionFactor(double factor) {
        RANDOM_MOTION_FACTOR = factor;
    }

    //
    // Parameters for "closest & area to average" algorithm
    //

    // how closely aligned with neighbors movements
    public static double COPY_MOVEMENT_FACTOR = .2;

    public double getMovementCopyFactor() {
        return COPY_MOVEMENT_FACTOR;
    }

    public void setMovementCopyFactor(double factor) {
        COPY_MOVEMENT_FACTOR = factor;
    }

    // min area at which the special algorithm "kicks in"
    public static double MIN_AREA_THRESHOLD = .9;
    // max area at which the special algorithm "kicks in"
    public static double MAX_AREA_THRESHOLD = 1.1;
    // scale of closest/farthest neighbor to move to/from
    public static double MOVE_TO_NBR_FACTOR = .05;

    public double getMinArea() {
        return MIN_AREA_THRESHOLD;
    }
    public void setMinArea(double area) {
        MIN_AREA_THRESHOLD = area;
    }

    public double getMaxArea() {
        return MAX_AREA_THRESHOLD;
    }
    public void setMaxArea(double area) {
        MAX_AREA_THRESHOLD = area;
    }

    public double getMoveToNeighborFactor() {
        return MOVE_TO_NBR_FACTOR;
    }
    public void setMoveToNeighborFactor(double factor) {
        MOVE_TO_NBR_FACTOR = factor;
    }

    //
    // Parameters for boundary zone algorithm
    //

    public static double BOUNDARY_BUFFER = 0.1;

    public double getBoundaryBuffer() {
        return BOUNDARY_BUFFER;
    }

    public void setBoundaryBuffer(double buffer) {
        if (buffer >= 0)
            BOUNDARY_BUFFER = buffer;
    }


}
