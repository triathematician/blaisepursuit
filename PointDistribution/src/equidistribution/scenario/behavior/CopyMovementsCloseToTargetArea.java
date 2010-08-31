/*
 * RandomMovement.java
 * Created Aug 18, 2010
 */

package equidistribution.scenario.behavior;

import equidistribution.scenario.EquiTeam.Agent;
import equidistribution.scenario.GlobalParameters;
import java.awt.geom.Point2D;
import org.bm.blaise.scio.algorithm.PolygonUtils;

/**
 * This algorithm alternates between copying movements from adjacent players,
 * and (if outside a prescribed region of areas centered about the average area),
 * moving to/from the boundary or another neighbor.
 * @author Elisha Peterson
 */
public class CopyMovementsCloseToTargetArea implements AgentBehavior {

    public static CopyMovementsCloseToTargetArea DEFAULT_INSTANCE = new CopyMovementsCloseToTargetArea();

    @Override public String toString() { return "copy neighbor movements"; }

    double LOWER_THRESHOLD = .9;
    double UPPER_THRESHOLD = 1.1;
    double COPY_PARAMETER = .5;

    public double getLowerThreshold() { return LOWER_THRESHOLD; }
    public void setLowerThreshold(double d) { LOWER_THRESHOLD = d; }
    public double getUpperThreshold() { return UPPER_THRESHOLD; }
    public void setUpperThreshold(double d) { UPPER_THRESHOLD = d; }
    public double getCopyParameter() { return COPY_PARAMETER; }
    public void setCopyParameter(double d) { COPY_PARAMETER = d; }

    public Point2D.Double compute(Point2D.Double[] border, double target, Agent self, java.util.Set neighbors, GlobalParameters par) {
        if (self.area < LOWER_THRESHOLD * target)
            return moveFromBoundaryOrClosest(border, self, neighbors, par);
        else if (self.area > UPPER_THRESHOLD * target)
            return moveToBoundaryOrFarthest(border, self, neighbors, par);
        else
            return copyNeighborMovements(neighbors, par);
    }

    /** Moves away from the boundary (if it's a boundary point), or away from nearest neighbor otherwise. */
    private Point2D.Double moveFromBoundaryOrClosest(Point2D.Double[] border, Agent self, java.util.Set neighbors, GlobalParameters par) {
        Point2D.Double pointToMoveAwayFrom = self.boundaryAgent
                ? PolygonUtils.closestPointOnPolygon(self, border)
                : AlgorithmUtils.closestNeighbor(self, neighbors);
        return AlgorithmUtils.scaledUnitVector(-par.MAX_MOVE, self, pointToMoveAwayFrom);
    }

    /** Moves toward boundary (if it's a boundary point), or toward farthest neighbor otherwise. */
    private Point2D.Double moveToBoundaryOrFarthest(Point2D.Double[] border, Agent self, java.util.Set neighbors, GlobalParameters par) {
        Point2D.Double pointToMoveToward = self.boundaryAgent
                ? PolygonUtils.closestPointOnPolygon(self, border)
                : AlgorithmUtils.farthestNeighbor(self, neighbors);
        return AlgorithmUtils.scaledUnitVector(par.MAX_MOVE, self, pointToMoveToward);
    }

    /** Copies movement of neighbors */
    private Point2D.Double copyNeighborMovements(java.util.Set neighbors, GlobalParameters par) {
        Point2D.Double result = new Point2D.Double();
        for (Object o : neighbors) {
            Agent n = (Agent) o;
            result.x += n.move.x;
            result.y += n.move.y;
        }
        result.x *= COPY_PARAMETER / neighbors.size();
        result.y *= COPY_PARAMETER / neighbors.size();

        // ensure that the result's length is no more than the global max
        double length = result.distance(0, 0);
        if (length > par.MAX_MOVE) {
            result.x *= par.MAX_MOVE / length;
            result.y *= par.MAX_MOVE / length;
        }

        return result;
    }

}
