/*
 * RandomMovement.java
 * Created Aug 18, 2010
 */

package equidistribution.scenario.behavior;

import equidistribution.scenario.EquiTeam.Agent;
import equidistribution.scenario.GlobalParameters;
import java.awt.geom.Point2D;

/**
 * Moves an agent toward neighbor with largest area.
 * @author Elisha Peterson
 */
public class TowardLargestArea implements AgentBehavior {

    public static TowardLargestArea INSTANCE = new TowardLargestArea();

    private TowardLargestArea() {}

    @Override public String toString() { return "move to neighbor with largest area"; }

    public Point2D.Double compute(Point2D.Double[] border, double target, Agent self, java.util.Set neighbors, GlobalParameters par) {
        double maxArea = 0;
        Agent best = null;
        for (Object no : neighbors) {
            Agent n = (Agent) no;
            if (n.area > maxArea) {
                maxArea = n.area;
                best = n;
            }
        }

        // do not move if already have the largest area
        if (best == null || self.area > best.area)
            return new Point2D.Double();

        return AlgorithmUtils.scaledUnitVector(par.MAX_MOVE, self, best);
    }

}
