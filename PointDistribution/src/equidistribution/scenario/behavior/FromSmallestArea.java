/*
 * RandomMovement.java
 * Created Aug 18, 2010
 */

package equidistribution.scenario.behavior;

import equidistribution.scenario.EquiTeam.Agent;
import equidistribution.scenario.GlobalParameters;
import java.awt.geom.Point2D;

/**
 * Moves an agent away from neighbor with smallest area.
 * @author Elisha Peterson
 */
public class FromSmallestArea implements AgentBehavior {

    public static FromSmallestArea INSTANCE = new FromSmallestArea();

    private FromSmallestArea() {}

    @Override public String toString() { return "move from neighbor with smallest area"; }

    public Point2D.Double compute(Point2D.Double[] border, double target, Agent self, java.util.Set neighbors, GlobalParameters par) {
        double minArea = Double.MAX_VALUE;
        Agent best = null;
        for (Object no : neighbors) {
            Agent n = (Agent) no;
            if (n.area < minArea) {
                minArea = n.area;
                best = n;
            }
        }

        // do not move if already have the largest area
        if (best == null || self.area < best.area)
            return new Point2D.Double();

        return AlgorithmUtils.scaledUnitVector(-par.MAX_MOVE, self, best);
    }

}
